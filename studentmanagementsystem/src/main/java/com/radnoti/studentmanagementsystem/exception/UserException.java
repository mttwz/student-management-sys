package com.radnoti.studentmanagementsystem.exception;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public enum UserException {
    NOT_FOUND(1,Types.NOT_FOUND),
    EXIST(2,Types.EXIST),
    NOT_ACTIVATED(3,Types.NOT_ACTIVATED),
    ACTIVATED(4, Types.ACTIVATED),
    DELETED(5, Types.DELETED),

    ;


    public class Types {
        public static final String NOT_FOUND = "The user not found";
        public static final String EXIST = "The user already exist";
        public static final String NOT_ACTIVATED = "The user is not activated yet";
        public static final String ACTIVATED = "The user already activated";
        public static final String DELETED = "The user is deleted";
    }
    private final Integer id;
    private final String message;

    private UserException(Integer id, String label) {
        this.id = id;
        this.message = label;
    }

    public String getException(){
        return Map.of(this.id, this.message).toString();
    }
}
