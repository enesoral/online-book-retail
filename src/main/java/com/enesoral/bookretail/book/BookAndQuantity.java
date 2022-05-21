package com.enesoral.bookretail.book;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class BookAndQuantity {

    @NotBlank
    private String bookId;

    @Positive
    private Long quantity;
}
