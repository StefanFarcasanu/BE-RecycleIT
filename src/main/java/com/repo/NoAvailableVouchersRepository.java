package com.repo;

import com.domain.entity.NoAvailableVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoAvailableVouchersRepository extends JpaRepository<NoAvailableVoucher, Integer> {
}
