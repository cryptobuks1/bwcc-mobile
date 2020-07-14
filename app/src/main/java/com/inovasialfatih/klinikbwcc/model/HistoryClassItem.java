package com.inovasialfatih.klinikbwcc.model;

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel;

import java.util.List;

public class HistoryClassItem {
    public String status;
    public List<HistoryClassData> data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class HistoryClassData implements ViewModel{
        public String booking_class_id;
        public String patient_name;
        public String class_name;
        public String instructor_name;
        public String date_start_schedule;
    }

}
