package com.utils;

import com.domain.dto.RecycleRequestDto;
import com.domain.entity.RecycleRequestEntity;
import com.domain.enums.StatusEnum;

public class RequestMapper {

    public static RecycleRequestDto entityToDto(RecycleRequestEntity entity) {
        return RecycleRequestDto.builder()
                .id(entity.getId())
                .clientId(entity.getClient().getId())
                .companyId(entity.getCompany().getId())
                .quantity(entity.getQuantity())
                .type(entity.getType())
                .status(entity.getStatus())
                .build();
    }

    public static RecycleRequestEntity dtoToEntity(RecycleRequestDto dto) {
        return RecycleRequestEntity.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .type(dto.getType())
                .status(StatusEnum.PENDING)
                .build();
    }
}
