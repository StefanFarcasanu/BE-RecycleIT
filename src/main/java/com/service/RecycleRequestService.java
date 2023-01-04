package com.service;

import com.domain.dto.RecycleRequestDto;
import com.domain.entity.RecycleRequestEntity;
import com.domain.enums.StatusEnum;
import com.domain.enums.TypeEnum;
import com.repo.RecycleRequestRepository;
import com.repo.UserRepository;
import com.repo.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.utils.RequestMapper.dtoToEntity;

@Service
@RequiredArgsConstructor
public class RecycleRequestService {

    private final RecycleRequestRepository recycleRequestRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final VoucherRepository voucherRepository;

    public List<RecycleRequestEntity> getAllRequests() {
        List<RecycleRequestEntity> entities = recycleRequestRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests!");
        }

        entities.forEach(x -> {
            x.getClient().setPassword("");
            x.getCompany().setPassword("");
        });

        return entities;
    }

    @Transactional
    public RecycleRequestEntity updateRequest(Integer requestId, RecycleRequestDto body) {
        if (body.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status not provided!");
        }

        if (!StatusEnum.CONFIRMED.equals(body.getStatus()) && !StatusEnum.DECLINED.equals(body.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request status provided! Should be CONFIRMED or DECLINED.");
        }

        Optional<RecycleRequestEntity> found = recycleRequestRepository.findById(requestId);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found!");
        }

        RecycleRequestEntity updated = found.get();
        if (StatusEnum.CONFIRMED.equals(body.getStatus())) {
            updated.setStatus(StatusEnum.CONFIRMED);
        } else {
            updated.setStatus(StatusEnum.DECLINED);
        }

        recycleRequestRepository.save(updated);
        emailService.sendConfirmationMail(updated);

        return updated;
    }

    public RecycleRequestEntity getRequestById(Integer requestId) {
        Optional<RecycleRequestEntity> found = recycleRequestRepository.findById(requestId);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found!");
        }

        RecycleRequestEntity entity = found.get();
        entity.getClient().setPassword("");
        entity.getCompany().setPassword("");

        return entity;
    }

    public List<RecycleRequestEntity> getRequestsByCompanyId(Integer companyId) {
        List<RecycleRequestEntity> entities = recycleRequestRepository.findAllByCompanyId(companyId);

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests for this company!");
        }

        entities.forEach(x -> {
            x.getClient().setPassword("");
            x.getCompany().setPassword("");
        });

        return entities;
    }

    public RecycleRequestEntity addRequest(RecycleRequestDto body) {
        var client = userRepository.findById(body.getClientId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client do not exists!"));
        var company = userRepository.findById(body.getCompanyId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company do not exists!"));

        if (body.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request quantity provided! Can not be lower than 0!");
        }

        if (!TypeEnum.METAL.equals(body.getType()) && !TypeEnum.ELECTRONICS.equals(body.getType()) && !TypeEnum.PAPER.equals(body.getType()) && !TypeEnum.PLASTIC.equals(body.getType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request type provided! Should be one of: METAL, ELECTRONICS, PAPER or PLASTIC.");
        }

        body.setId(null);   // this is in order to prevent the entity from being updated instead of being saved (if the ID is accidentally passed in the request body)
        body.setStatus(StatusEnum.PENDING);
        body.setDate(LocalDateTime.now());

        var recycling = dtoToEntity(body);
        recycling.setClient(client);
        recycling.setCompany(company);

        recycleRequestRepository.save(recycling);
        emailService.sendThanksMail(recycling);

        recycling.getClient().setPassword("");
        recycling.getCompany().setPassword("");

        return recycling;
    }

    public Double getNextMilestoneForClientId(Integer clientId) {
        Optional<Double> sum = recycleRequestRepository.getTotalRecycledQuantityByClient(clientId);

        if (sum.isEmpty()) {
            return 0.5;
        } else {
            Double total = sum.get();
            Integer numberOfVouchers = voucherRepository.countAllByClient(clientId).orElse(0);

            if (total > 20 && Integer.valueOf(12).equals(numberOfVouchers)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You have already redeemed all of your vouchers!");
            }

            if (total < 0.5) {
                return 0.5;
            } else if (total < 1) {
                return 1.0;
            } else {
                return (double) (total.intValue() + (total.intValue() % 2 == 0 ? 2 : 1));
            }
        }
    }

    public List<RecycleRequestEntity> getRecyclingHistoryForClientId(Integer clientId) {
        List<RecycleRequestEntity> entities = recycleRequestRepository.getRecyclingHistoryForClientId(clientId);

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This client has made no recycling requests!");
        }

        entities.forEach(x -> {
            x.getClient().setPassword("");
            x.getCompany().setPassword("");
        });

        return entities;
    }

    public void deleteRequest(Integer requestId) {
        try {
            recycleRequestRepository.deleteById(requestId);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no request with the provided ID!");
        }
    }

    public Double getTotalNumberOfKilograms() {
        return recycleRequestRepository.getTotalNumberOfKilograms().orElse(0D);
    }
}
