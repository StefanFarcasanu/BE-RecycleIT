package com.service;

import com.domain.dto.RecycleRequestDto;
import com.domain.dto.UserDto;
import com.domain.entity.RecycleRequestEntity;
import com.domain.enums.RoleEnum;
import com.domain.enums.StatusEnum;
import com.domain.enums.TypeEnum;
import com.repo.RequestRepository;
import com.repo.UserRepository;
import com.utils.RequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.utils.RequestMapper.dtoToEntity;
import static com.utils.RequestMapper.entityToDto;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    public List<RecycleRequestDto> getAllRequests() {
        List<RecycleRequestEntity> entities = requestRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests!");
        }

        return entities.stream().map(RequestMapper::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public RecycleRequestDto updateRequest(Integer requestId, RecycleRequestDto body) {
        if (body.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status not provided!");
        }

        if (!StatusEnum.CONFIRMED.equals(body.getStatus()) && !StatusEnum.DECLINED.equals(body.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request status provided! Should be CONFIRMED or DECLINED.");
        }

        Optional<RecycleRequestEntity> found = requestRepository.findById(requestId);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found!");
        }

        RecycleRequestEntity updated = found.get();
        if (StatusEnum.CONFIRMED.equals(body.getStatus())) {
            updated.setStatus(StatusEnum.CONFIRMED);
        } else {
            updated.setStatus(StatusEnum.DECLINED);
        }

        requestRepository.save(updated);
        emailService.sendSimpleMail(updated);

        return RequestMapper.entityToDto(updated);
    }

    public RecycleRequestDto getRequestById(Integer requestId) {
        Optional<RecycleRequestEntity> found = requestRepository.findById(requestId);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found!");
        }

        return RequestMapper.entityToDto(found.get());
    }

    public List<RecycleRequestDto> getRequestsByCompanyId(Integer companyId) {
        List<RecycleRequestEntity> entities = requestRepository.findAllByCompanyId(companyId);

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests for this company!");
        }

        return entities.stream().map(RequestMapper::entityToDto).collect(Collectors.toList());
    }

    public RecycleRequestDto add(RecycleRequestDto body) {
        var client = userRepository.findById(body.getClientId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client do not exists!"));
        var company = userRepository.findById(body.getCompanyId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company do not exists!"));

        if (body.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request quantity provided! Can not be 0");
        }

        if (!TypeEnum.METAL.equals(body.getType()) && !TypeEnum.ELECTRONICS.equals(body.getType()) && !TypeEnum.PAPER.equals(body.getType()) && !TypeEnum.PLASTIC.equals(body.getType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request type provided! Should be one of: METAL, ELECTRONICS, PAPER or PLASTIC.");
        }

        var recycling = dtoToEntity(body);
        recycling.setClient(client);
        recycling.setCompany(company);

        body.setStatus(StatusEnum.PENDING);

        requestRepository.save(recycling);

        emailService.sendThanksMail(recycling);
        return entityToDto(recycling);
    }

}
