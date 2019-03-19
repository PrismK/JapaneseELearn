package com.prismk.japaneseelearn.bean;


public class UserData {

    private String userName;
    private String userPwd;
    private int userId;
    public int pwdresetFlag=0;
    private boolean isTeacherUser;
    private boolean isVIP;
    private String sign;
    private String tag;
    private String headImgUrlString;
    private String nickName;

    public UserData(String userName, String userPwd, boolean isTeacherUser, boolean isVIP, String sign, String tag, String headImgUrlString, String nickName) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.isTeacherUser = isTeacherUser;
        this.isVIP = isVIP;
        this.sign = sign;
        this.tag = tag;
        this.headImgUrlString = headImgUrlString;
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrlString() {
        return headImgUrlString;
    }

    public void setHeadImgUrlString(String headImgUrlString) {
        this.headImgUrlString = headImgUrlString;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public boolean isTeacherUser() {
        return isTeacherUser;
    }

    public void setTeacherUser(boolean teacherUser) {
        isTeacherUser = teacherUser;
    }

    public String getUserName() {             //获取用户名
        return userName;
    }

    public void setUserName(String userName) {  //输入用户名
        this.userName = userName;
    }

    public String getUserPwd() {                //获取用户密码
        return userPwd;
    }

    public void setUserPwd(String userPwd) {     //输入用户密码
        this.userPwd = userPwd;
    }

    public int getUserId() {                   //获取用户ID号
        return userId;
    }

    public void setUserId(int userId) {       //设置用户ID号
        this.userId = userId;
    }

    public UserData(String userName, String userPwd) {
        super();
        this.userName = userName;
        this.userPwd = userPwd;
    }

}
