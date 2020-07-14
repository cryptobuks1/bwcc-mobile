package com.inovasialfatih.klinikbwcc.model;

public class LoginResult {
    private String status;
    private String message;
    private LoginData data;

    public LoginResult() {
    }

    public boolean isStatus() {
        return status.equals("Success");
    }

    public String getMessage() {
        return message;
    }

    public LoginData getData() {
        return data;
    }
}
