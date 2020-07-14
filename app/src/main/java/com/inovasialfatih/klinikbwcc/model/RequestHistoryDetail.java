package com.inovasialfatih.klinikbwcc.model;

import java.util.List;

public class RequestHistoryDetail {
    public String status;
    public RequestData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class RequestData{
        public String message;
        public RequestBookingData data;
    }

    public class RequestBookingData{
        public String booking_id;
        public RequestBookingDetail details;
        public List<RequestBookingStatus> status;
    }

    public class RequestBookingDetail{
        public String poly_name;
        public String doctor_name;
        public String date;
        public String time_start;
        public String time_finish;
        public String created_by;
        public String payment_receipt;
        public String patient_name;
        public String booking_date;
        public String queue_number;
    }

    public class RequestBookingStatus{
        public String time;
        public String status;
    }
}
