package com.service;

import com.domain.dto.UserDto;
import com.domain.entity.UserEntity;
import com.domain.enums.RoleEnum;
import com.domain.validation.UserValidator;
import com.domain.validation.ValidationException;
import com.repo.UserRepository;
import com.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getCompaniesFromUserCounty(Integer clientId) {
        var client = userRepository.findByIdAndRole(clientId, RoleEnum.CLIENT).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client do not exists!"));

        return userRepository.findAllByCountyEqualsAndRoleEquals(client.getCounty(), RoleEnum.COMPANY).stream().map(UserMapper::entityToDto).collect(Collectors.toList());
    }

    public List<UserDto> getAll() {
        List<UserEntity> entities = userRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no users!");
        }

        return entities.stream().map(UserMapper::entityToDto).collect(Collectors.toList());
    }

    public UserEntity addUser(UserEntity user)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(13);
        UserValidator.validate(user);
        if(!userRepository.findUserEntityByEmail(user.getEmail()).isEmpty())
            throw new ValidationException("Email address already used!");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(RoleEnum.CLIENT);
        return this.userRepository.save(user);
    }
}
