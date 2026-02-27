package com.challenge.leban.exception;

public abstract class CoreException extends RuntimeException {

    protected CoreException() {
        super();
    }

    protected CoreException(String message) {
        super(message);
    }

    public abstract Integer getStatusCode();

    public abstract String getCodeMessage();

}
