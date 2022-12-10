package com.domain.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

public class CountyDTO {
    @JsonSetter("auto")
    private String countyAbbreviation;
    @JsonSetter("nume")
    private String countyName;

    public CountyDTO() {}

    public CountyDTO(String countyAbbreviation, String countyName) {
        this.countyAbbreviation = countyAbbreviation;
        this.countyName = countyName;
    }

    public String getCountyAbbreviation() {
        return countyAbbreviation;
    }

    public void setCountyAbbreviation(String countyAbbreviation) {
        this.countyAbbreviation = countyAbbreviation;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
