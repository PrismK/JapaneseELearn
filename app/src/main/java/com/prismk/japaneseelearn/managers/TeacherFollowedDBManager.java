package com.prismk.japaneseelearn.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prismk.japaneseelearn.bean.TeacherFollowedData;
import com.prismk.japaneseelearn.bean.UserData;

public class TeacherFollowedDBManager {

    public static final String DB_NAME = "teacher_followed";
    public static final String TABLE_NAME = "teacher_followed";
    public static final String ID = "_id";
    public static final String STUDENT_ID = "student_id";
    public static final String TEACHER_ID = "teacher_id";

    private static final int DB_VERSION = 1;

    private Context mContext;

    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + STUDENT_ID + " integer,"
            + TEACHER_ID + " inteager"
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
                values.put(TEACHER_ID, 10 + i);
                db.insert(TABLE_NAME, ID, values);

                if (i == 5) {
                    values.clear();
                    values.put(STUDENT_ID, i);
                    values.put(TEACHER_ID, 19 - i);
                } else {
                    values.clear();
                    values.put(STUDENT_ID, i);
                    values.put(TEACHER_ID, 20 - i);
                }

                db.insert(TABLE_NAME, ID, values);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public TeacherFollowedDBManager(Context context) {
        mContext = context;
    }

    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertFollowData(TeacherFollowedData teacherFollowedData) {
        int studentId = teacherFollowedData.getStudentId();
        int teacherId = teacherFollowedData.getTeacherId();
        ContentValues values = new ContentValues();
        values.put(STUDENT_ID, studentId);
        values.put(TEACHER_ID, teacherId);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

}
