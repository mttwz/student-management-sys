package com.radnoti.studentmanagementsystem.exception.card;

public class CardNotCreatedException extends RuntimeException{
    public CardNotCreatedException() {
        super("Card does not created");
    }
}
