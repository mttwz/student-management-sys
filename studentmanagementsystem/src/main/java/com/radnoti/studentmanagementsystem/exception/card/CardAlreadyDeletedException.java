package com.radnoti.studentmanagementsystem.exception.card;

public class CardAlreadyDeletedException extends RuntimeException{
    public CardAlreadyDeletedException() {
        super("Card is already deleted");
    }
}
