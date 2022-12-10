package com.controller;

import com.domain.dto.CountyStatisticsDTO;
import com.service.StatisticsService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    public List<CountyStatisticsDTO> getCountyStatistics() {
        return statisticsService.getCountyStatistics();
    }
}
