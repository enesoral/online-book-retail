package com.enesoral.bookretail.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderCommand> getById(@PathVariable @NotBlank String orderId) {
        return new ResponseEntity<>(orderService.getById(orderId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<OrderCommand>> getByDateInterval(@Valid DateInterval dateInterval,
                                                                @RequestParam(defaultValue = "0") @PositiveOrZero int page) {
        return new ResponseEntity<>(orderService.getAllByDateInterval(dateInterval, page), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderCommand> order(@Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.processOrder(orderRequest), HttpStatus.CREATED);
    }

}
