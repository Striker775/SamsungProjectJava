package com.example.samsungprojectjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBScheduleClass {
    String login;
    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tableSchedule";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_TIME = "Time";
    private static final String COLUMN_IMPORTANCE = "Importance";
    private static final String COLUMN_LOGIN = "Login";
    private static final String COLUMN_LONGITUDE = "Longitude";
    private static final String COLUMN_LATITUDE = "Latitude";
    private static final String COLUMN_COORDS = "Coords";
    private static final String COLUMN_MENTIONED = "Mentioned";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAME = 1;
    private static final int NUM_COLUMN_TIME = 2;
    private static final int NUM_COLUMN_IMPORTANCE = 3;
    private static final int NUM_COLUMN_LOGIN = 4;
    private static final int NUM_COLUMN_LONGITUDE = 5;
    private static final int NUM_COLUMN_LATITUDE = 6;
    private static final int NUM_COLUMN_COORDS = 7;
    private static final int NUM_COLUMN_MENTIONED = 8;

    private SQLiteDatabase mDataBase;

    public DBScheduleClass(Context context, String login) {
        this.login = login;
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String name, long time, int importance, String login, double longitude, double latitude, boolean coords, int mentioned) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_IMPORTANCE, importance);
        cv.put(COLUMN_LOGIN, login);
        cv.put(COLUMN_LONGITUDE, longitude);
        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_COORDS, coords ? 1 : 0);
        cv.put(COLUMN_MENTIONED, mentioned);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(ScheduleClass md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, md.name);
        cv.put(COLUMN_TIME, md.time);
        cv.put(COLUMN_IMPORTANCE, md.importance);
        cv.put(COLUMN_LOGIN, md.login);
        cv.put(COLUMN_LONGITUDE, md.longitude);
        cv.put(COLUMN_LATITUDE, md.latitude);
        cv.put(COLUMN_COORDS, md.coords ? 1 : 0);
        cv.put(COLUMN_MENTIONED, md.mentioned);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(md.id)});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public ScheduleClass select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        mCursor.moveToFirst();
        String Name = mCursor.getString(NUM_COLUMN_NAME);
        long Time = mCursor.getLong(NUM_COLUMN_TIME);
        int Importance = mCursor.getInt(NUM_COLUMN_IMPORTANCE);
        String Login = mCursor.getString(NUM_COLUMN_LOGIN);
        double Longitude = mCursor.getDouble(NUM_COLUMN_LONGITUDE);
        double Latitude = mCursor.getDouble(NUM_COLUMN_LATITUDE);
        boolean coords = mCursor.getInt(NUM_COLUMN_COORDS) == 1;
        int mentioned = mCursor.getInt(NUM_COLUMN_MENTIONED);
        ScheduleClass sch = new ScheduleClass(id, Name, Time, Importance, Login, mentioned);
        if (coords) sch.setCoords(Longitude, Latitude);
        return sch;
    }

    public ArrayList<ScheduleClass> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<ScheduleClass> arr = new ArrayList<ScheduleClass>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String Name = mCursor.getString(NUM_COLUMN_NAME);
                long Time = mCursor.getLong(NUM_COLUMN_TIME);
                int Importance = mCursor.getInt(NUM_COLUMN_IMPORTANCE);
                String Login = mCursor.getString(NUM_COLUMN_LOGIN);
                double Longitude = mCursor.getDouble(NUM_COLUMN_LONGITUDE);
                double Latitude = mCursor.getDouble(NUM_COLUMN_LATITUDE);
                boolean coords = mCursor.getInt(NUM_COLUMN_COORDS) == 1;
                int mentioned = mCursor.getInt(NUM_COLUMN_MENTIONED);
                ScheduleClass sch = new ScheduleClass(id, Name, Time, Importance, Login, mentioned);
                if (coords) sch.setCoords(Longitude, Latitude);
                arr.add(sch);
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
                    COLUMN_NAME+ " TEXT, " +
                    COLUMN_TIME + " BIGINT, " +
                    COLUMN_IMPORTANCE + " INT, " +
                    COLUMN_LOGIN + " TEXT, " +
                    COLUMN_LONGITUDE + " DOUBLE, " +
                    COLUMN_LATITUDE + " DOUBLE, " +
                    COLUMN_COORDS + " INT, " +
                    COLUMN_MENTIONED + " INT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
