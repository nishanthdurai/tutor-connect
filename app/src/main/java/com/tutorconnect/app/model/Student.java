package com.tutorconnect.app.model;

public class Student {
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

    public Student() {
    }

    public Student(String studentName, String studentSubject) {
        this.studentName = studentName;
        this.studentSubject = studentSubject;
    }
}
