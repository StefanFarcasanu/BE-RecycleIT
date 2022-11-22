package com.domain.validation;

import com.domain.entity.UserEntity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    public static void validate(UserEntity entity) {
        String err = "";

        if (Objects.isNull(entity.getFirstname()) || Objects.equals(entity.getFirstname(), "")) {
            err += "Invalid first name!\n";
        }

        if (Objects.isNull(entity.getLastname()) || Objects.equals(entity.getLastname(), "")) {
            err += "Invalid last name!\n";
        }

        if (Objects.isNull(entity.getPassword()) || entity.getPassword().length() < 8) {
            err += "Invalid password!\n";
        }

        if (Objects.isNull(entity.getEmail())) {
            err += "Invalid email!\n";
        } else {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(entity.getEmail());
            boolean matchFound = matcher.find();
            if (!matchFound)
                err += "Invalid e-mail!\n";
        }

        if (Objects.isNull(entity.getCounty()) || Objects.equals(entity.getCounty(), "")) {
            err += "Invalid country!\n";
        }

        if (Objects.isNull(entity.getCity()) || Objects.equals(entity.getCity(), "")) {
            err += "Invalid city!\n";
        }

        if (err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
