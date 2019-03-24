package com.prismk.japaneseelearn.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoCollectionData;

public class VideoCollectionDBManager {

    public static final String DB_NAME = "video_collection";
    public static final String TABLE_NAME = "video_collection";
    public static final String ID = "_id";
    public static final String STUDENT_ID = "student_id";
    public static final String VIDEO_ID = "video_id";

    private static final int DB_VERSION = 1;

    private Context mContext;

    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + STUDENT_ID + " integer,"
            + VIDEO_ID + " inteager"
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
                values.put(STUDENT_ID, i);
                values.put(VIDEO_ID, i);
                db.insert(TABLE_NAME, ID, values);

                values.clear();
                values.put(STUDENT_ID, i);
                values.put(VIDEO_ID, 10 + i);

                values.clear();
                values.put(STUDENT_ID, i);
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
        values.put(STUDENT_ID, studentId);
        values.put(VIDEO_ID, videoId);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

}
