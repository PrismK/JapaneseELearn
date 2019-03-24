package com.prismk.japaneseelearn.bean;


public class TeacherFollowedData {

    private int id;
    private int StudentId;
    private int teacherId;

    public TeacherFollowedData(int studentId, int teacherId) {
        StudentId = studentId;
        this.teacherId = teacherId;
    }

    public TeacherFollowedData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}
