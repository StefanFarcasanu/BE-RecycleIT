package com.controller;

import com.domain.entity.VoucherEntity;
import com.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vouchers")
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VoucherEntity> getVouchers(@RequestParam(name = "id", required = false) Integer id) {
        if (id == null) {
            return voucherService.getAllVouchers();
        } else {
            return voucherService.getVouchersById(id);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VoucherEntity redeemVoucherForClient(
            @RequestParam(name = "clientId", required = true) Integer clientId,
            @RequestParam(name = "quantity", required = true) Double quantity
    ) {
        return voucherService.redeemVoucherForClientId(clientId, quantity * 10);
    }
}
