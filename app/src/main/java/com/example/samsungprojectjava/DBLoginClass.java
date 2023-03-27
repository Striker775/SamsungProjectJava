package com.example.samsungprojectjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBLoginClass { //Можно через дженерики
    private static final String DATABASE_NAME = "login.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tableLogin";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LAST_LOGIN = "Last_Login";
    private static final String COLUMN_LAST_PASSWORD = "Last_password";
    private static final String COLUMN_REMEMBER = "Remember";
    private static final String COLUMN_HIGH_TASKS = "High_tasks";
    private static final String COLUMN_MEDIUM_TASKS = "Medium_tasks";
    private static final String COLUMN_LOW_TASKS = "Low_tasks";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LAST_LOGIN  = 1;
    private static final int NUM_COLUMN_LAST_PASSWORD = 2;
    private static final int NUM_COLUMN_REMEMBER = 3;
    private static final int NUM_COLUMN_HIGH_TASKS = 4;
    private static final int NUM_COLUMN_MEDIUM_TASKS = 5;
    private static final int NUM_COLUMN_LOW_TASKS = 6;

    private SQLiteDatabase mDataBase;
    public DBLoginClass(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String login, String password, int remember, int high, int medium, int low) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LAST_LOGIN, login);
        cv.put(COLUMN_LAST_PASSWORD, password);
        cv.put(COLUMN_REMEMBER, remember);
        cv.put(COLUMN_HIGH_TASKS, high);
        cv.put(COLUMN_MEDIUM_TASKS, medium);
        cv.put(COLUMN_LOW_TASKS, low);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(LoginClass md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LAST_LOGIN, md.login);
        cv.put(COLUMN_LAST_PASSWORD, md.password);
        cv.put(COLUMN_REMEMBER, md.remember);
        cv.put(COLUMN_HIGH_TASKS, md.high_tasks);
        cv.put(COLUMN_MEDIUM_TASKS, md.medium_tasks);
        cv.put(COLUMN_LOW_TASKS, md.low_tasks);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(md.id)});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public LoginClass select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        mCursor.moveToFirst();
        String Login = mCursor.getString(NUM_COLUMN_LAST_LOGIN);
        String Password = mCursor.getString(NUM_COLUMN_LAST_PASSWORD);
        int remember = mCursor.getInt(NUM_COLUMN_REMEMBER);
        int high = mCursor.getInt(NUM_COLUMN_HIGH_TASKS);
        int medium = mCursor.getInt(NUM_COLUMN_MEDIUM_TASKS);
        int low = mCursor.getInt(NUM_COLUMN_LOW_TASKS);
        return new LoginClass(id, Login, Password, remember, high, medium, low);
    }
    public LoginClass select_by_login(String Login) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_LAST_LOGIN + " = ?", new String[]{String.valueOf(Login)}, null, null, null);
        mCursor.moveToFirst();
        long id = mCursor.getLong(NUM_COLUMN_ID);
        String Password = mCursor.getString(NUM_COLUMN_LAST_PASSWORD);
        int remember = mCursor.getInt(NUM_COLUMN_REMEMBER);
        int high = mCursor.getInt(NUM_COLUMN_HIGH_TASKS);
        int medium = mCursor.getInt(NUM_COLUMN_MEDIUM_TASKS);
        int low = mCursor.getInt(NUM_COLUMN_LOW_TASKS);
        return new LoginClass(id, Login, Password, remember, high, medium, low);
    }
    public LoginClass select_by_remember(int remember) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_REMEMBER + " = ?", new String[]{String.valueOf(remember)}, null, null, null);
        mCursor.moveToFirst();
        long id = mCursor.getLong(NUM_COLUMN_ID);
        String Login = mCursor.getString(NUM_COLUMN_LAST_LOGIN);
        String Password = mCursor.getString(NUM_COLUMN_LAST_PASSWORD);
        int high = mCursor.getInt(NUM_COLUMN_HIGH_TASKS);
        int medium = mCursor.getInt(NUM_COLUMN_MEDIUM_TASKS);
        int low = mCursor.getInt(NUM_COLUMN_LOW_TASKS);
        return new LoginClass(id, Login, Password, remember, high, medium, low);
    }

    public ArrayList<LoginClass> selectAll(boolean select_remember) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<LoginClass> arr = new ArrayList<LoginClass>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                if (!select_remember || (select_remember && mCursor.getInt(NUM_COLUMN_REMEMBER) == 1)) {
                    long id = mCursor.getLong(NUM_COLUMN_ID);
                    String Login = mCursor.getString(NUM_COLUMN_LAST_LOGIN);
                    String Password = mCursor.getString(NUM_COLUMN_LAST_PASSWORD);
                    int remember = mCursor.getInt(NUM_COLUMN_REMEMBER);
                    int high = mCursor.getInt(NUM_COLUMN_HIGH_TASKS);
                    int medium = mCursor.getInt(NUM_COLUMN_MEDIUM_TASKS);
                    int low = mCursor.getInt(NUM_COLUMN_LOW_TASKS);
                    arr.add(new LoginClass(id, Login, Password, remember, high, medium, low));
                }
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LAST_LOGIN + " TEXT, " +
                    COLUMN_LAST_PASSWORD + " TEXT, " +
                    COLUMN_REMEMBER + " INTEGER, " +
                    COLUMN_HIGH_TASKS + " INTEGER, " +
                    COLUMN_MEDIUM_TASKS + " INTEGER, " +
                    COLUMN_LOW_TASKS + " INTEGER);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}

