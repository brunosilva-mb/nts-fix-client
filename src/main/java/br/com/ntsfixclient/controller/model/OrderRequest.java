package br.com.ntsfixclient.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {
    private String symbol;
    private double price;
    private int quantity;
    private char side;
}
