package com.controller;

import com.domain.dto.RecycleRequestDto;
import com.domain.dto.UserDto;
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
    public List<RecycleRequestDto> getRequests(@RequestParam(name = "companyId", required = false) Integer companyId) {
        if (companyId == null) {
            return requestService.getAllRequests();
        } else {
            return requestService.getRequestsByCompanyId(companyId);
        }
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RecycleRequestDto getRequestById(@PathVariable Integer requestId) {
        return requestService.getRequestById(requestId);
    }

    @PutMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RecycleRequestDto updateRequest(@PathVariable Integer requestId, @RequestBody RecycleRequestDto body) {
        return requestService.updateRequest(requestId, body);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public RecycleRequestDto addRecycleRequest(@RequestBody RecycleRequestDto body) {
        return requestService.add(body);
    }
}
