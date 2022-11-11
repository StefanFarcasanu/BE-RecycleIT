package com.domain.enums;

public enum TypeEnum {
    METAL, PLASTIC, ELECTRONICS, PAPER;

    public static TypeEnum findByValue(String value) {
        TypeEnum result = null;
        for (TypeEnum typeEnum : values()) {
            if (typeEnum.name().equalsIgnoreCase(value)) {
                result = typeEnum;
                break;
            }
        }
        return result;
    }
}
