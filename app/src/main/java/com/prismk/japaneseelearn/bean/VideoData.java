package com.prismk.japaneseelearn.bean;


public class VideoData {

    private int userId;
    private String videoUrlString;
    private String videoImgUrlString;
    private String videoTitle;
    private String videoIntroduction;
    private String videoContext;
    private boolean isVipVideo;
    private String uploadTime;
    private int uploadTeacherId;

    public VideoData(int userId, String videoUrlString, String videoImgUrlString,
                     String videoTitle, String videoIntroduction, String videoContext,
                     boolean isVipVideo, String uploadTime, int uploadTeacherId) {
        this.userId = userId;
        this.videoUrlString = videoUrlString;
        this.videoImgUrlString = videoImgUrlString;
        this.videoTitle = videoTitle;
        this.videoIntroduction = videoIntroduction;
        this.videoContext = videoContext;
        this.isVipVideo = isVipVideo;
        this.uploadTime = uploadTime;
        this.uploadTeacherId = uploadTeacherId;
    }

    public VideoData() {
    }

    public int getUploadtTeacherId() {
        return uploadTeacherId;
    }

    public void setUploadTeacherId(int uploadTeacherId) {
        this.uploadTeacherId = uploadTeacherId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVideoUrlString() {
        return videoUrlString;
    }

    public void setVideoUrlString(String videoUrlString) {
        this.videoUrlString = videoUrlString;
    }

    public String getVideoImgUrlString() {
        return videoImgUrlString;
    }

    public void setVideoImgUrlString(String videoImgUrlString) {
        this.videoImgUrlString = videoImgUrlString;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoIntroduction() {
        return videoIntroduction;
    }

    public void setVideoIntroduction(String videoIntroduction) {
        this.videoIntroduction = videoIntroduction;
    }

    public String getVideoContext() {
        return videoContext;
    }

    public void setVideoContext(String videoContext) {
        this.videoContext = videoContext;
    }

    public boolean isVipVideo() {
        return isVipVideo;
    }

    public void setVipVideo(boolean vipVideo) {
        isVipVideo = vipVideo;
    }

}
