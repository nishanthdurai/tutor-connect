package com.tutorconnect.app.tutor;

public class putPDF {
    public String name, url;

    public putPDF() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public putPDF(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
