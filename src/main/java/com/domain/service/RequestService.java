package com.domain.service;

import com.domain.dto.RequestDto;
import com.domain.entity.RequestEntity;
import com.domain.enums.StatusEnum;
import com.repo.RequestRepository;
import com.utils.RequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public List<RequestDto> getAllRequests() {
        List<RequestEntity> entities = requestRepository.findAll();
        return entities.stream().map(RequestMapper::entityToDto).collect(Collectors.toList());
    }

    @Transactional()
    public RequestDto updateRequest(Integer requestId, RequestDto body) {
        if (body.getStatus() == null) {
            throw new IllegalArgumentException("Request status not provided!");
        }
        if (!body.getStatus().equals("CONFIRMED") && !body.getStatus().equals("DECLINED")) {
            throw new IllegalArgumentException("Illegal request status provided! Should be CONFIRMED or DECLINED.");
        }

        Optional<RequestEntity> found = requestRepository.findById(requestId);
        if (found.isEmpty()) {
            throw new NoSuchElementException("Request not found!");
        }

        RequestEntity updated = found.get();
        if (body.getStatus().equals("CONFIRMED")) {
            updated.setStatus(StatusEnum.CONFIRMED);
        } else {
            updated.setStatus(StatusEnum.DECLINED);
        }

        requestRepository.save(updated);
        return RequestMapper.entityToDto(updated);
    }
}
