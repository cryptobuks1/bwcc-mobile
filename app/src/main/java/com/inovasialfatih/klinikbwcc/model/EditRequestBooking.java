package com.inovasialfatih.klinikbwcc.model;

import java.util.List;

public class EditRequestBooking {
    public String status;
    public List<RequestBookingData> data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class RequestBookingData {
        public String doctor_id;
        public String start_time;
        public String end_time;

    }

}
