package com.repo;

import com.domain.entity.UserEntity;
import com.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findAllByCountyEqualsAndRoleEquals(String county, RoleEnum roleEnum);

    Optional<UserEntity> findByIdAndRole(Integer integer, RoleEnum roleEnum);
}