package com.controller;

import com.domain.dto.CountyStatisticsDTO;
import com.service.StatisticsService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CountyStatisticsDTO> getCountyStatistics() {
        return statisticsService.getCountyStatistics();
    }
}
