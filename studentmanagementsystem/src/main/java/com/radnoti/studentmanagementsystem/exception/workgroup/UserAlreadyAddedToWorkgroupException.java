package com.radnoti.studentmanagementsystem.exception.workgroup;

public class UserAlreadyAddedToWorkgroupException extends RuntimeException{
    public UserAlreadyAddedToWorkgroupException() {
        super("User already added to the workgroup");
    }
}
