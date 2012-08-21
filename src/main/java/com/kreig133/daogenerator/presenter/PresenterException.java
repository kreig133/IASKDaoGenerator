package com.kreig133.daogenerator.presenter;

public class PresenterException extends RuntimeException {

    public static final String WARN = "WARN";
    public static final String ERROR = "ERROR";

    private String type;
    private String msg;

    private PresenterException(String msg, String type){
        this.msg = msg;
        this.type = type;
    }

    public static PresenterException warn(String msg){
        return new PresenterException(msg, WARN);
    }

    public static PresenterException error(String msg){
        return new PresenterException(msg, ERROR);
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
