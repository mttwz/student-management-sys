package com.radnoti.studentmanagementsystem.exception.workgroup;

public class UserNotAddedToWorkgroupException extends RuntimeException{
    public UserNotAddedToWorkgroupException() {
        super("User does not added to the workgroup");
    }
}
