package com.enesoral.bookretail.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
interface OrderRepository extends MongoRepository<Order, String> {

    Page<UserOrderCommand> findAllByUserId(String userId, Pageable pageable);

    Page<OrderCommand> findAllByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
