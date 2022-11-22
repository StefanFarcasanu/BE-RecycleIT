package com.utils;

import com.domain.dto.UserDto;
import com.domain.entity.UserEntity;
import com.domain.enums.RoleEnum;

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

    public static UserEntity dtoToEntity(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .county(userDto.getCounty())
                .city(userDto.getCity())
                .role(userDto.getRole())
                .build();
    }
}
