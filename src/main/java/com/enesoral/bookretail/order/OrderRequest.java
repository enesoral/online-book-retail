package com.enesoral.bookretail.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
class OrderRequest {

    @NotBlank
    private String userId;

    @NotEmpty
    private List<@Valid BookAndQuantity> booksAndQuantities;
}
