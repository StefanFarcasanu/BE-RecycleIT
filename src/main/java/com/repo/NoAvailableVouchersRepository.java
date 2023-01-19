package com.repo;

import com.domain.entity.NoAvailableVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoAvailableVouchersRepository extends JpaRepository<NoAvailableVoucher, Integer> {

    Optional<NoAvailableVoucher> findByUserIdAndValue(Integer userId, Double value);
}
