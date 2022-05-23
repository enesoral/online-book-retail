package com.enesoral.bookretail.book;

import com.enesoral.bookretail.common.exception.BookNotFoundException;
import com.enesoral.bookretail.common.exception.InsufficientStockException;
import com.enesoral.bookretail.order.BookAndQuantity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    @Retryable(maxAttempts = 10, value = OptimisticLockingFailureException.class)
    public BigDecimal getTotalPriceAndReduceStock(@Valid BookAndQuantity bookAndQuantity) {
        final Optional<Book> bookById = bookRepository.findById(bookAndQuantity.getBookId());
        return bookById.map(book -> {
                    tryReduceStock(book, bookAndQuantity.getQuantity());
                    return book.getPrice().multiply(BigDecimal.valueOf(bookAndQuantity.getQuantity()));
                })
                .orElseThrow(() -> new BookNotFoundException(
                        String.format("Book not found with id: %s", bookAndQuantity.getBookId())));
    }

    Book save(BookCommand bookCommand) {
        return bookRepository.save(toDocument(bookCommand));
    }

    @Retryable(maxAttempts = 5, value = OptimisticLockingFailureException.class)
    BookCommand updateStock(StockUpdateCommand stockUpdateCommand) {
        final Optional<Book> bookById = bookRepository.findById(stockUpdateCommand.getBookId());
        bookById.ifPresentOrElse(
                book -> {
                    log.info("Book({}) stock will be set to {}", book.getId(), stockUpdateCommand.getNewStock());
                    book.setStock(stockUpdateCommand.getNewStock());
                    bookRepository.save(book);
                },
                () -> {
                    throw new BookNotFoundException(
                            String.format("Book not found with id: %s", stockUpdateCommand.getBookId()));
                }
        );
        return toCommand(bookById.orElseThrow());
    }

    private void tryReduceStock(Book book, Long quantity) {
        if (book.getStock() < quantity) {
            throw new InsufficientStockException(book.getId(), book.getStock(), quantity);
        }

        log.info("Book({}) stock will be reduced by {}", book.getId(), quantity);
        book.setStock(book.getStock() - quantity);
        bookRepository.save(book);

    }

    private Book toDocument(BookCommand bookCommand) {
        return bookMapper.toDocument(bookCommand);
    }

    private BookCommand toCommand(Book book) {
        return bookMapper.toCommand(book);
    }
}
