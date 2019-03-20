package com.prismk.japaneseelearn.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prismk.japaneseelearn.bean.VideoData;

public class VideoDBManager {

    private static final String DB_NAME = "e_learn_db";
    private static final String TABLE_NAME = "videos";
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
            + IS_VIP_VIDEO + " boolean," + VIDEO_UPLOAD_TIME + " varchar(50),"
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
        int uploadTeacherId = videoData.getUploadtTeacherId();
        ContentValues values = new ContentValues();
        values.put(VIDEO_URL, videoUrlString);
        values.put(VIDEO_IMG_URL, videoImgUrlString);
        values.put(VIDEO_TITLE,videoTitle);
        values.put(VIDEO_INTRODUCTION,videoIntroduction);
        values.put(VIDEO_CONTEXT,videoContext);
        values.put(VIDEO_UPLOAD_TIME,videoUploadTime);
        values.put(IS_VIP_VIDEO,vipVideo);
        values.put(UPLOAD_TEACHER_ID,uploadTeacherId);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }
}
