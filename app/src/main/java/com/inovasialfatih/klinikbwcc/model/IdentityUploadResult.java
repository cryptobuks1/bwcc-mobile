package com.inovasialfatih.klinikbwcc.model;

public class IdentityUploadResult {
    public String status;
    public UploadData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class UploadData {
        public String message;
        public String file;
        public UploadDataParam param;
    }

    public class UploadDataParam {
        public String booking_id;
    }
}
