package com.enesoral.bookretail.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    Book save(BookCommand bookCommand) {
        return bookRepository.save(toDocument(bookCommand));
    }

    private Book toDocument(BookCommand bookCommand) {
        return bookMapper.toDocument(bookCommand);
    }
}
