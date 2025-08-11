package br.com.ntsfixclient.controller;

import br.com.ntsfixclient.controller.model.MarketRequest;
import br.com.ntsfixclient.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MarketDataController {
    
    private final MarketDataService marketDataService;
    
    @PostMapping("/broker/market-data/subscribe")
    public void subscribe(@RequestBody MarketRequest request) {
        marketDataService.sendMarketDataRequest(request);
    }
}