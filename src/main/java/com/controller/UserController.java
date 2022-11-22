package com.controller;

import com.domain.dto.UserDto;
import com.domain.entity.UserEntity;
import com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String registerUser(@RequestBody UserEntity user) {
        this.userService.addUser(user);
        return "Account created!\n";
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity updateUserAccount(@PathVariable Integer userId, @RequestBody UserDto user) {
        return this.userService.updateUserAccount(userId, user);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity getUserById(@PathVariable Integer userId) {
        return this.userService.getUserById(userId);
    }
}
