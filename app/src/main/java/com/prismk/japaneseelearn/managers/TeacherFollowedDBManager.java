package com.prismk.japaneseelearn.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prismk.japaneseelearn.bean.TeacherFollowedData;

import java.util.ArrayList;
import java.util.List;

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
            + TEACHER_ID + " integer"
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

    public long insertFavoriteTeacher(TeacherFollowedData teacherFollowedData) {
        if (mSQLiteDatabase != null) {
            return insertData(teacherFollowedData);
        } else {
            openDataBase();
            return insertData(teacherFollowedData);
        }

    }

    private int deleteData(TeacherFollowedData teacherFollowedData) {
        mSQLiteDatabase.beginTransaction();
        int l = 0;
        try {
            l = mSQLiteDatabase.delete(TABLE_NAME, STUDENT_ID + " = ? and  " + TEACHER_ID + " =  ? ", new String[]{String.valueOf(teacherFollowedData.getStudentId()),String.valueOf(teacherFollowedData.getTeacherId())});
            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
            if (l == 1)
                return l;
            else
                return 0;
        }
    }

    public int deleteFavoriteTeacher(TeacherFollowedData teacherFollowedData) {
        if (mSQLiteDatabase != null) {
            return deleteData(teacherFollowedData);
        } else {
            openDataBase();
            return deleteData(teacherFollowedData);
        }
    }

    private long insertData(TeacherFollowedData teacherFollowedData) {
        long start = -1;
        long end = -1;
        long l = 0;
        mSQLiteDatabase.beginTransaction();
        ContentValues values = null;
        try {
            values = new ContentValues();
            values.put(STUDENT_ID, teacherFollowedData.getStudentId());
            values.put(TEACHER_ID, teacherFollowedData.getTeacherId());
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

    public List<TeacherFollowedData> getTeacherFlollowedDataFromDB() {
        List<TeacherFollowedData> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = query();
        } else {
            openDataBase();
            list = query();
        }
        return list;
    }

    private List<TeacherFollowedData> query() {
        List<TeacherFollowedData> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int studentId = cursor.getInt(1);
                int teacherId = cursor.getInt(2);
                TeacherFollowedData teacherFollowedData = new TeacherFollowedData();
                teacherFollowedData.setStudentId(studentId);
                teacherFollowedData.setTeacherId(teacherId);
                list.add(teacherFollowedData);
            }
        }

        return list;
    }

    public List<Integer> getFavoriteTeacherId(int loginuserID) {
        List<Integer> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = getTeacherID(loginuserID);
        } else {
            openDataBase();
            list = getTeacherID(loginuserID);
        }

        return list;
    }

    private List<Integer> getTeacherID(int loginuserID) {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + STUDENT_ID + " = ?", new String[]{String.valueOf(loginuserID)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int teacherID = cursor.getInt(2);
                list.add(teacherID);
            }
        }
        return list;
    }


    public int getFansCount(int teacherID) {

        if (mSQLiteDatabase != null) {
            return getCount(teacherID);
        } else {
            openDataBase();
            return getCount(teacherID);
        }
    }

    private int getCount(int teacherID) {
        int count = 0;
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + TEACHER_ID + " = ?", new String[]{String.valueOf(teacherID)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                count = cursor.getCount();
            }
        }
        return count;
    }

}
