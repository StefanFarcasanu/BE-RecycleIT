package com.repo;

import com.domain.entity.RecycleRequestEntity;
import com.domain.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecycleRequestRepository extends JpaRepository<RecycleRequestEntity, Integer> {

    @Query(value = "SELECT * FROM requests WHERE company_id = :companyId", nativeQuery = true)
    List<RecycleRequestEntity> findAllByCompanyId(Integer companyId);

    @Query(
            value = "SELECT SUM(quantity) FROM requests WHERE client_id = :clientId AND status = 'CONFIRMED'",
            nativeQuery = true
    )
    Optional<Double> getTotalRecycledQuantityByClient(Integer clientId);

    @Query(value = "SELECT * FROM requests WHERE client_id = :clientId ORDER BY date DESC", nativeQuery = true)
    List<RecycleRequestEntity> getRecyclingHistoryForClientId(Integer clientId);

    List<RecycleRequestEntity> findByStatus(StatusEnum statusEnum);

    void deleteById(Integer requestId);
}
