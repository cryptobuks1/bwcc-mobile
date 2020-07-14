package com.inovasialfatih.klinikbwcc.model;

public class LoginModel {
    public String status;
    public LoginData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class LoginData {
        private Boolean is_valid;
        public String token_user;
        public String message;
        public LoginParam user_param;
    }

    public class LoginParam {
        public String name;
        public String email;
        public String password;
    }
}
