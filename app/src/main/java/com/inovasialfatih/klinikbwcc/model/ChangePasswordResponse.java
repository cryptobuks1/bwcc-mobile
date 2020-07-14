package com.inovasialfatih.klinikbwcc.model;

public class ChangePasswordResponse {
    public String status;
    public ForgotPasswordData data;

    public class ForgotPasswordData{
        public String message;
    }
}
