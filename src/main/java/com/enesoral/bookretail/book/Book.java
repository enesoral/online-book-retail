package com.enesoral.bookretail.book;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Book {

    @Id
    private String id;

    @Version
    private Long version;

    @Indexed(unique = true)
    private String isbn;

    @Indexed(unique = true)
    private String name;

    private Long stock;

    private BigDecimal price;
}
