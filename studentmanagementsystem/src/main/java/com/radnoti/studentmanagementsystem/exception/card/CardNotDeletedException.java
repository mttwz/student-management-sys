package com.radnoti.studentmanagementsystem.exception.card;

public class CardNotDeletedException extends RuntimeException{
    public CardNotDeletedException() {
        super("Card is not deleted");
    }
}
