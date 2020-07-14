package com.inovasialfatih.klinikbwcc.model;

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel;

import java.util.List;

public class PatientListItem {
    public String status;
    public List<PatientDetail> data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class PatientDetail implements ViewModel {
        public String pasien_id;
        public String nama;
        public String jenis_kelamin;
        public String tanggal_lahir;

    }
}
