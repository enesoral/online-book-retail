package com.enesoral.bookretail.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserOrderCommand {

    private String id;

    private List<BookAndQuantity> booksAndQuantities;

    private BigDecimal totalPrice;

    private LocalDateTime dateCreated;
}
