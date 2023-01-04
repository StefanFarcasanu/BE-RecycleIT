package com.repo;

import com.domain.entity.VoucherEntity;
import com.domain.enums.VoucherStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {

    @Query(value = "SELECT * FROM vouchers WHERE client_id = :clientId", nativeQuery = true)
    Optional<List<VoucherEntity>> findAllByClientId(Integer clientId);

    @Query(value = "SELECT COUNT(*) FROM vouchers WHERE client_id = :clientId", nativeQuery = true)
    Optional<Integer> countAllByClient(Integer clientId);

    Optional<List<VoucherEntity>> findAllByRetailerId(Integer retailerId);

    Optional<VoucherEntity> getFirstByValueEqualsAndStatusEqualsAndClientIsNull(Double value, VoucherStatusEnum status);

    void deleteById(Integer voucherId);

    @Query(value = "SELECT COUNT(*) FROM vouchers WHERE client_id IS NOT NULL", nativeQuery = true)
    Optional<Integer> getTotalNumberOfAssignedVouchers();
}
