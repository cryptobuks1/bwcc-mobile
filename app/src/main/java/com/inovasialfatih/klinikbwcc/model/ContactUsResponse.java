package com.inovasialfatih.klinikbwcc.model;

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel;

import java.util.List;

public class ContactUsResponse {
    public String status;
    public ContactUsData data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class ContactUsData{
        public String id;
        public String alamat_klinik;
        public String email;
        public List<PhoneNumber> phone_number;
        public String whatsapp_number;
        public String facebook_name;
        public String facebook;
        public String instagram_name;
        public String instagram;
        public String youtube_name;
        public String youtube;
        public String twitter_name;
        public String twitter;
    }

    public class PhoneNumber implements ViewModel {
        public String number;
    }
}
