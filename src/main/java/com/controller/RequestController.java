package com.controller;

import com.domain.dto.RequestDto;
import com.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getRequests(@RequestParam(name = "companyId", required = false) Integer companyId) {
        if (companyId == null) {
            return requestService.getAllRequests();
        } else {
            return requestService.getRequestsByCompanyId(companyId);
        }
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto getRequestById(@PathVariable Integer requestId) {
        return requestService.getRequestById(requestId);
    }

    @PutMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto updateRequest(@PathVariable Integer requestId, @RequestBody RequestDto body) {
        return requestService.updateRequest(requestId, body);
    }
}
