package com.radnoti.studentmanagementsystem.exception.user;

public class UserNotActivatedException extends RuntimeException{
    public UserNotActivatedException() {
        super("User is not activated yet");
    }
}
