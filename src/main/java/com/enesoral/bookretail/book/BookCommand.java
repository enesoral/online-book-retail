package com.enesoral.bookretail.book;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Builder
class BookCommand {

    @NotBlank
    private String isbn;

    @NotBlank
    private String name;

    @PositiveOrZero
    private Long stock;

    @PositiveOrZero
    private BigDecimal price;
}
