package com.example.androidprojecttutor;

public class student {
    private String studentName;
    private String studentSubject;

    public String getStudentName() {
        return studentName;
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

    public student() {
    }

    public student(String studentName, String studentSubject) {
        this.studentName = studentName;
        this.studentSubject = studentSubject;
    }
    }

