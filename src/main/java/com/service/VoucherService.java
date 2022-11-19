package com.service;

import com.domain.entity.RecycleRequestEntity;
import com.domain.entity.VoucherEntity;
import com.repo.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public List<VoucherEntity> getAllVouchers() {
        List<VoucherEntity> entities = voucherRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no vouchers!");
        }

        return entities;
    }

    public List<VoucherEntity> getVouchersByClientId(Integer clientId) {
        List<VoucherEntity> entities = voucherRepository.findAllByClientId(clientId);

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no vouchers for this client!");
        }

        return entities;
    }
}
