package com.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDto {

    private Integer id;

    private Integer clientId;

    private Integer companyId;

    private Integer quantity;

    private String type;

    private String status;
}
