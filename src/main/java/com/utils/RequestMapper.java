package com.utils;

import com.domain.dto.RequestDto;
import com.domain.entity.RequestEntity;

public class RequestMapper {

    public static RequestDto entityToDto(RequestEntity entity) {
        return RequestDto.builder()
                .id(entity.getId())
                .clientId(entity.getClient().getId())
                .companyId(entity.getCompany().getId())
                .quantity(entity.getQuantity())
                .type(entity.getType())
                .status(entity.getStatus())
                .build();
    }
}
