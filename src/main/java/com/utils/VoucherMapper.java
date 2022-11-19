package com.utils;

import com.domain.dto.VoucherDto;
import com.domain.entity.VoucherEntity;

public class VoucherMapper {

    public static VoucherDto entityToDto(VoucherEntity entity) {
        return VoucherDto.builder()
                .id(entity.getId())
                .clientId(entity.getClient().getId())
                .retailerId(entity.getRetailer().getId())
                .value(entity.getValue())
                .details(entity.getDetails())
                .code(entity.getCode())
                .status(entity.getStatus())
                .validUntil(entity.getVaildUntil())
                .build();
    }
}
