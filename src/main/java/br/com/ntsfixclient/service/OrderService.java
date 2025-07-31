package br.com.ntsfixclient.service;

import br.com.ntsfixclient.controller.model.OrderCrossRequest;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.NewOrderCross;
import quickfix.fix50sp2.NewOrderSingle;
import quickfix.fix50sp2.component.Instrument;
import quickfix.fix50sp2.component.OrderQtyData;
import quickfix.fix50sp2.component.SideCrossOrdModGrp;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

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
        log.info("New order single sent: {}", order);
    }

    public void sendNewOrderCross(OrderCrossRequest request) {
        NewOrderCross order = new NewOrderCross(
            new CrossID(request.getCrossId()),
            new CrossType(request.getCrossType()),
            new CrossPrioritization(request.getCrossPrioritization()),
            new TransactTime(LocalDateTime.now()),
            new OrdType(request.getOrderType())
        );

        var sideCrossGroup = new SideCrossOrdModGrp();
        request.getSides().forEach(
            orderSide -> {
                var sideGroup = new SideCrossOrdModGrp.NoSides();
                sideGroup.set(new Side(orderSide.getSide()));
                sideGroup.set(new ClOrdID(orderSide.getClientOrderId()));
                sideGroup.set(new Account(orderSide.getAccount()));

                var quantityData = new OrderQtyData();
                quantityData.set(new OrderQty(orderSide.getQuantity().doubleValue()));
                sideGroup.set(quantityData);

                sideGroup.setBoolean(1057, orderSide.isAggressor());
            }
        );

        order.set(sideCrossGroup);

        var instrument = new Instrument();
        instrument.set(new Symbol(request.getSymbol()));
        order.set(instrument);

        order.set(new Price(request.getPrice().doubleValue()));

        send(order);
        log.info("New order Cross sent: {}", order);
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