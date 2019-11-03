package com.softHeart.exception;

public class AnswerSaveException extends RuntimeException {

    public AnswerSaveException() {
        super();
    }

    public AnswerSaveException(String message) {
        super(message);
    }

    public AnswerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnswerSaveException(Throwable cause) {
        super(cause);
    }

    protected AnswerSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
