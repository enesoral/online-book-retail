package com.enesoral.bookretail.order;

import com.enesoral.bookretail.book.BookAndQuantity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
class Order {

    @Id
    private String id;

    private String userId;

    private List<BookAndQuantity> booksAndQuantities;

    private BigDecimal totalPrice;

    private LocalDateTime dateCreated;

    @Builder
    public static Order of(String userId, List<BookAndQuantity> booksAndQuantities, BigDecimal totalPrice) {
        Order order = new Order();
        order.setUserId(userId);
        order.setBooksAndQuantities(booksAndQuantities);
        order.setTotalPrice(totalPrice);
        order.setDateCreated(LocalDateTime.now());
        return order;
    }

    public static Order generate(OrderRequest orderRequest, BigDecimal totalPrice) {
        return Order.builder()
                .userId(orderRequest.getUserId())
                .totalPrice(totalPrice)
                .booksAndQuantities(orderRequest.getBooksAndQuantities())
                .build();
    }
}
