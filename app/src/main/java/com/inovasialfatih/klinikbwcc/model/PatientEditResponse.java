package com.inovasialfatih.klinikbwcc.model;

public class PatientEditResponse {

    public String status;
    public String data;
    public boolean is_show_message;

    public boolean isStatus() {
        return status.equals("Success");
    }
}
