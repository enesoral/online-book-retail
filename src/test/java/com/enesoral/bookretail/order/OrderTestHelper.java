package com.enesoral.bookretail.order;

import com.enesoral.bookretail.book.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class OrderTestHelper {

    public static final long QUANTITY_1 = 5L;
    public static final long QUANTITY_2 = 10L;

    private final WebApplicationContext applicationContext;

    private final ObjectMapper objectMapper;

    private final MockMvc mvc;

    public OrderTestHelper(WebApplicationContext webApplicationContext, ObjectMapper objectMapper) {
        this.applicationContext = webApplicationContext;
        this.objectMapper = objectMapper;
        this.mvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    public OrderCommand createOrder(String userId, List<BookAndQuantity> bookAndQuantityList) throws Exception {
        final OrderRequest orderRequest = createOrderRequest(userId, bookAndQuantityList);

        return objectMapper.readValue(
                mvc.perform(
                        MockMvcRequestBuilders.post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(orderRequest))
                        )
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), OrderCommand.class);
    }

    public void tryCreatingOrderWithNonExistentUser(String userId, List<BookAndQuantity> bookAndQuantityList) throws Exception {
        final OrderRequest orderRequest = createOrderRequest(userId, bookAndQuantityList);

        final String errorMessage = mvc.perform(MockMvcRequestBuilders.post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getErrorMessage();

        assertThat(errorMessage)
                .isEqualTo(String.format("User not found with id: %s", userId));
    }

    List<BookAndQuantity> createBookAndQuantityList(Book book1, Book book2, Long quantity1, Long quantity2) {
        BookAndQuantity bookAndQuantity1 = BookAndQuantity.builder()
                .bookId(book1.getId())
                .quantity(quantity1)
                .build();

        BookAndQuantity bookAndQuantity2 = BookAndQuantity.builder()
                .bookId(book2.getId())
                .quantity(quantity2)
                .build();

        return List.of(bookAndQuantity1, bookAndQuantity2);
    }


    BigDecimal calculatePrice(Book book, Book book2, Long quantity1, Long quantity2) {
        BigDecimal expectedPrice = book.getPrice().multiply(BigDecimal.valueOf(quantity1));
        BigDecimal expectedPrice2 = book2.getPrice().multiply(BigDecimal.valueOf(quantity2));
        return expectedPrice.add(expectedPrice2);
    }

    private OrderRequest createOrderRequest(String userId, List<BookAndQuantity> bookAndQuantityList) {
        return OrderRequest.builder()
                .userId(userId)
                .booksAndQuantities(bookAndQuantityList)
                .build();
    }
}
