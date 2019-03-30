package com.prismk.japaneseelearn.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prismk.japaneseelearn.bean.VideoCollectionData;

import java.util.ArrayList;
import java.util.List;

public class VideoCollectionDBManager {

    public static final String DB_NAME = "video_collection";
    public static final String TABLE_NAME = "video_collection";
    public static final String ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String VIDEO_ID = "video_id";

    private static final int DB_VERSION = 1;

    private Context mContext;

    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + USER_ID + " integer,"
            + VIDEO_ID + " integer"
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
            for (int i = 1; i <= 10; i++) {
                values.clear();
                values.put(USER_ID, i);
                values.put(VIDEO_ID, i);
                db.insert(TABLE_NAME, ID, values);

                values.clear();
                values.put(USER_ID, i);
                values.put(VIDEO_ID, 10 + i);

                values.clear();
                values.put(USER_ID, i);
                values.put(VIDEO_ID, 20 + i);
                db.insert(TABLE_NAME, ID, values);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public VideoCollectionDBManager(Context context) {
        mContext = context;
    }

    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertFollowData(VideoCollectionData videoCollectionData) {
        int studentId = videoCollectionData.getStudentId();
        int videoId = videoCollectionData.getVideoId();
        ContentValues values = new ContentValues();
        values.put(USER_ID, studentId);
        values.put(VIDEO_ID, videoId);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }
    public long insertFavoriteVideo(VideoCollectionData videoCollectionData) {
        if (mSQLiteDatabase != null) {
            return insertData(videoCollectionData);
        } else {
            openDataBase();
            return insertData(videoCollectionData);
        }

    }

    private int deleteData(VideoCollectionData videoCollectionData) {
        mSQLiteDatabase.beginTransaction();
        int l = 0;
        try {
            l = mSQLiteDatabase.delete(TABLE_NAME, VIDEO_ID + " =  ? ", new String[]{String.valueOf(videoCollectionData.getVideoId())});
            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
            if (l == 1)
                return l;
            else
                return 0;
        }
    }

    public int deleteFavoriteVideo(VideoCollectionData videoCollectionData) {
        if (mSQLiteDatabase != null) {
            return deleteData(videoCollectionData);
        } else {
            openDataBase();
            return deleteData(videoCollectionData);
        }
    }

    private long insertData(VideoCollectionData videoCollectionData) {
        long start = -1;
        long end = -1;
        long l = 0;
        mSQLiteDatabase.beginTransaction();
        ContentValues values = null;
        try {
            values = new ContentValues();
            values.put(USER_ID, videoCollectionData.getStudentId());
            values.put(VIDEO_ID, videoCollectionData.getVideoId());
            l = mSQLiteDatabase.insert(TABLE_NAME, ID, values);
            if (start == -1) {
                start = l;
            }
            end = l + 1;

            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
            if (1 == end - start) {
                return l;
            } else {
                return 0;
            }
        }


    }

    public List<VideoCollectionData> getVideoFlollowedDataFromDB() {
        List<VideoCollectionData> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = query();
        } else {
            openDataBase();
            list = query();
        }
        return list;
    }

    private List<VideoCollectionData> query() {
        List<VideoCollectionData> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from "+TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int studentId = cursor.getInt(1);
                int videoId = cursor.getInt(2);
                VideoCollectionData videoCollectionData = new VideoCollectionData();
                videoCollectionData.setStudentId(studentId);
                videoCollectionData.setVideoId(videoId);
                list.add(videoCollectionData);
            }
        }

        return list;
    }

    public List<Integer> getFavoriteVideoId(int loginuserID) {
        List<Integer> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = getVideoID(loginuserID);
        } else {
            openDataBase();
            list = getVideoID(loginuserID);
        }

        return list;
    }

    private List<Integer> getVideoID(int loginuserID) {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + USER_ID + " = ?", new String[]{String.valueOf(loginuserID)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int videoId = cursor.getInt(2);
                list.add(videoId);
            }
        }
        return list;
    }
}
