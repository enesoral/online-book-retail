package com.enesoral.bookretail.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StaticController {

    private final StatisticService statisticService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<StatisticCommand>> getStatisticsByUser(@PathVariable String userId) {
        return new ResponseEntity<>(statisticService.getStatisticsByUser(userId), HttpStatus.OK);
    }
}
