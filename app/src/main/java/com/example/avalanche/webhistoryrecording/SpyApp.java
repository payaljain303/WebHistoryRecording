package com.example.avalanche.webhistoryrecording;

import java.io.Serializable;

/**
 * Created by avalanche on 2/2/17.
 */
public class SpyApp implements Serializable{
    private String title;
    private String url;
    private String no;
    private String date;
    private String webId;

    /*public SpyApp(String title, String url, String no, String date) {
        this.title = title;
        this.url = url;
        this.no = no;
        this.date = date;
    }*/


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWebId() {
        return webId;
    }

    public void setWebId(String webId) {
        this.webId = webId;
    }
}
