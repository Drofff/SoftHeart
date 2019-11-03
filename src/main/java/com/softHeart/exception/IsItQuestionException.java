package com.softHeart.exception;

public class IsItQuestionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Well.. Is it a question?";

    public IsItQuestionException() {
        super(DEFAULT_MESSAGE);
    }

    public IsItQuestionException(String message) {
        super(message);
    }

}
