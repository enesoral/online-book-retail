package com.enesoral.bookretail.book;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder
class StockUpdateCommand {

    @NotBlank
    String bookId;

    @PositiveOrZero
    private Long newStock;
}
