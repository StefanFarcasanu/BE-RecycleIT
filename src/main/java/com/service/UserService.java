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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.relation.Role;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(13);

    public List<UserDto> getCompaniesFromUserCounty(Integer clientId) {
        var client = userRepository.findByIdAndRole(clientId, RoleEnum.CLIENT).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client does not exist!"));

        return userRepository.findAllByCountyEqualsAndRoleEquals(client.getCounty(), RoleEnum.COMPANY).stream().map(UserMapper::entityToDto).collect(Collectors.toList());
    }

    public List<UserDto> getAll() {
        List<UserEntity> entities = userRepository.findAll();

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no users!");
        }

        return entities.stream().map(UserMapper::entityToDto).collect(Collectors.toList());
    }

    public void addUser(UserEntity user) {
        UserValidator.validate(user);

        if(userRepository.findUserEntityByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("Email address already used!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(RoleEnum.CLIENT);

        userRepository.save(user);
    }

    public UserEntity updateUserAccount(Integer clientId, UserDto userDto) {
        userDto.setId(clientId);
        userDto.setRole(RoleEnum.CLIENT);
        UserEntity userEntity = UserMapper.dtoToEntity(userDto);

        UserValidator.validate(userEntity);

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity updatedUser =  userRepository.save(userEntity);
        updatedUser.setPassword("");

        return updatedUser;
    }

    public UserEntity getUserById(Integer userId) {
        UserEntity user =  userRepository
                .findByIdAndRole(userId, RoleEnum.CLIENT)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No client found with the entered id!"
                ));

        user.setPassword("");
        return user;
    }
}
