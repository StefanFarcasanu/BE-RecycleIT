package com.repo;

import com.domain.entity.RecycleRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecycleRequestRepository extends JpaRepository<RecycleRequestEntity, Integer> {

    @Query(value = "SELECT * FROM requests WHERE company_id = :companyId", nativeQuery = true)
    List<RecycleRequestEntity> findAllByCompanyId(Integer companyId);
}
