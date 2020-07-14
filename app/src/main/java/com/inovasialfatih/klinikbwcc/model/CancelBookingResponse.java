package com.inovasialfatih.klinikbwcc.model;

public class CancelBookingResponse {
    public String status;
    public String data;

    public boolean isStatus() {
        return status.equals("Success");
    }
}
