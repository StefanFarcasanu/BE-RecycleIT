package com.repo;

import com.domain.entity.UserEntity;
import com.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserEntityByEmail(String email);

    List<UserEntity> findAllByCountyEqualsAndRoleEquals(String county, RoleEnum roleEnum);

    Optional<UserEntity> findByIdAndRole(Integer integer, RoleEnum roleEnum);

    List<UserEntity> findAllByRole(RoleEnum roleEnum);

    void deleteById(Integer userId);

    @Query(value = "SELECT COUNT(*) FROM users WHERE role = 'CLIENT'", nativeQuery = true)
    Integer getTotalNumberOfClients();
}
