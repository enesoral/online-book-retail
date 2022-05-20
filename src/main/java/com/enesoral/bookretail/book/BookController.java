package com.enesoral.bookretail.book;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> save(@Valid @RequestBody BookCommand recordCommand) {
        return new ResponseEntity<>(bookService.save(recordCommand), HttpStatus.OK);
    }

    @PutMapping("/stocks")
    public ResponseEntity<BookCommand> update(@Valid @RequestBody StockUpdateCommand stockUpdateCommand) {
        return new ResponseEntity<>(bookService.updateStock(stockUpdateCommand), HttpStatus.OK);
    }
}
