package com.enesoral.bookretail.order;

import com.enesoral.bookretail.book.Book;
import com.enesoral.bookretail.book.BookTestHelper;
import com.enesoral.bookretail.user.User;
import com.enesoral.bookretail.user.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.enesoral.bookretail.book.BookTestHelper.BOOK_NAME_3;
import static com.enesoral.bookretail.book.BookTestHelper.BOOK_NAME_4;
import static com.enesoral.bookretail.book.BookTestHelper.BOOK_NAME_5;
import static com.enesoral.bookretail.book.BookTestHelper.BOOK_NAME_6;
import static com.enesoral.bookretail.book.BookTestHelper.ISBN_3;
import static com.enesoral.bookretail.book.BookTestHelper.ISBN_4;
import static com.enesoral.bookretail.book.BookTestHelper.ISBN_5;
import static com.enesoral.bookretail.book.BookTestHelper.ISBN_6;
import static com.enesoral.bookretail.book.BookTestHelper.UNRECOGNIZED_ID;
import static com.enesoral.bookretail.order.OrderTestHelper.QUANTITY_1;
import static com.enesoral.bookretail.order.OrderTestHelper.QUANTITY_2;
import static com.enesoral.bookretail.user.UserTestHelper.EMAIL_2;
import static com.enesoral.bookretail.user.UserTestHelper.FULL_NAME_2;
import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka
@AutoConfigureDataMongo
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test")
class OrderIntegrationTest {

    @Autowired
    private OrderTestHelper orderTestHelper;

    @Autowired
    private BookTestHelper bookTestHelper;

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void givenValidOrderRequest_whenProcessingOrder_thenReturnSuccessResponse() throws Exception {

        final Book book = bookTestHelper.createBook(BOOK_NAME_3, ISBN_3);
        final Book book2 = bookTestHelper.createBook(BOOK_NAME_4, ISBN_4);

        final User createdUser = userTestHelper.createUser(FULL_NAME_2, EMAIL_2);

        final List<BookAndQuantity> bookAndQuantityList =
                orderTestHelper.createBookAndQuantityList(book, book2, QUANTITY_1, QUANTITY_2);

        final OrderCommand createdOrder = orderTestHelper.createOrder(createdUser.getId(), bookAndQuantityList);

        BigDecimal expectedPrice = orderTestHelper.calculatePrice(book, book2, QUANTITY_1, QUANTITY_2);

        assertThat(createdOrder)
                .isNotNull()
                .matches(order -> order.getTotalPrice().equals(expectedPrice));
    }

    @Test
    void givenValidOrderRequest_whenUserNotExists_throwException() throws Exception {
        final Book book = bookTestHelper.createBook(BOOK_NAME_5, ISBN_5);
        final Book book2 = bookTestHelper.createBook(BOOK_NAME_6, ISBN_6);

        final List<BookAndQuantity> bookAndQuantityList =
                orderTestHelper.createBookAndQuantityList(book, book2, QUANTITY_1, QUANTITY_2);

        orderTestHelper.tryCreatingOrderWithNonExistentUser(UNRECOGNIZED_ID, bookAndQuantityList);
    }



    @Test
    void getAllByUserId() {
    }

    @Test
    void getAllByDateInterval() {
    }
}