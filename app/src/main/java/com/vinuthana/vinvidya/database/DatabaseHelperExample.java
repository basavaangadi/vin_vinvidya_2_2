package com.vinuthana.vinvidya.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 12/14/2017.
 */

public class DatabaseHelperExample extends SQLiteOpenHelper {

    private static  String DBNAME="vinvidya.db";
    private static int VERSION = 1;
    //Table names
    private static String ATTENDANCE_TABLE = "tbl_attendance";

    //Common fields
    public static String ID = "id";

    //ATTENDANCE_TABLE fields
    public static String STUDENT_NAME = "student_name";
    public static String A_STATUS = "a_status";

    //Create tables
    //Create attendance table
    private static String CREATE_TABLE_ATTENDANCE ="CREATE TABLE IF NOT EXIST(" +
            ID + "integer primary key autoincrement, " +
            STUDENT_NAME + "text, "+
            A_STATUS + "text)";


    public DatabaseHelperExample(Context context) {
        super(context, DBNAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ATTENDANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertAttendance(ArrayList<HashMap<String,String>> hashMaps) {
        SQLiteDatabase db =  this.getWritableDatabase();
        for (int i = 0; i<hashMaps.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(STUDENT_NAME, hashMaps.get(i).get(STUDENT_NAME));
            values.put(A_STATUS, hashMaps.get(i).get(A_STATUS));
            db.insert(ATTENDANCE_TABLE, null, values );
        }
        db.close();
    }
}
