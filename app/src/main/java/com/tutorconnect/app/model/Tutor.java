package com.tutorconnect.app.model;

public class Tutor {
    private String email;
    private String password;
    private String key;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getKey() {
        return key;
    }

    public Tutor() {
    }

    public Tutor(String email, String password, String key) {
        this.email = email;
        this.password = password;
        this.key = key;
    }
}
