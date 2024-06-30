package com.tutorconnect.app.model;

public class ParentTutor {
    private String name;
    private String id;
    private String studentId;
    private String tutorId;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }

    public void setStudentId(String id) {
        this.studentId = id;
    }
    public String getStudentId() {
        return studentId;
    }

    public void setTutorId(String id) {
        this.tutorId = id;
    }
    public String getTutorId() {
        return tutorId;
    }

    public ParentTutor(String name, String id, String studentId, String tutorId) {
        this.name = name;
        this.id = id;
        this.studentId = studentId;
        this.tutorId = tutorId;
    }
}
