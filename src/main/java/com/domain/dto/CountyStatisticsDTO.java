package com.domain.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountyStatisticsDTO {

    private String countyAbbreviation;
    private String countyName;
    private Double quantity;
    private Long noVouchers;
    private Long noClients;

    public CountyStatisticsDTO(CountyDTO countyDTO) {
        this.countyAbbreviation = countyDTO.getCountyAbbreviation();
        this.countyName = countyDTO.getCountyName();
    }
}
