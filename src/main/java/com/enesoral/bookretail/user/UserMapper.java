package com.enesoral.bookretail.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {
    UserCommand toCommand(User book);

    User toDocument(UserCommand userCommand);
}
