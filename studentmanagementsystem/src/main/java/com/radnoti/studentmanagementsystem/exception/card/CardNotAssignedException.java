package com.radnoti.studentmanagementsystem.exception.card;

public class CardNotAssignedException extends RuntimeException{
    public CardNotAssignedException() {
        super("Card does not assigned");
    }
}
