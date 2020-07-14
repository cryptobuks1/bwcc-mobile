package com.inovasialfatih.klinikbwcc.model;

public class RequestCekRmResult {
    public String status;
    public RequestRmData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class RequestRmData {
        public String kode_rm;
        public String nama;
        public String no_ktp;
        public String jk;
        public String tempat_lahir;
        public String tanggal_lahir;
        public String nm_ibu;
        public String alamat;
        public String gol_darah;
        public String pekerjaan;
        public String stts_nikah;
        public String agama;
        public String tgl_daftar;
        public String no_tlp;
        public String umur;
        public String pnd;
        public String keluarga;
        public String namakeluarga;
        public String kd_pj;
        public String pno_pesertand;
        public String kd_kel;
        public String kd_kec;
        public String kd_kab;
        public String pekerjaanpj;
        public String alamatpj;
        public String kelurahanpj;
        public String kecamatanpj;
        public String kabupatenpj;
        public String perusahaan_pasien;
        public String suku_bangsa;
        public String bahasa_pasien;
        public String cacat_fisik;
        public String email;
        public String nip;
        public String kd_prop;
        public String propinsipj;

    }



}
