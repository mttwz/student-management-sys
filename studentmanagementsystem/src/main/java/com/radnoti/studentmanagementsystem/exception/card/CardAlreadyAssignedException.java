package com.radnoti.studentmanagementsystem.exception.card;

public class CardAlreadyAssignedException extends RuntimeException{
    public CardAlreadyAssignedException() {
        super("Card is already assigned");
    }
}
