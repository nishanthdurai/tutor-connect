package com.tutorconnect.app.model;

public class Parent {
    private String studentName;
    private String studentSubject;
    private String present;
    private String remarks;
    private String date;

    public String getStudentName() {
        return studentName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSubject() {
        return studentSubject;
    }

    public void setStudentSubject(String studentSubject) {
        this.studentSubject = studentSubject;
    }

    public Parent() {
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Parent(String studentName, String studentSubject, String present, String date, String remarks) {
        this.studentName = studentName;
        this.studentSubject = studentSubject;
        this.present = present;
        this.date = date;
        this.remarks = remarks;
    }
}
