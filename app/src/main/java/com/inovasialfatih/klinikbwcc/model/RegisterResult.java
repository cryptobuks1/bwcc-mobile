package com.inovasialfatih.klinikbwcc.model;

public class RegisterResult {
    private String status;
    private String message;
    private RegisterData data;

    public RegisterResult() {
    }

    public boolean isStatus() {
        return status.equals("Success");
    }

    public String getMessage() {
        return message;
    }

    public RegisterData getData() {
        return data;
    }
}
