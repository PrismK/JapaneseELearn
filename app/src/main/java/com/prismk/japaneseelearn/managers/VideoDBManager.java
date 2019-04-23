package com.prismk.japaneseelearn.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prismk.japaneseelearn.bean.VideoData;

import java.util.ArrayList;
import java.util.List;

public class VideoDBManager {

    public static final String DB_NAME = "video";
    public static final String TABLE_NAME = "videos";
    public static final String ID = "_id";
    public static final String VIDEO_URL = "video_url";
    public static final String VIDEO_IMG_URL = "video_img_url";
    public static final String VIDEO_TITLE = "video_title";
    public static final String VIDEO_INTRODUCTION = "video_introduction";
    public static final String VIDEO_CONTEXT = "video_context";
    public static final String IS_VIP_VIDEO = "is_vip_video";
    public static final String VIDEO_UPLOAD_TIME = "video_upload_time";
    public static final String UPLOAD_TEACHER_ID = "upload_teacher_id";

    private static final int DB_VERSION = 1;

    private Context mContext;

    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + VIDEO_URL + " varchar(50),"
            + VIDEO_IMG_URL + " varchar(50)," + VIDEO_TITLE + " varchar(30),"
            + VIDEO_INTRODUCTION + " varchar(100)," + VIDEO_CONTEXT + " varchar(300),"
            + IS_VIP_VIDEO + " intger," + VIDEO_UPLOAD_TIME + " varchar(50),"
            + UPLOAD_TEACHER_ID + " integer,"
            + "foreign key(" + UPLOAD_TEACHER_ID + ") references users(_id)"
            + ");";

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);

            ContentValues values = new ContentValues();

            for (int i = 1; i <= 30; i++) {
                values.clear();
                values.put(VIDEO_URL, "http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4");
                values.put(VIDEO_IMG_URL, "http://img4.jiecaojingxuan.com/2016/11/23/00b026e7-b830-4994-bc87-38f4033806a6.jpg@!640_360");
                values.put(VIDEO_TITLE, "" + i + "号视频的标题");
                values.put(VIDEO_INTRODUCTION, "" + i + "号视频的简介");
                values.put(VIDEO_CONTEXT, "" + i + "号视频的内容文字描述安徽v就爱看女傻女那是陇南市吕尼拉v你啊啊是离开农村快乐女声v扣篮速率那粮库看来你真v看来嫩绿嫩绿那里的女郎在哪里");
                String dayNum = null;
                if (i <= 9)
                    dayNum = "0" + i;
                else
                    dayNum = "" + i;
                values.put(VIDEO_UPLOAD_TIME, "2019-03-" + dayNum);
                if (i >= 11 && i <= 20) {
                    values.put(UPLOAD_TEACHER_ID, i);
                } else if (i <= 10 && i >= 6) {
                    values.put(UPLOAD_TEACHER_ID, i * 2);
                } else if (i <= 5) {
                    values.put(UPLOAD_TEACHER_ID, i * 2 + 10);
                } else {
                    values.put(UPLOAD_TEACHER_ID, i / 2);
                }
                if (i >= 19) {
                    values.put(IS_VIP_VIDEO, 1);
                } else {
                    values.put(IS_VIP_VIDEO, 0);
                }
                db.insert(TABLE_NAME, ID, values);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public VideoDBManager(Context context) {
        mContext = context;
    }

    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertVideoData(VideoData videoData) {
        String videoUrlString = videoData.getVideoUrlString();
        String videoImgUrlString = videoData.getVideoImgUrlString();
        String videoTitle = videoData.getVideoTitle();
        String videoIntroduction = videoData.getVideoIntroduction();
        String videoContext = videoData.getVideoContext();
        String videoUploadTime = videoData.getUploadTime();
        boolean vipVideo = videoData.isVipVideo();
        int uploadTeacherId = videoData.getUploadTeacherId();
        ContentValues values = new ContentValues();
        values.put(VIDEO_URL, videoUrlString);
        values.put(VIDEO_IMG_URL, videoImgUrlString);
        values.put(VIDEO_TITLE, videoTitle);
        values.put(VIDEO_INTRODUCTION, videoIntroduction);
        values.put(VIDEO_CONTEXT, videoContext);
        values.put(VIDEO_UPLOAD_TIME, videoUploadTime);
        values.put(IS_VIP_VIDEO, vipVideo);
        values.put(UPLOAD_TEACHER_ID, uploadTeacherId);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

    private List<VideoData> query() {
        List<VideoData> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + TABLE_NAME + " order by " + VIDEO_UPLOAD_TIME + " DESC", null);
//        Cursor cursor = mSQLiteDatabase.query(TABLE_NAME,null,null,null,null,null,VIDEO_UPLOAD_TIME + " DESC");
        if (cursor != null || cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int videoId = cursor.getInt(0);
                String url = cursor.getString(1);
                String imageUrl = cursor.getString(2);
                String videoTitle = cursor.getString(3);
                String videoIntroduction = cursor.getString(4);
                String videoContext = cursor.getString(5);
                // ???
                boolean isVipClass = cursor.getInt(6) > 0;
                String uploadTime = cursor.getString(7);
                int uploadTeacherId = cursor.getInt(8);
                VideoData data = new VideoData();
                data.setVideoId(videoId);
                data.setVideoUrlString(url);
                data.setVideoImgUrlString(imageUrl);
                data.setVideoTitle(videoTitle);
                data.setVideoIntroduction(videoIntroduction);
                data.setVideoContext(videoContext);
                data.setVipVideo(isVipClass);
                data.setUploadTime(uploadTime);
                data.setUploadTeacherId(uploadTeacherId);
                list.add(data);
            }
        }
        return list;
    }

    public List<VideoData> getVideoDataListFromVideoDB() {
        List<VideoData> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = query();
        } else {
            openDataBase();
            list = query();
        }
        return list;
    }

    public List<VideoData> getVideoDataListFromTeacherID(int teacherID) {
        List<VideoData> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = queryFromTeacher(teacherID);
        } else {
            openDataBase();
            list = queryFromTeacher(teacherID);
        }
        return list;
    }

    private List<VideoData> queryFromTeacher(int teacherID) {
        List<VideoData> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + UPLOAD_TEACHER_ID + " = ?" + "order by " + VIDEO_UPLOAD_TIME + " DESC", new String[]{String.valueOf(teacherID)});
        if (cursor != null || cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int videoID = cursor.getInt(0);
                String url = cursor.getString(1);
                String imageUrl = cursor.getString(2);
                String videoTitle = cursor.getString(3);
                String videoIntroduction = cursor.getString(4);
                String videoContext = cursor.getString(5);
                // ???
                boolean isVipClass = cursor.getInt(6) > 0;
                String uploadTime = cursor.getString(7);
                VideoData data = new VideoData();
                data.setVideoId(videoID);
                data.setVideoUrlString(url);
                data.setVideoImgUrlString(imageUrl);
                data.setVideoTitle(videoTitle);
                data.setVideoIntroduction(videoIntroduction);
                data.setVideoContext(videoContext);
                data.setVipVideo(isVipClass);
                data.setUploadTime(uploadTime);
                list.add(data);
            }
        }
        return list;
    }
}
