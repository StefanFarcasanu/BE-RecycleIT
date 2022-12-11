package com.domain.dto;

public class CountyStatisticsDTO {
    private String countyAbbreviation;
    private String countyName;
    private Double quantity;
    private Long noVouchers;
    private Long noClients;

    public CountyStatisticsDTO() {}

    public CountyStatisticsDTO(CountyDTO countyDTO) {
        this.countyAbbreviation = countyDTO.getCountyAbbreviation();
        this.countyName = countyDTO.getCountyName();
    }

    public CountyStatisticsDTO(String countyAbbreviation, String countyName, Double quantity, Long noVouchers, Long noClients) {
        this.countyAbbreviation = countyAbbreviation;
        this.countyName = countyName;
        this.quantity = quantity;
        this.noVouchers = noVouchers;
        this.noClients = noClients;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Long getNoVouchers() {
        return noVouchers;
    }

    public void setNoVouchers(Long noVouchers) {
        this.noVouchers = noVouchers;
    }

    public Long getNoClients() {
        return noClients;
    }

    public void setNoClients(Long noClients) {
        this.noClients = noClients;
    }
}
