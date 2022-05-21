package com.enesoral.bookretail.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderCommand> getById(@PathVariable String orderId) {
        return new ResponseEntity<>(orderService.getById(orderId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderCommand> order(@Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.processOrder(orderRequest), HttpStatus.OK);
    }

}
