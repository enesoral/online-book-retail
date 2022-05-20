package com.enesoral.bookretail.book;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Getter
@Setter
class Book {

    @Id
    private String id;

    @Indexed(unique = true)
    private String isbn;

    @Indexed(unique = true)
    private String name;

    private Long stock;

    private BigDecimal price;
}
