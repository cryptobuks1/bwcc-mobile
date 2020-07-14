package com.inovasialfatih.klinikbwcc.model;

import java.util.List;

public class DoctorScheduleResponse {
    public String status;
    public DoctorScheduleData data;

    public class DoctorScheduleData{
        public String message;
        public List<DoctorScheduleList> schedule;
    }

    public class DoctorScheduleList{
        public String poly_id;
        public String poly_name;
        public List<DoctorResult> doctors;
    }

    public class DoctorResult{
        public String doctor_id;
        public String name;
        public String specialist;
        public String image_url;
        public String experience;
        // public List<DoctorScheduleTime> schedule;
        public List<DoctorScheduleTime> jadwal_absen;
    }

    public class DoctorScheduleTime{
        public String day;
        public String date;
        public String hour;
        public String jam_absen;
        public String status;
    }
}
