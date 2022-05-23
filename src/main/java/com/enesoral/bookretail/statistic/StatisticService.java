package com.enesoral.bookretail.statistic;

import com.enesoral.bookretail.order.OrderCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class StatisticService {

    private final StatisticRepository statisticRepository;

    public List<StatisticCommand> getStatisticsByUser(String userId) {
        return statisticRepository.findAllByUserId(userId);
    }

    @KafkaListener(topics = "${kafka.order-statistic-topic}",
            containerFactory = "kafkaListenerContainerFactory",
            autoStartup = "${kafka.enabled}")
    @Retryable(maxAttempts = 2, value = OptimisticLockingFailureException.class)
    void listenUserOrderStatisticTopic(OrderCommand order) {
        log.info("Statistics will be updated for User({})", order.getUserId());
        updateStatistics(order);
    }

    private void updateStatistics(OrderCommand order) {
        final Statistic statistic =
                statisticRepository.findByUserIdAndYearMonth(order.getUserId(), YearMonth.from(order.getDateCreated()))
                .orElse(new Statistic(order));

        statistic.setNewStatistics(order);
        statisticRepository.save(statistic);
    }


}
