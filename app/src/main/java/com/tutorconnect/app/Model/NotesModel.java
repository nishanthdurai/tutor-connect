package com.tutorconnect.app.Model;

public class NotesModel {
    String name, url;

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

    public NotesModel() {
    }

    public NotesModel(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
