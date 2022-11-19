package com.service;

import com.domain.dto.RecycleRequestDto;
import com.domain.entity.RecycleRequestEntity;
import com.domain.enums.StatusEnum;
import com.domain.enums.TypeEnum;
import com.repo.RecycleRequestRepository;
import com.repo.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public List<RecycleRequestEntity> getAllRequests() {
        List<RecycleRequestEntity> entities = recycleRequestRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests!");
        }

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

        return found.get();
    }

    public List<RecycleRequestEntity> getRequestsByCompanyId(Integer companyId) {
        List<RecycleRequestEntity> entities = recycleRequestRepository.findAllByCompanyId(companyId);

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests for this company!");
        }

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

        return recycling;
    }
}
