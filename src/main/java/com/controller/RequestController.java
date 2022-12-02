package com.controller;

import com.domain.dto.RecycleRequestDto;
import com.domain.entity.RecycleRequestEntity;
import com.service.RecycleRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final RecycleRequestService recycleRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecycleRequestEntity> getRequests(@RequestParam(name = "companyId", required = false) Integer companyId) {
        if (companyId == null) {
            return recycleRequestService.getAllRequests();
        } else {
            return recycleRequestService.getRequestsByCompanyId(companyId);
        }
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RecycleRequestEntity getRequestById(@PathVariable Integer requestId) {
        return recycleRequestService.getRequestById(requestId);
    }

    @PutMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RecycleRequestEntity updateRequest(@PathVariable Integer requestId, @RequestBody RecycleRequestDto body) {
        return recycleRequestService.updateRequest(requestId, body);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecycleRequestEntity addRecycleRequest(@RequestBody RecycleRequestDto body) {
        return recycleRequestService.addRequest(body);
    }

    @GetMapping("/milestone/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public Double getTheNextMilestoneForClientId(@PathVariable Integer clientId) {
        return recycleRequestService.getNextMilestoneForClientId(clientId);
    }

    @GetMapping("/history/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RecycleRequestEntity> getRecyclingHistoryForClientId(@PathVariable Integer clientId) {
        return recycleRequestService.getRecyclingHistoryForClientId(clientId);
    }

    // Should only be used for testing
    @DeleteMapping("/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequest(@PathVariable Integer requestId) {
        recycleRequestService.deleteRequest(requestId);
    }
}
