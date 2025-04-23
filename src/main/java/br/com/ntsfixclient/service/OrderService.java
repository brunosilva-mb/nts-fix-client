package br.com.ntsfixclient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.NewOrderSingle;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final Session session;

    public void sendNewOrderSingle(String symbol, double price, int quantity, char side) {
        try {
            NewOrderSingle order = new NewOrderSingle(
                    new ClOrdID(generateOrderId()),
                    new Side(side),
                    new TransactTime(LocalDateTime.now()),
                    new OrdType(OrdType.LIMIT)
            );

            order.set(new Symbol(symbol));
            order.set(new Price(price));
            order.set(new OrderQty(quantity));
            
            Session.sendToTarget(order, session.getSessionID());
            
            log.info("New order sent: {}", order);
        } catch (SessionNotFound e) {
            log.error("Error sending order", e);
            throw new RuntimeException("Failed to send order", e);
        }
    }

    private String generateOrderId() {
        return "ORDER-" + System.currentTimeMillis();
    }
}