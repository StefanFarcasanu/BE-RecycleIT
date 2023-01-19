package com.domain.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountyDTO {

    @JsonSetter("auto")
    private String countyAbbreviation;
    @JsonSetter("nume")
    private String countyName;
}
