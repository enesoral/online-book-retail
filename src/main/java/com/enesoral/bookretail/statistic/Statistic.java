package com.enesoral.bookretail.statistic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.YearMonth;

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
}
