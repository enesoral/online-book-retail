package com.enesoral.bookretail.statistic;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.Optional;

@Repository
interface StatisticRepository extends MongoRepository<Statistic, String> {
    Optional<Statistic> findByUserIdAndYearMonth(String userId, YearMonth yearMonth);
}