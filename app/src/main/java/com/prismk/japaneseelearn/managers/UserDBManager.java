package com.prismk.japaneseelearn.managers;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prismk.japaneseelearn.bean.UserData;

import java.util.ArrayList;
import java.util.List;

public class UserDBManager {

    public static final String DB_NAME = "user";
    public static final String TABLE_NAME = "users";
    public static final String ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_pwd";
    public static final String USER_SIGN = "user_sign";
    public static final String USER_TAG = "user_tag";
    public static final String USER_HEADIMG = "user_headimg_urlstring";
    public static final String ISTEACHER = "isteacher";
    public static final String ISVIP = "isvip";
    public static final String USER_NICKNAME = "user_nickname";

    private static final int DB_VERSION = 1;

    private Context mContext;

    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + USER_NAME + " varchar(15),"
            + USER_PWD + " varchar(15)," + ISTEACHER + " integer," + ISVIP + " integer,"
            + USER_SIGN + " varchar(50)," + USER_TAG + " varchar(30),"
            + USER_HEADIMG + " varchar(50)," + USER_NICKNAME + " varchar(20)"
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
                values.put(USER_NAME, "" + i);
                values.put(USER_PWD, "" + i);
                values.put(USER_NICKNAME, "" + i + "号学生的昵称");
                values.put(USER_SIGN, "" + i + "号学生的个性签名");
                values.put(USER_TAG, "" + i + "号学生的标签");
                values.put(USER_HEADIMG, "http://111.231.206.89/lyf/image/1.png");
                values.put(ISTEACHER, 0);
                if (i >= 6) {
                    values.put(ISVIP, 1);
                } else {
                    values.put(ISVIP, 0);
                }
                db.insert(TABLE_NAME, ID, values);
            }

            for (int i = 11; i <= 20; i++) {
                values.clear();
                values.put(USER_NAME, "" + i);
                values.put(USER_PWD, "" + i);
                values.put(USER_NICKNAME, "" + i + "号教师的昵称");
                values.put(USER_SIGN, "" + i + "号教师的个性签名");
                values.put(USER_TAG, "" + i + "号教师的标签");
                values.put(USER_HEADIMG, "");
                values.put(ISTEACHER, 1);
                if (i >= 16) {
                    values.put(ISVIP, 1);
                } else {
                    values.put(ISVIP, 0);
                }
                db.insert(TABLE_NAME, ID, values);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public UserDBManager(Context context) {
        mContext = context;
    }

    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertUserNameAndPWD(UserData userData) {
        String userName = userData.getUserName();
        String userPwd = userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_PWD, userPwd);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

    public boolean updateUserPW(UserData userData) {
        String userName = userData.getUserName();
        String userPwd = userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_PWD, userPwd);
        String where = USER_NAME + "=" + "=\"" + userName + "\"";
        return mSQLiteDatabase.update(TABLE_NAME, values, where, null) > 0;
    }

    public Cursor fetchUserData(int id) throws SQLException {
        Cursor mCursor = mSQLiteDatabase.query(false, TABLE_NAME, null, ID
                + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllUserDatas() {
        return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public boolean deleteUserData(int id) {
        return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
    }

    public boolean deleteUserDatabyname(String name) {
        return mSQLiteDatabase.delete(TABLE_NAME, USER_NAME + "=\"" + name + "\"", null) > 0;
    }

    public boolean deleteAllUserDatas() {
        return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    public String getStringByColumnName(String columnName, int id) {
        Cursor mCursor = fetchUserData(id);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }

    public boolean updateUserDataById(String columnName, int id, String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }

    //根据用户名找用户，可以判断注册时用户名是否已经存在
    public int findUserByName(String userName) {
        int result = 0;
        Cursor mCursor = mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME + "=?", new String[]{userName}, null, null, null);
        if (mCursor != null) {
            result = mCursor.getCount();
            mCursor.close();
        }
        return result;
    }

    //根据用户名和密码找用户，用于登录
    public int findUserByNameAndPwd(String userName, String pwd) {
        int result = 0;
        Cursor mCursor = mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME + "=?" + " and " + USER_PWD + "=?",
                new String[]{userName, pwd}, null, null, null);
        if (mCursor != null) {
            result = mCursor.getCount();
            mCursor.close();
        }
        return result;
    }

    //用于VideoPlayerActivity显示老师的信息 - 根据ID 查找并返回信息
    private List<UserData> queryAll() {
        List<UserData> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from users", null);
        if (cursor != null || cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // 3 5 7 8
                int userID = cursor.getInt(0);
                boolean isTeacher = cursor.getInt(3) > 0;
                boolean isVip = cursor.getInt(4) > 0;
                String teacherSign = cursor.getString(5);
                String teacherAvator = cursor.getString(7);
                String teacherName = cursor.getString(8);
                UserData userData = new UserData();
                userData.setUserId(userID);
                userData.setTeacherUser(isTeacher);
                userData.setVIP(isVip);
                userData.setSign(teacherSign);
                userData.setHeadImgUrlString(teacherAvator);
                userData.setNickName(teacherName);
                list.add(userData);
            }
        }
        return list;
    }

    public List<UserData> getUserDataListFromUserDB() {
        List<UserData> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = queryAll();
        } else {
            openDataBase();
            list = queryAll();
        }
        return list;
    }
    public List<UserData>getTeacherListFromUserDB(){
        List<UserData> list = new ArrayList<>();
        if (mSQLiteDatabase != null) {
            list = queryTeacher();
        } else {
            openDataBase();
            list = queryTeacher();
        }
        return list;
    }
    private List<UserData> queryTeacher() {
        List<UserData> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from users", null);
        if (cursor != null || cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // 3 5 7 8
                int teacherId = cursor.getInt(0);
                boolean isTeacher = cursor.getInt(3) > 0;
                boolean isVip = cursor.getInt(4)>0;
                String teacherSign = cursor.getString(5);
                String teacherAvator = cursor.getString(7);
                String teacherName = cursor.getString(8);
                if (isTeacher){
                    UserData userData = new UserData();
                    userData.setUserId(teacherId);
                    userData.setTeacherUser(isTeacher);
                    userData.setVIP(isVip);
                    userData.setSign(teacherSign);
                    userData.setHeadImgUrlString(teacherAvator);
                    userData.setNickName(teacherName);
                    list.add(userData);
                }
            }
        }
        return list;
    }
    public int getLoginUesrID() {
        int uesrID = 0;
        if (mSQLiteDatabase != null) {
            uesrID = getUserId();
        } else {
            openDataBase();
            uesrID = getUserId();
        }
        return uesrID;
    }

    private int getUserId() {
        SharedPreferences login_sp = mContext.getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        int userID = 0;
        Cursor mCursor = mSQLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + USER_NAME + " = ?", new String[]{name});
        if (mCursor != null && mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                userID = mCursor.getInt(1);
            }
        }
        return userID;
    }

    public int updateUserVipState() {
        if (mSQLiteDatabase != null) {
            return updateVip();
        } else {
            openDataBase();
            return updateVip();
        }
    }

    private int updateVip() {
        int l = 0;
        SharedPreferences login_sp = mContext.getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        mSQLiteDatabase.beginTransaction();
        ContentValues values = null;
        try {
            values = new ContentValues();
            values.put(ISVIP, 1);
            l = mSQLiteDatabase.update(TABLE_NAME, values, USER_NAME + " = ? ", new String[]{name});
            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
            if (l == 1)
                return l;
            else
                return 0;
        }

    }
}
