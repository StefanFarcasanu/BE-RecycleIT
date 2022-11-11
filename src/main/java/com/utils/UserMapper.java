package com.utils;

import com.domain.dto.UserDto;
import com.domain.entity.UserEntity;

public class UserMapper {

    public static UserDto entityToDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .county(entity.getCounty())
                .city(entity.getCity())
                .role(entity.getRole())
                .build();
    }
}
