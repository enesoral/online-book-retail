package com.enesoral.bookretail.book;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface BookMapper {
    BookCommand toCommand(Book book);

    Book toDocument(BookCommand bookCommand);
}
