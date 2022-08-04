package com.example.jwtdemo.exception;

import com.example.jwtdemo.dto.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(JwtBadCredentialException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseObject<Object> JwtBadCredentialHandler(JwtBadCredentialException ex) {
        return new ResponseObject<>(false, com.example.jwtdemo.dto.response.ResponseStatus.BAD_CREDENTIAL, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(JwtDisabledException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseObject<Object> JwtDisabledHandler(JwtDisabledException ex) {
        return new ResponseObject<>(false, com.example.jwtdemo.dto.response.ResponseStatus.BAD_REQUEST, ex.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(UnSuccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseObject<Object> UnSuccessHandler(UnSuccessException ex) {
        return new ResponseObject<>(false, com.example.jwtdemo.dto.response.ResponseStatus.UNSUCCESS, ex.getMessage());
    }
}
