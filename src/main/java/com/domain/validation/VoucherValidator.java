package com.domain.validation;

import com.domain.entity.VoucherEntity;

import java.util.Objects;

public class VoucherValidator {
    public static void validate(VoucherEntity entity){
        String err = "";
        if(Objects.equals(entity.getDetails(), ""))
            err += "Invalid details!";
        if( !(entity.getValue() == 5 || entity.getValue() == 10 || (entity.getValue() % 20 == 0 && entity.getValue() <= 200) )  )
            err += "Invalid value!";
        if(err.length() != 0)
            throw new ValidationException(err);
    }

}
