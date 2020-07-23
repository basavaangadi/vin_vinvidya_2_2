package com.vinuthana.vinvidya.sqlitetables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.vinuthana.vinvidya.models.CurrentAttendanceModel;
import com.vinuthana.vinvidya.models.CurrentHomeWorkModel;
import com.vinuthana.vinvidya.utils.DatabaseManager;

/**
 * Created by Administrator on 12/15/2017.
 */

public class CurrentHomeWorkTable {
    private CurrentHomeWorkModel homeWorkModel;

    public CurrentHomeWorkTable() {
        homeWorkModel = new CurrentHomeWorkModel();
    }

    public static String homeWorkTable() {
        return "CREATE TABLE " + CurrentHomeWorkModel.TBL_CURRENT_HOME_WORK + "("
                + CurrentHomeWorkModel.HOM_WRK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CurrentHomeWorkModel.STUD_ID + " INTEGER, " + CurrentHomeWorkModel.HOME_WORK
                + " TEXT, " + CurrentHomeWorkModel.CHAPTER_NAME + " TEXT,"
                + CurrentHomeWorkModel.HMWRK_CLAS + " TEXT," + CurrentHomeWorkModel.STUD_NAME
                + " TEXT," + CurrentHomeWorkModel.SUBJECT + " TEXT," + CurrentHomeWorkModel.HMWRK_DATE
                + " TEXT );";
    }

    public int insertHmWkTbl(CurrentHomeWorkModel homeWorkModel) {
        int hmwrkId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrentHomeWorkModel.HOM_WRK_ID,homeWorkModel.getId());
        values.put(CurrentHomeWorkModel.STUD_ID,homeWorkModel.getStudentId());
        values.put(CurrentHomeWorkModel.HOME_WORK,homeWorkModel.getHomeWork());
        values.put(CurrentHomeWorkModel.CHAPTER_NAME,homeWorkModel.getChapterName());
        values.put(CurrentHomeWorkModel.HMWRK_CLAS,homeWorkModel.getChapterName());
        values.put(CurrentHomeWorkModel.STUD_NAME,homeWorkModel.getStudentName());
        values.put(CurrentHomeWorkModel.SUBJECT,homeWorkModel.getSubject());
        values.put(CurrentHomeWorkModel.HMWRK_DATE,homeWorkModel.getDate());
        hmwrkId = (int) db.insert(CurrentHomeWorkModel.TBL_CURRENT_HOME_WORK,null, values);
        DatabaseManager.getInstance().closeDatabase();
        return hmwrkId;
    }
}
