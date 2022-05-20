package com.enesoral.bookretail.book;

import com.enesoral.bookretail.common.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    Book save(BookCommand bookCommand) {
        return bookRepository.save(toDocument(bookCommand));
    }

    @Retryable(maxAttempts = 5, value = OptimisticLockingFailureException.class)
    BookCommand updateStock(StockUpdateCommand stockUpdateCommand) {
        final Optional<Book> bookById = bookRepository.findById(stockUpdateCommand.getBookId());
        bookById.ifPresentOrElse(
                book -> {
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

    private Book toDocument(BookCommand bookCommand) {
        return bookMapper.toDocument(bookCommand);
    }

    private BookCommand toCommand(Book book) {
        return bookMapper.toCommand(book);
    }
}
