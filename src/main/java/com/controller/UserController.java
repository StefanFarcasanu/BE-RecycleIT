package com.controller;

import com.domain.dto.UserDto;
import com.domain.entity.UserEntity;
import com.security.JWTAuthorizationFilter;
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

    @GetMapping("/companies")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getCompaniesFromClientCounty(@RequestHeader("Authorization") String token) {
        return userService.getCompaniesFromUserCounty(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String registerUser(@RequestBody UserEntity user) {
        this.userService.addUser(user);
        return "Account created!\n";
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserEntity updateClientAccount(@RequestHeader("Authorization") String token, @RequestBody UserDto user) {
        return this.userService.updateClientAccount(JWTAuthorizationFilter.getUserIdFromJwt(token), user);
    }

    @GetMapping("/client")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity getClientById(@RequestHeader("Authorization") String token) {
        return this.userService.getClientById(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
