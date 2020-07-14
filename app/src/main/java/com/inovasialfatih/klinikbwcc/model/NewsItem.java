package com.inovasialfatih.klinikbwcc.model;

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel;

import java.util.List;

public class NewsItem {
    public String status;
    public NewsData data;

    public class NewsData {
        public String message;
        public List<NewsResult> data;
        public String auth;
    }

    public class NewsResult implements ViewModel {
        public String news_id;
        public String title;
        public String short_content;
        public String category;
        public String date_created;
        public String image_url;
        public String type;
    }
}
