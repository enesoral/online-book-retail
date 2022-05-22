package com.enesoral.bookretail.statistic;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@Setter
class StatisticCommand {

    private YearMonth yearMonth;

    private Long bookCount;

    private Long orderCount;

    private BigDecimal totalMoneySpent;
}
