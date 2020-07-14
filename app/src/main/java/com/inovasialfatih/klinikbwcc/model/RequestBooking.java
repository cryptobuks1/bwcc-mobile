package com.inovasialfatih.klinikbwcc.model;

public class RequestBooking {
    public String status;
    public RequestBookingData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class RequestBookingData {
        public String message;
        public String booking_id;
        public String status;

    }

}
