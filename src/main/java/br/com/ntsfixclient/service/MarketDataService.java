package br.com.ntsfixclient.service;

import br.com.ntsfixclient.controller.model.MarketRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.MarketDataRequest;

@Slf4j
@Service
public class MarketDataService extends FixService {

    public MarketDataService(Initiator initiator, SessionSettings settings) {
        super(initiator, settings);
    }

    public void sendMarketDataRequest(MarketRequest request) {
        MarketDataRequest marketData = new MarketDataRequest(
            new MDReqID("subscription"),
            new SubscriptionRequestType(request.getSubscriptionRequestType()),
            new MarketDepth(request.getMarketDepth())
        );

        marketData.set(new NoMDEntryTypes(request.getEntryTypes().size()));
        request.getEntryTypes().forEach(
            entry -> {
                MarketDataRequest.NoMDEntryTypes group = new MarketDataRequest.NoMDEntryTypes();
                group.set(new MDEntryType(entry));
                marketData.addGroup(group);
            }
        );
        log.info("Subscribe sent: {}", marketData);
    }
}