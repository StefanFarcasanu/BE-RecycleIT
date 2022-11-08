package com.repo;

import com.domain.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {

    @Query(value = "SELECT * FROM requests WHERE company_id = :companyId", nativeQuery = true)
    List<RequestEntity> findAllByCompanyId(Integer companyId);
}
