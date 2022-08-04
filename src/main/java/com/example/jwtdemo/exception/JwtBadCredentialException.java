package com.example.jwtdemo.exception;

public class JwtBadCredentialException extends RuntimeException{
    public JwtBadCredentialException(String message){
        super(message);
    }
}
