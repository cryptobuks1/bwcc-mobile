package com.inovasialfatih.klinikbwcc.model;

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel;

import java.util.List;

public class HistoryItem {
    public String status;
    public HistoryItemData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class HistoryItemData{
        public String message;
        public List<HistoryItemList> status;
    }

    public class HistoryItemList implements ViewModel {
        public String booking_id;
        public String patient_id;
        public String created_date;
        public String patient_name;
        public String date;
        public String time_start;
        public String time_finish;
    }

}
