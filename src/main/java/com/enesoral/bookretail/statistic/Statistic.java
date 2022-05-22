package com.enesoral.bookretail.statistic;

import com.enesoral.bookretail.order.BookAndQuantity;
import com.enesoral.bookretail.order.OrderCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

@Document
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class Statistic {

    @Id
    private String id;

    @Version
    private Long version;

    private String userId;

    private YearMonth yearMonth;

    private Long bookCount;

    private Long orderCount;

    private BigDecimal totalMoneySpent;

    public Statistic(OrderCommand order) {
        this.userId = order.getUserId();
        this.yearMonth = YearMonth.from(order.getDateCreated());
        this.bookCount = 0L;
        this.orderCount = 0L;
        this.totalMoneySpent = BigDecimal.ZERO;
    }

    public void setNewStatistics(OrderCommand orderCommand) {
        requireFieldsNonNull(orderCommand);
        this.orderCount++;
        this.bookCount += orderCommand.getBooksAndQuantities().stream()
                .mapToLong(BookAndQuantity::getQuantity)
                .sum();
        this.totalMoneySpent = this.totalMoneySpent.add(orderCommand.getTotalPrice());
    }

    private void requireFieldsNonNull(OrderCommand orderCommand) {
        Objects.requireNonNull(orderCommand, "Argument 'orderCommand' can not be null!");
        Objects.requireNonNull(orderCommand.getUserId(), "Argument 'userId' can not be null!");
        Objects.requireNonNull(orderCommand.getBooksAndQuantities(), "Argument 'booksAndQuantities' can not be null!");
        Objects.requireNonNull(orderCommand.getTotalPrice(), "Argument 'totalPrice' can not be null!");
        Objects.requireNonNull(orderCommand.getDateCreated(), "Argument 'dateCreated' can not be null!");
    }
}
