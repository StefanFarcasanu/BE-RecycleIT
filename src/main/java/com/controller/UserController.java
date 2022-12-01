package com.controller;

import com.domain.dto.UserDto;
import com.domain.entity.UserEntity;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String registerUser(@RequestBody UserEntity user) {
        this.userService.addUser(user);
        return "Account created!\n";
    }

    @PutMapping("/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity updateClientAccount(@PathVariable Integer clientId, @RequestBody UserDto user) {
        return this.userService.updateClientAccount(clientId, user);
    }

    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity getClientById(@PathVariable Integer clientId) {
        return this.userService.getClientById(clientId);
    }

    // Should only be used for testing
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }
}
