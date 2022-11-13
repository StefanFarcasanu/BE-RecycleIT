package com.domain.validation;

import com.domain.entity.UserEntity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    public static void validate(UserEntity entity) {
        String err = "";
        if(Objects.equals(entity.getFirstname(), ""))
            err += "Invalid first name!\n";
        if(Objects.equals(entity.getLastname(), ""))
            err += "Invalid last name!\n";
        if(entity.getPassword().length() < 8)
            err += "Invalid password!\n";
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(entity.getEmail());
        boolean matchFound = matcher.find();
        if(!matchFound)
            err += "Invalid e-mail!\n";
        if(Objects.equals(entity.getCounty(), ""))
            err += "Invalid country!\n";
        if(Objects.equals(entity.getCity(), ""))
            err += "Invalid city!\n";
        if(err.length() != 0)
            throw  new ValidationException(err);
    }

}
