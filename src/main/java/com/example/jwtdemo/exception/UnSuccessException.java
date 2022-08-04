package com.example.jwtdemo.exception;

public class UnSuccessException extends RuntimeException{
    public UnSuccessException(String message){
        super(message);
    }
}
