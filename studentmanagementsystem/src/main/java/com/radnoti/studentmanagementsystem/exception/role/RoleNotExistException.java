package com.radnoti.studentmanagementsystem.exception.role;

public class RoleNotExistException extends RuntimeException{
    public RoleNotExistException() {
        super("Role does not exist");
    }
}
