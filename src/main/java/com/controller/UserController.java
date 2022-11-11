package com.controller;

import com.domain.dto.UserDto;
import com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getCompaniesFromClientCounty(@RequestParam(name = "clientId", required = false) Integer clientId) {
        if (clientId == null) {
            return userService.getAll();
        } else {
            return userService.getCompaniesFromUserCounty(clientId);
        }
    }
}
