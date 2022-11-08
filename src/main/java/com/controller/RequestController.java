package com.controller;

import com.domain.dto.RequestDto;
import com.domain.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getRequests(@RequestParam(name = "companyId", required = false) Integer companyId) {
        if (companyId == null) {
            return requestService.getAllRequests();
        } else {
            return requestService.getRequestsByCompanyId(companyId);
        }
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@PathVariable Integer requestId) {
        return requestService.getRequestById(requestId);
    }

    @PutMapping("/{requestId}")
    public RequestDto updateRequest(@PathVariable Integer requestId, @RequestBody RequestDto body) {
        return requestService.updateRequest(requestId, body);
    }
}
