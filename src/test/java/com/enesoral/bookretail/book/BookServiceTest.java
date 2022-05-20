package com.enesoral.bookretail.book;

import com.enesoral.bookretail.common.exception.BookNotFoundException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BookServiceTest {

    public static final String ID = ObjectId.get().toString();
    public static final String UNRECOGNIZED_ID = ObjectId.get().toString();
    public static final long STOCK = 15;
    public static final long NEW_STOCK = 30;
    public static final String ISBN = "9783161484100";
    public static final BigDecimal PRICE = BigDecimal.valueOf(14.5);
    public static final String BOOK_NAME = "Gulliver's Travels";

    public static Book bookResponse;
    public static Book bookRequest;
    public static BookCommand bookCommand;
    public static StockUpdateCommand validStockUpdateCommand;
    public static StockUpdateCommand invalidStockUpdateCommand;

    @Spy
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
    
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookResponse = Book.builder()
                .id(ID)
                .isbn(ISBN)
                .name(BOOK_NAME)
                .stock(STOCK)
                .price(PRICE)
                .build();

        bookRequest = Book.builder()
                .isbn(ISBN)
                .name(BOOK_NAME)
                .stock(STOCK)
                .price(PRICE)
                .build();

        bookCommand = bookMapper.toCommand(bookResponse);

        validStockUpdateCommand = StockUpdateCommand.builder()
                .bookId(ID)
                .newStock(NEW_STOCK)
                .build();

        invalidStockUpdateCommand = StockUpdateCommand.builder()
                .bookId(UNRECOGNIZED_ID)
                .newStock(NEW_STOCK)
                .build();
    }

    @Test
    void testValidBookSave() {
        lenient().when(bookRepository.save(bookRequest))
                .thenReturn(bookResponse);

        final Book savedBook = bookService.save(bookCommand);

        verify(bookRepository, times(1)).save(bookRequest);
        assertThat(savedBook)
                .isNotNull()
                .matches(book -> book.getId().equals(bookResponse.getId()))
                .matches(book -> book.getIsbn().equals(bookResponse.getIsbn()))
                .matches(book -> book.getName().equals(bookResponse.getName()))
                .matches(book -> book.getPrice().equals(bookResponse.getPrice()))
                .matches(book -> book.getStock().equals(bookResponse.getStock()));
    }

    @Test
    void testValidUpdateStock() {
        lenient().when(bookRepository.findById(ID))
                .thenReturn(Optional.of(bookResponse));

        final BookCommand bookCommand = bookService.updateStock(validStockUpdateCommand);

        verify(bookRepository, times(1)).findById(ID);
        assertThat(bookCommand)
                .isNotNull()
                .matches(book -> book.getIsbn().equals(bookResponse.getIsbn()))
                .matches(book -> book.getName().equals(bookResponse.getName()))
                .matches(book -> book.getPrice().equals(bookResponse.getPrice()))
                .matches(book -> book.getStock().equals(validStockUpdateCommand.getNewStock()));
    }

    @Test
    void testInvalidUpdateStock() {
        lenient().when(bookRepository.findById(ID))
                .thenReturn(Optional.of(bookResponse));

        verify(bookRepository, times(1)).findById(ID);
        assertThrows(BookNotFoundException.class,
                () -> bookService.updateStock(invalidStockUpdateCommand));
    }
}