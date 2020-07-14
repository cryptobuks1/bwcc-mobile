package com.inovasialfatih.klinikbwcc.model;

import java.util.List;

public class ScheduleResponse {
    public String status;
    public ScheduleData data;

    public class ScheduleData {
        public String message;
        public List<ScheduleDataResult> result;
    }

    public class ScheduleDataResult {
        public ScheduleDataDay day;
        public List<ScheduleDataPoly> poly;
    }

    public class ScheduleDataDay {
        public String day;
        public String date;
    }

    public class ScheduleDataPoly {
        public String id_poly;
        public String poly_code;
        public String poly_name;
        public List<ScheduleDataDoctors> doctors;
    }

    public class ScheduleDataDoctors {
        public ScheduleDoctors doctor;
        public List<ScheduleTime> schedule;
    }

    public class ScheduleDoctors{
        public String id;
        public String name;
    }

    public class ScheduleTime{
        public String schedule_id;
        public String time;
        public String queue_number;
        public String status;
    }


}
