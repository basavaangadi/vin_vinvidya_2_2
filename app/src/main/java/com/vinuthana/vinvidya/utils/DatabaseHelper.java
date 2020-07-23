package com.vinuthana.vinvidya.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vinuthana.vinvidya.app.App;
import com.vinuthana.vinvidya.models.CurrentAttendanceModel;
import com.vinuthana.vinvidya.models.CurrentHomeWorkModel;
import com.vinuthana.vinvidya.sqlitetables.CurrentAtndTable;
import com.vinuthana.vinvidya.sqlitetables.CurrentHomeWorkTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KISHAN on 08-09-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "studentmanagement";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper() {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        //Log.d("table", CREATE_STUDENT_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CurrentAtndTable.attendanceTable());
        db.execSQL(CurrentHomeWorkTable.homeWorkTable());
        /*db.execSQL(CURRENT_ATTENDANCE_TABLE);
        db.execSQL(PREVIOUS_ATTENDANCE_TABLE);
        db.execSQL(CURRENT_HOMEWORK_TABLE);
        db.execSQL(PREVIOUS_HOMEWORK_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CurrentAttendanceModel.TBL_CURRENT_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + CurrentHomeWorkModel.TBL_CURRENT_HOME_WORK);
        onCreate(db);
        /*db.execSQL("DROP TABLE IF EXISTS '" + CURRENT_ATTENDANCE_TABLE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + PREVIOUS_ATTENDANCE_TABLE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CURRENT_HOMEWORK_TABLE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + PREVIOUS_HOMEWORK_TABLE + "'");*/

    }
}
