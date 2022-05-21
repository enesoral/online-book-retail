package com.enesoral.bookretail.order;

import com.enesoral.bookretail.book.BookService;
import com.enesoral.bookretail.common.exception.OrderNotFoundException;
import com.enesoral.bookretail.common.exception.UserNotFoundException;
import com.enesoral.bookretail.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;
    private final BookService bookService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public OrderCommand getById(String id) {
        final Optional<Order> orderById = orderRepository.findById(id);
        return orderById.map(this::toCommand)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    public OrderCommand processOrder(OrderRequest orderRequest) {
        if (!userService.isUserExist(orderRequest.getUserId())) {
            throw new UserNotFoundException(orderRequest.getUserId());
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BookAndQuantity bookAndQuantity : orderRequest.getBooksAndQuantities()) {
            final BigDecimal price = bookService.getTotalPriceAndReduceStock(bookAndQuantity);
            totalPrice = totalPrice.add(price);
        }

        return toCommand(orderRepository.save(Order.generate(orderRequest, totalPrice)));
    }

    public Page<UserOrderCommand> getAllByUserId(String userId, int page) {
        return orderRepository.findAllByUserId(userId, PageRequest.of(page, 10));
    }

    private OrderCommand toCommand(Order order) {
        return orderMapper.toCommand(order);
    }
}
