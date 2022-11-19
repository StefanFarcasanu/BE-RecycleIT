package com.repo;

import com.domain.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {

    @Query(value = "SELECT * FROM vouchers WHERE client_id = :clientId", nativeQuery = true)
    List<VoucherEntity> findAllByClientId(Integer clientId);
}
