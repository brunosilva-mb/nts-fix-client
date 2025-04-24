package br.com.ntsfixclient.service;

import jakarta.annotation.PreDestroy;
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

    private final SessionSettings settings;
    private final Initiator initiator;

    private SessionID sessionID;

    public void sendNewOrderSingle(String symbol, double price, int quantity, char side) {
        NewOrderSingle order = new NewOrderSingle(
                new ClOrdID("ORDER-" + System.currentTimeMillis()),
                new Side(side),
                new TransactTime(LocalDateTime.now()),
                new OrdType(OrdType.LIMIT)
        );

        order.set(new Symbol(symbol));
        order.set(new Price(price));
        order.set(new OrderQty(quantity));
        order.set(new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));

        send(order);
        log.info("New order sent: {}", order);
    }

    private void send(Message message) {
        sessionID = new SessionID("FIXT.1.1", "user-bc1", "PEB");
        Session session = Session.lookupSession(sessionID);
        if (session != null && session.isLoggedOn()) {
            session.send(message);
            log.info("NewOrderSingle message sent: {}", message.toString().replace('\u0001', '|'));
        } else {
            log.warn("Session not active. Cannot send message.");
        }
    }

    @PreDestroy
    public void destroySession() {
        if (initiator != null && initiator.isLoggedOn()) {
            Session session = Session.lookupSession(sessionID);
            if (session != null) {
                session.logout("User requested logout");
            }
            initiator.stop();
            log.info("FIX Client stopped.");
        }
    }
}