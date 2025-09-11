package br.com.ntsfixclient.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MarketRequest {
    private char subscriptionRequestType;
    private Integer marketDepth;
    private Integer updateType;
    private List<Character> entryTypes;
    private List<String> symbols;
}
