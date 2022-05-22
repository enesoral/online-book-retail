package com.enesoral.bookretail.statistic;

import com.enesoral.bookretail.order.OrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
class StatisticService {

    private final StatisticRepository statisticRepository;

    @KafkaListener(topics = "${kafka.order-statistic-topic}",
            containerFactory = "kafkaListenerContainerFactory",
            autoStartup = "${kafka.enabled}")
    public void listenUserOrderStatisticTopic(OrderCommand order) {
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
