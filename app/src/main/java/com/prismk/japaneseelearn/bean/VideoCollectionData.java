package com.prismk.japaneseelearn.bean;


public class VideoCollectionData {

    private int id;
    private int StudentId;
    private int videoId;

    public VideoCollectionData(int studentId, int videoId) {
        StudentId = studentId;
        this.videoId = videoId;
    }

    public VideoCollectionData() {
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

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
