package com.controller;

import com.domain.dto.VoucherDto;
import com.domain.entity.UserEntity;
import com.domain.entity.VoucherEntity;
import com.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VoucherEntity redeemVoucherForClient(
            @RequestParam(name = "clientId", required = true) Integer clientId,
            @RequestParam(name = "quantity", required = true) Double quantity
    ) {
        return voucherService.redeemVoucherForClientId(clientId, quantity * 10);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createVoucher(@RequestBody VoucherDto voucherDto){
        this.voucherService.addVoucher(voucherDto);
        return new ResponseEntity<>("Voucher created!\n", HttpStatus.OK);
    }
}
