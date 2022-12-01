package com.controller;

import com.domain.dto.RecycleRequestDto;
import com.domain.entity.RecycleRequestEntity;
import com.security.JWTAuthorizationFilter;
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

    @GetMapping("/company")
    @ResponseStatus(HttpStatus.OK)
    public List<RecycleRequestEntity> getRequestsByCompanyId(@RequestHeader("Authorization") String token) {
        return recycleRequestService.getRequestsByCompanyId(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecycleRequestEntity> getAllRequests() {
        return recycleRequestService.getAllRequests();
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

    @GetMapping("/milestone")
    @ResponseStatus(HttpStatus.OK)
    public Double getTheNextMilestoneForClientId(@RequestHeader("Authorization") String token) {
        return recycleRequestService.getNextMilestoneForClientId(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public List<RecycleRequestEntity> getRecyclingHistoryForClientId(@RequestHeader("Authorization") String token) {
        return recycleRequestService.getRecyclingHistoryForClientId(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
