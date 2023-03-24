package com.radnoti.studentmanagementsystem.exception.card;

public class AnotherCardAlreadyAssignedException extends RuntimeException{
    public AnotherCardAlreadyAssignedException() {
        super("Another Card is already assigned to the student");
    }
}
