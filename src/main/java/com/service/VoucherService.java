package com.service;

import com.domain.dto.VoucherDto;
import com.domain.entity.NoAvailableVoucher;
import com.domain.entity.UserEntity;
import com.domain.entity.VoucherEntity;
import com.domain.enums.RoleEnum;
import com.domain.enums.VoucherStatusEnum;
import com.domain.validation.ValidationException;
import com.domain.validation.VoucherValidator;
import com.repo.NoAvailableVouchersRepository;
import com.repo.UserRepository;
import com.repo.VoucherRepository;
import com.utils.VoucherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final NoAvailableVouchersRepository noAvailableVouchersRepository;

    public List<VoucherEntity> getAllVouchers() {
        List<VoucherEntity> entities = voucherRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no vouchers!");
        }

        entities.forEach(x -> {
            x.getClient().setPassword("");
            x.getRetailer().setPassword("");
            x.setCode("");
        });

        return entities;
    }

    public List<VoucherEntity> getVouchersByClientId(Integer clientId) {
        if (userRepository.findByIdAndRole(clientId, RoleEnum.CLIENT).isPresent()){
            Optional<List<VoucherEntity>> voucherEntities = voucherRepository.findAllByClientId(clientId);
            if (voucherEntities.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no vouchers for this client!");
            }

            List<VoucherEntity> voucherEntityList = voucherEntities.get();
            voucherEntityList.forEach(x -> {
                x.getClient().setPassword("");
                x.getRetailer().setPassword("");
                x.setCode("");
            });

            return voucherEntityList;
        } else if (userRepository.findByIdAndRole(clientId, RoleEnum.RETAILER).isPresent()){
            Optional<List<VoucherEntity>> voucherEntities = voucherRepository.findAllByRetailerId(clientId);
            if (voucherEntities.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no vouchers for this retailer!");
            }

            List<VoucherEntity> voucherEntityList = voucherEntities.get();
            voucherEntityList.forEach(x -> {
                if (x.getClient() != null) {
                    x.getClient().setPassword("");
                }
                x.getRetailer().setPassword("");
                x.setCode("");
            });

            return voucherEntityList;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no client or retailer with this id: " + clientId);
    }

    public VoucherDto addVoucher(VoucherDto voucherDto) {

        if (!userRepository.existsById(voucherDto.getRetailerId()))
            throw new ValidationException("Invalid retailer id!");

        UserEntity user = userRepository.getReferenceById(voucherDto.getRetailerId());
        UUID uuid = UUID.randomUUID();

        VoucherEntity voucher = VoucherEntity.builder()
                .retailer(user)
                .value(voucherDto.getValue())
                .details(voucherDto.getDetails())
                .code(uuid.toString())
                .status(VoucherStatusEnum.AVAILABLE)
                .build();
        VoucherValidator.validate(voucher);

        return VoucherMapper.entityToDto(this.voucherRepository.save(voucher));
    }

    public VoucherEntity redeemVoucherForClientId(Integer clientId, Double value) {

        Optional<VoucherEntity> voucherEntityOptional =
                voucherRepository.getFirstByValueEqualsAndStatusEqualsAndClientIsNull(value, VoucherStatusEnum.AVAILABLE);

        if (voucherEntityOptional.isEmpty()) {
            handleNoAvailableVouchers(clientId, value);
        }

        VoucherEntity voucherEntity = voucherEntityOptional.get();
        voucherEntity.setClient(UserEntity.builder().id(clientId).build());
        voucherEntity.setVaildUntil(LocalDateTime.now().plusMonths(1));

        voucherRepository.save(voucherEntity);

        return voucherEntity;
    }

    private void handleNoAvailableVouchers(Integer clientId, Double value) {
        NoAvailableVoucher noAvailableVoucher = NoAvailableVoucher.builder()
                .userId(clientId)
                .value(value)
                .build();

        noAvailableVouchersRepository.save(noAvailableVoucher);

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Transactional
    @Async
    @Scheduled(fixedRate = 3600000) // 1 hour - 3600000
    public void scheduleFixedRateTask() {
        System.out.println("---------------------ScheduleFixedRateTask----------------------");
        List<NoAvailableVoucher> noAvailableVouchers = noAvailableVouchersRepository.findAll();

        noAvailableVouchers.forEach(x -> {
            noAvailableVouchersRepository.delete(x);
            try {
                redeemVoucherForClientId(x.getUserId(), x.getValue());
            } catch (ResponseStatusException ignored) {

            }
        });
    }

    public void deleteVoucher(Integer voucherId) {
        try {
            voucherRepository.deleteById(voucherId);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no voucher with the provided ID!");
        }
    }

    public Integer getTotalNumberOfVouchers() {
        return voucherRepository.getTotalNumberOfAssignedVouchers().orElse(0);
    }

    public VoucherEntity useVoucher(Integer voucherId, Integer clientId)
    {
        Optional<VoucherEntity> existingVoucher = voucherRepository.findById(voucherId);
        if(existingVoucher.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found!");
        }

        VoucherEntity newVoucher = existingVoucher.get();

        if(!newVoucher.getClient().getId().equals(clientId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid voucher id!");
        }

        if(newVoucher.getStatus().equals(VoucherStatusEnum.AVAILABLE)) {
            newVoucher.setStatus(VoucherStatusEnum.USED);
        }
        else if(newVoucher.getStatus().equals(VoucherStatusEnum.EXPIRED))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The voucher has expired!");
        }
        else if(newVoucher.getStatus().equals(VoucherStatusEnum.USED))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The voucher has already been used!");
        }

        LocalDateTime localDateTime = java.time.LocalDateTime.now();
        localDateTime = localDateTime.plusDays(30);
        newVoucher.setVaildUntil(localDateTime);

        voucherRepository.save(newVoucher);
        emailService.sendUsedVoucherMail(newVoucher);

        return newVoucher;
    }
}
