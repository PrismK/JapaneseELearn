package com.prismk.japaneseelearn.managers;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * create by Nevermore
 * 2019/4/22
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class UpdateEverydayManager {
    List<String> updateStrList;
    String[] updateStrArray;
    private static final int UPDATE = 0;
    private TextView toUpdate;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE) {
                if (updateStrArray != null) {
                    toUpdate.setText(updateStrArray[(Integer) msg.obj]);
                } else if (updateStrList != null) {
                    toUpdate.setText(updateStrList.get((Integer) msg.obj));
                } else if (updateStrList.size() < 9 || updateStrArray.length < 9) {
                    toUpdate.setText("获取每日一句失败");
                }
            }
        }
    };
    private CharSequence timerTime;
    private CharSequence todayDate;

    public UpdateEverydayManager(TextView toUpdate) {
        this.toUpdate = toUpdate;
        init();
    }

    public UpdateEverydayManager(List<String> updateStrList, TextView toUpdate) {
        this.updateStrList = updateStrList;
        this.toUpdate = toUpdate;
        init();
    }

    public UpdateEverydayManager(String[] updateStrArray, TextView toUpdate) {
        this.updateStrArray = updateStrArray;
        this.toUpdate = toUpdate;
        init();
    }

    private void init() {
        getNowTime();
        initFakeData();
        setDefaultShow();
    }

    private void setDefaultShow() {

        if (updateStrArray != null) {
            toUpdate.setText(updateStrArray[Integer.parseInt(String.valueOf(todayDate.charAt(9)))]);
        } else if (updateStrList != null) {
            toUpdate.setText(updateStrList.get(Integer.parseInt(String.valueOf(todayDate.charAt(9)))));
        } else if (updateStrList.size() < 9 || updateStrArray.length < 9) {
            toUpdate.setText("获取每日一句失败");
        }
    }

    private void initFakeData() {
        updateStrArray = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        updateStrList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            updateStrList.add(String.valueOf(i));
        }
    }

    private String getNowTime() {
        Date nowDate = new Date();
        todayDate = DateFormat.format("yyyy-MM-dd HH:mm:ss ", nowDate);
        return todayDate.toString().substring(0, 10);
    }

    public void checkSystemTime() {
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Date timerDate = new Date();
                timerTime = DateFormat.format("yyyy-MM-dd HH:mm:ss ", timerDate);
                String substring = timerTime.toString().substring(0, 10);
                if (!(substring.equals(getNowTime())) || (timerTime.toString().substring(11, 16).equals("17:33"))) {
                    Message message = Message.obtain();
                    message.obj = Integer.parseInt(String.valueOf(timerTime.charAt(9)));
                    handler.sendMessage(message);
                }
            }
        };
        timer.schedule(timerTask, 0, 2000);
    }

    public void addStringList(List<String> updateStrList) {
        this.updateStrList = updateStrList;
    }

    public void addStringArray(String[] updateStrArray) {
        this.updateStrArray = updateStrArray;
    }

}
