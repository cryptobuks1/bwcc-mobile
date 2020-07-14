package com.inovasialfatih.klinikbwcc.model;

public class PatientDetailItem {
    public String status;
    public RequestPatientDetail data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class RequestPatientDetail {
        public String pasien_id;
        public String kode_rm;
        public String nama;
        public String jenis_kelamin;
        public String jenis_pasien;
        public String tempat_lahir;
        public String tanggal_lahir;
        public String alamat;
        public String no_hp;
        public String email;
        public String jenis_pembayaran;
    }
}
