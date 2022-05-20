package com.enesoral.bookretail.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);
}
