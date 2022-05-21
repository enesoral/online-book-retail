package com.enesoral.bookretail.book;

import com.enesoral.bookretail.common.exception.BookNotFoundException;
import com.enesoral.bookretail.common.exception.InsufficientStockException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    public static final String ID = ObjectId.get().toString();
    public static final String UNRECOGNIZED_ID = ObjectId.get().toString();
    public static final long STOCK = 15;
    public static final long NEW_STOCK = 30;
    public static final String ISBN = "9783161484100";
    public static final BigDecimal PRICE = BigDecimal.valueOf(14.5);
    public static final String BOOK_NAME = "Gulliver's Travels";
    public static final long SUFFICIENT_QUANTITY = 10;
    public static final long INSUFFICIENT_QUANTITY = 20;
    public static final BigDecimal CALCULATED_PRICE = PRICE.multiply(BigDecimal.valueOf(SUFFICIENT_QUANTITY));
    public static final String NOT_SAVED_ID = ObjectId.get().toString();

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
    void givenValidBookCommand_whenPerformSaving_thenReturnSuccessResponse() {
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
    void givenValidUpdateCommand_whenUpdatingStock_thenReturnSuccessResponse() {
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
    void givenValidUpdateCommand_whenBookNotExist_thenException() {
        lenient().when(bookRepository.findById(ID))
                .thenReturn(Optional.of(bookResponse));

        assertThrows(BookNotFoundException.class,
                () -> bookService.updateStock(invalidStockUpdateCommand));
        verify(bookRepository, times(1)).findById(UNRECOGNIZED_ID);
    }

    @Test
    void givenValidBookAndQuantity_whenSufficientStock_thenReturnSuccessResponse() {
        lenient().when(bookRepository.save(bookRequest))
                .thenReturn(bookResponse);

        final Book savedBook = bookService.save(bookCommand);

        lenient().when(bookRepository.findById(savedBook.getId()))
                .thenReturn(Optional.of(savedBook));

        final BookAndQuantity bookAndQuantity = BookAndQuantity.builder()
                .bookId(savedBook.getId())
                .quantity(SUFFICIENT_QUANTITY)
                .build();

        final BigDecimal totalPrice = bookService.getTotalPriceAndReduceStock(bookAndQuantity);

        assertEquals(CALCULATED_PRICE, totalPrice);
    }

    @Test
    void givenValidBookAndQuantity_whenBookNotSaved_thenException() {
        final BookAndQuantity bookAndQuantity = BookAndQuantity.builder()
                .bookId(NOT_SAVED_ID)
                .quantity(SUFFICIENT_QUANTITY)
                .build();

        assertThrows(BookNotFoundException.class, () -> bookService.getTotalPriceAndReduceStock(bookAndQuantity));
    }

    @Test
    void givenValidBookAndQuantity_whenInsufficientStock_thenException() {
        lenient().when(bookRepository.save(bookRequest))
                .thenReturn(bookResponse);

        final Book savedBook = bookService.save(bookCommand);

        lenient().when(bookRepository.findById(savedBook.getId()))
                .thenReturn(Optional.of(savedBook));

        final BookAndQuantity bookAndQuantity = BookAndQuantity.builder()
                .bookId(savedBook.getId())
                .quantity(INSUFFICIENT_QUANTITY)
                .build();

        assertThrows(InsufficientStockException.class, () -> bookService.getTotalPriceAndReduceStock(bookAndQuantity));
    }
}