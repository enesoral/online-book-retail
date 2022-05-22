package com.enesoral.bookretail.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.enesoral.bookretail.book.BookTestHelper.BOOK_NAME;
import static com.enesoral.bookretail.book.BookTestHelper.BOOK_NAME_2;
import static com.enesoral.bookretail.book.BookTestHelper.ISBN;
import static com.enesoral.bookretail.book.BookTestHelper.ISBN_2;
import static com.enesoral.bookretail.book.BookTestHelper.NEW_STOCK;
import static com.enesoral.bookretail.book.BookTestHelper.PRICE;
import static com.enesoral.bookretail.book.BookTestHelper.STOCK;
import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka
@AutoConfigureDataMongo
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test")
class BookIntegrationTest {

    @Autowired
    private BookTestHelper testHelper;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void givenValidBookCommand_whenPerformSaving_thenReturnSuccessResponse() throws Exception {
        testHelper.createBook(BOOK_NAME, ISBN);

        final Optional<Book> bookByName = bookRepository.findByName(BOOK_NAME);
        assertThat(bookByName)
                .isPresent()
                .map(Book::getId)
                .isNotNull();

        assertThat(bookByName.get())
                .matches(book -> book.getName().equals(BOOK_NAME))
                .matches(book -> book.getIsbn().equals(ISBN))
                .matches(book -> book.getPrice().equals(PRICE))
                .matches(book -> book.getStock().equals(STOCK));
    }

    @Test
    void givenValidUpdateCommand_whenUpdatingStock_thenReturnSuccessResponse() throws Exception {
        final Book bookResponse = testHelper.createBook(BOOK_NAME_2, ISBN_2);

        testHelper.updateStock(bookResponse.getId(), NEW_STOCK);

        final Optional<Book> bookByName = bookRepository.findByName(BOOK_NAME_2);
        assertThat(bookByName)
                .isPresent()
                .map(Book::getId)
                .isNotNull();

        assertThat(bookByName.get())
                .matches(book -> book.getStock().equals(NEW_STOCK));
    }

}
