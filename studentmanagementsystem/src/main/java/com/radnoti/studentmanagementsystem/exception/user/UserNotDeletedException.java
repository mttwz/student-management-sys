package com.radnoti.studentmanagementsystem.exception.user;

public class UserNotDeletedException extends RuntimeException{
    public UserNotDeletedException() {
        super("User is not deleted");
    }
}
