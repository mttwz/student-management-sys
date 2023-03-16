package com.radnoti.studentmanagementsystem.exception.card;

public class CardNotExistException extends RuntimeException{
    public CardNotExistException() {
        super("Card does not exist");
    }
}
