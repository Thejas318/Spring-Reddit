package com.personalProject.reddit.exceptionHandler;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String exMessage) {
        super(exMessage);
    }
}
