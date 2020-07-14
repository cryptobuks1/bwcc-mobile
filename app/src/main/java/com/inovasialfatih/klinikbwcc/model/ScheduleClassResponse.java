package com.inovasialfatih.klinikbwcc.model;

import java.util.List;

public class ScheduleClassResponse {
    public String status;
    public ScheduleClassData data;

    public class ScheduleClassData {
        public List<ScheduleResult> result;
    }

    public class ScheduleResult {
        public ScheduleDate info_date;
        public List<ScheduleClass> schedule;
    }

    public class ScheduleDate {
        public String date_start;
        public String is_class_available;
    }

    public class ScheduleClass {
//        public String id_schedule;
        public String class_id;
        public String class_name;
        public List<DataInstructor> info_instructor;
        public String limit_quota;
    }

    public class DataInstructor {
        public String id_schedule;
        public String instructor_id;
        public String name_instructor;
        public String time_start;
        public String time_finish;
    }
}
