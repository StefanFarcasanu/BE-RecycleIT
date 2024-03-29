package com.controller;

import com.domain.dto.VoucherDto;
import com.domain.entity.VoucherEntity;
import com.security.JWTAuthorizationFilter;
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

    @GetMapping("/client")
    @ResponseStatus(HttpStatus.OK)
    public List<VoucherEntity> getVouchersByClientId(@RequestHeader("Authorization") String token) {
        return voucherService.getVouchersByClientId(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VoucherEntity> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VoucherEntity redeemVoucherForClient(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "quantity") Double quantity
    ) {
        return voucherService.redeemVoucherForClientId(
                JWTAuthorizationFilter.getUserIdFromJwt(token),
                quantity * 10
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<VoucherDto> createVoucher(@RequestBody VoucherDto voucherDto,
                                             @RequestParam(name = "number") Integer number) {
        return this.voucherService.addVoucher(voucherDto, number);
    }

    // Should only be used for testing
    @DeleteMapping("/{voucherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVoucher(@PathVariable Integer voucherId) {
        voucherService.deleteVoucher(voucherId);
    }

    @GetMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    public Integer getTotalNumberOfVouchers() {
        return voucherService.getTotalNumberOfVouchers();
    }

    @PutMapping("/{voucherId}")
    @ResponseStatus(HttpStatus.OK)
    public VoucherEntity useVoucher(@PathVariable Integer voucherId, @RequestHeader("Authorization") String token) {
        return this.voucherService.useVoucher(voucherId, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
