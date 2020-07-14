package com.inovasialfatih.klinikbwcc.model;

public class LoginData {
    private Boolean is_valid;
    public String message;
    private String token_user;
    public LoginUserParam user_param;


    public class LoginUserParam {
        public String name;
        public String email;

    }

    public LoginData() {
    }


    public Boolean getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(Boolean is_valid) {
        this.is_valid = is_valid;
    }

    public String getToken_user() {
        return token_user;
    }

    public void setToken_user(String token_user) {
        this.token_user = token_user;
    }
}
