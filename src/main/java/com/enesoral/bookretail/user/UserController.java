package com.enesoral.bookretail.user;

import com.enesoral.bookretail.order.OrderCommand;
import com.enesoral.bookretail.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/{userId}/orders")
    public ResponseEntity<Page<OrderCommand>> getOrders(@PathVariable String userId,
                                                        @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(orderService.getAllByUserId(userId, page), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> save(@Valid @RequestBody UserCommand userCommand) {
        return new ResponseEntity<>(userService.save(userCommand), HttpStatus.OK);
    }
}
