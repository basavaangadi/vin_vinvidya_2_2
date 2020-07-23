package com.vinuthana.vinvidya.sqlitetables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.vinuthana.vinvidya.models.CurrentAttendanceModel;
import com.vinuthana.vinvidya.utils.DatabaseManager;

/**
 * Created by Administrator on 12/14/2017.
 */

public class CurrentAtndTable {
    private CurrentAttendanceModel atndTable;

    public CurrentAtndTable() {
        atndTable = new CurrentAttendanceModel();
    }

    public static String attendanceTable() {
        return "CREATE TABLE " + CurrentAttendanceModel.TBL_CURRENT_ATTENDANCE + "("
                + CurrentAttendanceModel.ID + " INTEGER PRIMARY KEY, "
                + CurrentAttendanceModel.STUDENT_ID + " INTEGER, "
                + CurrentAttendanceModel.STATUS + " TEXT,"
                + CurrentAttendanceModel.ROLL_NO + " INTEGER,"
                + CurrentAttendanceModel.STUDENT_NAME + " TEXT,"
                + CurrentAttendanceModel.DAY + " TEXT,"
                + CurrentAttendanceModel.DATE + " TEXT )";
    }

    public int insertAtndTbl(CurrentAttendanceModel atndTable) {
        int atndTableId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrentAttendanceModel.ID, atndTable.getId());
        values.put(CurrentAttendanceModel.STUDENT_ID, atndTable.getStudentId());
        values.put(CurrentAttendanceModel.STATUS, atndTable.getStatus());
        values.put(CurrentAttendanceModel.ROLL_NO, atndTable.getRollNo());
        values.put(CurrentAttendanceModel.STUDENT_NAME, atndTable.getStudentName());
        values.put(CurrentAttendanceModel.DAY, atndTable.getDay());
        values.put(CurrentAttendanceModel.DATE, atndTable.getDate());

        atndTableId = (int) db.insert(CurrentAttendanceModel.TBL_CURRENT_ATTENDANCE, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return atndTableId;
    }
}
