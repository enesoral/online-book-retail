package com.enesoral.bookretail.user;

import com.enesoral.bookretail.order.OrderService;
import com.enesoral.bookretail.order.UserOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final OrderService orderService;

    @GetMapping("/{userId}/orders")
    public ResponseEntity<Page<UserOrderCommand>> getOrders(@PathVariable @NotBlank String userId,
                                                        @RequestParam(defaultValue = "0") @PositiveOrZero int page) {
        return new ResponseEntity<>(orderService.getAllByUserId(userId, page), HttpStatus.OK);
    }
}
