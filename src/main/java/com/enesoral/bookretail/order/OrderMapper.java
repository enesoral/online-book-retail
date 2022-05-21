package com.enesoral.bookretail.order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface OrderMapper {
    OrderCommand toCommand(Order order);

    Order toDocument(OrderCommand orderCommand);
}
