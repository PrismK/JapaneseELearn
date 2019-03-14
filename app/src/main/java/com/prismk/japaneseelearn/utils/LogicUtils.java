package com.prismk.japaneseelearn.utils;

public class LogicUtils {
    private long userPhone = 13666666666L;
    private static LogicUtils logicUtils;

    private LogicUtils() {
    }

    public static LogicUtils getInstance() {
        if (logicUtils == null) {
            logicUtils = new LogicUtils();
        }
        return logicUtils;
    }

    public boolean isLogined() {
        return userPhone != 0 ? true : false;
    }

    public long getUserPhone() {
        return isLogined() ? userPhone : 0;
    }

    public void uerLogin(long userPhone) {
        this.userPhone = userPhone;
    }
}
