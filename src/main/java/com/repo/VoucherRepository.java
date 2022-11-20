package com.repo;

import com.domain.entity.VoucherEntity;
import com.domain.enums.VoucherStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {

    @Query(value = "SELECT * FROM vouchers WHERE client_id = :clientId", nativeQuery = true)
    List<VoucherEntity> findAllByClientId(Integer clientId);

    Optional<VoucherEntity> getFirstByValueEqualsAndStatusEqualsAndClientIsNull(Double value, VoucherStatusEnum status);
}
