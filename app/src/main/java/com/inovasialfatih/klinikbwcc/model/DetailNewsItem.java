package com.inovasialfatih.klinikbwcc.model;

public class DetailNewsItem {
    public String status;
    public NewsItemData data;

    public class NewsItemData{
        public String message;
        public NewsItemDetail data;
    }

    public class NewsItemDetail{
        public String news_id;
        public String title;
        public String content;
        public String category;
        public String date_created;
        public String image_url;
        public String type;

    }
}
