package com.inovasialfatih.klinikbwcc.model;

import java.util.List;

public class ImageSlider {
    public String status;
    public List<ImageSliderData> data;

    public boolean isStatus() {
        return status.equals("Success");
    }

    public class ImageSliderData{
        public String image_url;
    }

}
