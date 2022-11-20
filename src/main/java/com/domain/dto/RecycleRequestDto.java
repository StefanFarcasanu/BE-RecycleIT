package com.domain.dto;

import com.domain.enums.StatusEnum;
import com.domain.enums.TypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RecycleRequestDto {

    private Integer id;

    private Integer clientId;

    private Integer companyId;

    private Double quantity;

    private TypeEnum type;

    private StatusEnum status;

    private LocalDateTime date;
}
