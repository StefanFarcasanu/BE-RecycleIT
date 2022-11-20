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
    public List<VoucherEntity> getVouchers(@RequestParam(name = "clientId", required = false) Integer clientId) {
        if (clientId == null) {
            return voucherService.getAllVouchers();
        } else {
            return voucherService.getVouchersByClientId(clientId);
        }
    }
}
