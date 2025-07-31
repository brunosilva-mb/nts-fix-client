package br.com.ntsfixclient.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCrossRequest {
    private String crossId;
    private Integer crossType;
    private Integer crossPrioritization;
    private Integer noSides;
    private String symbol;
    private char orderType;
    private Integer priceType;
    private BigDecimal price;
    private List<OrderSide> sides;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderSide {
        private char side;
        private String clientOrderId;
        private String account;
        private BigDecimal quantity;
        private boolean aggressor;
    }
}
