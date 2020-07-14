package com.inovasialfatih.klinikbwcc.model;

import java.util.List;

public class RequestHistoryClassDetail {
    public String status;
    public RequestData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class RequestData {
        public String message;
        public RequestListData data;
    }

    public class RequestListData {
        public String booking_id;
        public RequestDetail details;
        public List<StatusList> status;
    }

    public class RequestDetail{
        public String patient_name;
        public String class_name;
        public String instructor_name;
        public String date_start_schedule;
        public String start_time_schedule;
        public String finish_time_schedule;
        public String register_date;
    }

    public class StatusList{
        public String updated_date;
        public String status_code;
        public String status;
    }
}
