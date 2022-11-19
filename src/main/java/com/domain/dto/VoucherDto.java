package com.domain.dto;

import com.domain.enums.VoucherStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VoucherDto {

    private Integer id;

    private Integer clientId;

    private Integer retailerId;

    private Double value;

    private String details;

    private String code;

    private VoucherStatusEnum status;

    private LocalDateTime validUntil;
}
