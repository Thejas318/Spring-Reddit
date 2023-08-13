package com.personalProject.reddit.exceptionHandler;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String exMessage) {
        super(exMessage);
    }

    public SpringRedditException(String exMessage, Throwable ex) {
        super(exMessage, ex);
    }
}
