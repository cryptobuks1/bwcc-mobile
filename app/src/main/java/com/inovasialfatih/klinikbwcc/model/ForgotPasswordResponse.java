package com.inovasialfatih.klinikbwcc.model;

public class ForgotPasswordResponse {
    public String status;
    public ForgotPasswordData data;

    public class ForgotPasswordData{
        public String message;
        public ForgotPasswordResult data;
    }

    public class ForgotPasswordResult{
        public String email;
        public String send;
    }
}
