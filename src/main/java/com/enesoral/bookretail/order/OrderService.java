package com.enesoral.bookretail.order;

import com.enesoral.bookretail.book.BookService;
import com.enesoral.bookretail.common.exception.OrderNotFoundException;
import com.enesoral.bookretail.common.exception.UserNotFoundException;
import com.enesoral.bookretail.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderService {

    private final UserService userService;
    private final BookService bookService;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, OrderCommand> orderCommandKafkaTemplate;
    private final OrderRepository orderRepository;
    private final String userOrderStatisticTopic;

    public OrderService(UserService userService, BookService bookService, OrderMapper orderMapper,
                        KafkaTemplate<String, OrderCommand> orderCommandKafkaTemplate,
                        OrderRepository orderRepository,
                        @Value("${kafka.order-statistic-topic}") String userOrderStatisticTopic) {
        this.userService = userService;
        this.bookService = bookService;
        this.orderMapper = orderMapper;
        this.orderCommandKafkaTemplate = orderCommandKafkaTemplate;
        this.orderRepository = orderRepository;
        this.userOrderStatisticTopic = userOrderStatisticTopic;
    }

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

        final OrderCommand orderCommand = toCommand(orderRepository.save(Order.generate(orderRequest, totalPrice)));
        orderCommandKafkaTemplate.send(userOrderStatisticTopic, orderCommand);
        return orderCommand;
    }

    public Page<UserOrderCommand> getAllByUserId(String userId, int page) {
        return orderRepository.findAllByUserId(userId, PageRequest.of(page, 10));
    }

    public Page<OrderCommand> getAllByDateInterval(DateInterval dateIntervalRequest, int page) {
        return orderRepository.findAllByDateCreatedBetween(dateIntervalRequest.getStart(),
                dateIntervalRequest.getEnd(),
                PageRequest.of(page, 10));
    }

    private OrderCommand toCommand(Order order) {
        return orderMapper.toCommand(order);
    }
}
