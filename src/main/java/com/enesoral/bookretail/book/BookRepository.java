package com.enesoral.bookretail.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByName(String name);
}
