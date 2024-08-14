package com.example.jobhunter.exception;

import org.springframework.web.bind.annotation.PutMapping;

public class IdInvalidException extends Exception{
    public IdInvalidException(String message){
        super(message);
    }
}
