package com.inovasialfatih.klinikbwcc.model;

public class DataNewPatientResponse {
    public String status;
    public MessageResponse data;
    public boolean is_show_message;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class MessageResponse {
        public String message;
    }
}
