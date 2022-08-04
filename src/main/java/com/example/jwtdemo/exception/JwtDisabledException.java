package com.example.jwtdemo.exception;

public class JwtDisabledException extends RuntimeException{
   public JwtDisabledException(String message){
       super(message);
   }
}
