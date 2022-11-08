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
    public List<RequestDto> getRequests() {
        return requestService.getAllRequests();
    }

    @PutMapping("/{requestId}")
    public RequestDto updateRequest(@PathVariable Integer requestId, @RequestBody RequestDto body) {
        return requestService.updateRequest(requestId, body);
    }
}
