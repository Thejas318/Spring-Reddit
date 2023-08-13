package com.personalProject.reddit.exceptionHandler;

public class SubRedditNotFoundException extends RuntimeException{
    public SubRedditNotFoundException(String subredditName)  {
        super(subredditName);
    }
}
