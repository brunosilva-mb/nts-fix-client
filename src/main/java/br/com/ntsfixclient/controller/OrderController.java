package br.com.ntsfixclient.controller;

import br.com.ntsfixclient.controller.model.OrderRequest;
import br.com.ntsfixclient.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequest request) {
        orderService.sendNewOrderSingle(
            request.getSymbol(),
            request.getPrice(),
            request.getQuantity(),
            request.getSide()
        );
    }

    @PostMapping("/stop")
    public void stop() {
        orderService.destroySession();
    }
}