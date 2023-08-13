package com.personalProject.reddit.exceptionHandler;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String ex) {
        super(ex);
    }
}
