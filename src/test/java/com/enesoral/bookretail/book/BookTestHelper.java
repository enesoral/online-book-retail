package com.enesoral.bookretail.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class BookTestHelper {

    public static final String ID = ObjectId.get().toString();
    public static final String UNRECOGNIZED_ID = ObjectId.get().toString();
    public static final long STOCK = 15;
    public static final long NEW_STOCK = 30;
    public static final String ISBN = "9783161484100";
    public static final String ISBN_2 = "9783161484101";
    public static final String ISBN_3 = "9783161484102";
    public static final String ISBN_4 = "9783161484103";
    public static final String ISBN_5 = "9783161484104";
    public static final String ISBN_6 = "9783161484105";
    public static final BigDecimal PRICE = BigDecimal.valueOf(14.5);
    public static final String BOOK_NAME = "Gulliver's Travels";
    public static final String BOOK_NAME_2 = "Nineteen Eighty-Four";
    public static final String BOOK_NAME_3 = "Frankenstein";
    public static final String BOOK_NAME_4 = "Robinson Crusoe";
    public static final String BOOK_NAME_5 = "Tom Jones";
    public static final String BOOK_NAME_6 = "Emma";
    public static final long SUFFICIENT_QUANTITY = 10;
    public static final long INSUFFICIENT_QUANTITY = 20;
    public static final BigDecimal CALCULATED_PRICE = PRICE.multiply(BigDecimal.valueOf(SUFFICIENT_QUANTITY));
    public static final String NOT_SAVED_ID = ObjectId.get().toString();

    private final WebApplicationContext applicationContext;

    private final ObjectMapper objectMapper;

    private final MockMvc mvc;

    public BookTestHelper(WebApplicationContext webApplicationContext, ObjectMapper objectMapper) {
        this.applicationContext = webApplicationContext;
        this.objectMapper = objectMapper;
        this.mvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    public Book createBook(String bookName, String isbn) throws Exception {
        final Book newBook = Book.builder()
                .name(bookName)
                .isbn(isbn)
                .price(PRICE)
                .stock(STOCK)
                .build();

        return objectMapper.readValue(
                mvc.perform(
                        MockMvcRequestBuilders.post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(newBook))
                        )
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), Book.class);
    }

    public void updateStock(String bookId, Long newStock) throws Exception {
        final StockUpdateCommand stockUpdateCommand = StockUpdateCommand.builder()
                .bookId(bookId)
                .newStock(newStock)
                .build();

        mvc.perform(
                MockMvcRequestBuilders.put("/books/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(stockUpdateCommand)))
                .andExpect(status().isOk());
    }
}
