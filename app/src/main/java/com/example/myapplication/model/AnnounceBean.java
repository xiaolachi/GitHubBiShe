package com.example.myapplication.model;

import java.util.List;

public class AnnounceBean {

    private String content;
    private String publis_time;
    private String years;

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublis_time() {
        return publis_time;
    }

    public void setPublis_time(String publish_time) {
        this.publis_time = publish_time;
    }
}
