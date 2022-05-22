package com.enesoral.bookretail.statistic;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface StatisticRepository extends MongoRepository<Statistic, String> {

}