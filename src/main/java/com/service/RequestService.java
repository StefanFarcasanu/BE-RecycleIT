package com.service;

import com.domain.dto.RequestDto;
import com.domain.entity.RequestEntity;
import com.domain.enums.StatusEnum;
import com.repo.RequestRepository;
import com.utils.RequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    private final EmailService emailService;

    public List<RequestDto> getAllRequests() {
        List<RequestEntity> entities = requestRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests!");
        }

        return entities.stream().map(RequestMapper::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public RequestDto updateRequest(Integer requestId, RequestDto body) {
        if (body.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status not provided!");
        }

        if (!StatusEnum.CONFIRMED.equals(body.getStatus()) && !StatusEnum.DECLINED.equals(body.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request status provided! Should be CONFIRMED or DECLINED.");
        }

        Optional<RequestEntity> found = requestRepository.findById(requestId);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found!");
        }

        RequestEntity updated = found.get();
        if (StatusEnum.CONFIRMED.equals(body.getStatus())) {
            updated.setStatus(StatusEnum.CONFIRMED);
        } else {
            updated.setStatus(StatusEnum.DECLINED);
        }

        requestRepository.save(updated);
        emailService.sendSimpleMail(updated);

        return RequestMapper.entityToDto(updated);
    }

    public RequestDto getRequestById(Integer requestId) {
        Optional<RequestEntity> found = requestRepository.findById(requestId);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found!");
        }

        return RequestMapper.entityToDto(found.get());
    }

    public List<RequestDto> getRequestsByCompanyId(Integer companyId) {
        List<RequestEntity> entities = requestRepository.findAllByCompanyId(companyId);

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no requests for this company!");
        }

        return entities.stream().map(RequestMapper::entityToDto).collect(Collectors.toList());
    }
}
