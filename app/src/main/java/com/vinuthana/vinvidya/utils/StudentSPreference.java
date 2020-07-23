package com.vinuthana.vinvidya.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 12/22/2017.
 */

public class StudentSPreference {
    public static final String ROLL_NUM = "rollNum";
    public static final String STR_CLASS = "class";
    public static final String STR_SCHOOLID = "SchoolId";
    public static final String STR_SCHOOL = "School";
    public static final String STUD_ID = "studId";
    public static final String STR_CLASS_ID = "ClassId";
    public static final String STR_ACADEMIC_YEAR_ID = "AcademicYearId";
    public static final String KEY_STUD_NAME = "studname";
    private static final String PREFE_NAME = "StudSession";
    SharedPreferences preferences, studentSP;
    SharedPreferences.Editor editor;
    Context context;
    public ArrayList<SharedPreferences.Editor> editorList = new ArrayList<SharedPreferences.Editor>();

    public StudentSPreference(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void createStudPref(ArrayList<HashMap<String, String>> spList) {

        for (int i = 0; i < spList.size(); i++) {

            HashMap<String, String> spHashmap = spList.get(i);

            studentSP = context.getSharedPreferences("Student_" + spHashmap.get("strStudentId"), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1;
            editor1 = studentSP.edit();

            editor1.putString(STUD_ID, spHashmap.get("strStudentId"));
            editor1.putString(KEY_STUD_NAME, spHashmap.get("strStudentname"));
            editor1.putString(ROLL_NUM, spHashmap.get("strRollNo"));
            editor1.putString(STR_CLASS, spHashmap.get("strClass"));
            editor1.putString(STR_SCHOOLID, spHashmap.get("strSchoolId"));
            editor1.putString(STR_SCHOOL, spHashmap.get("strSchool"));
            editor1.putString(STR_CLASS_ID, spHashmap.get("strClassId"));
            editor1.putString(STR_ACADEMIC_YEAR_ID, spHashmap.get("strAcademicID"));
            editor1.commit();
        }
    }

    public HashMap<String, String> getStudentDataFromSP(String studentID) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        studentSP = context.getSharedPreferences("Student_" + studentID, Context.MODE_PRIVATE);

        hashMap.put(STUD_ID, studentSP.getString(STUD_ID, ""));
        hashMap.put(KEY_STUD_NAME, studentSP.getString(KEY_STUD_NAME, ""));
        hashMap.put(ROLL_NUM, studentSP.getString(ROLL_NUM, ""));
        hashMap.put(STR_CLASS, studentSP.getString(STR_CLASS, ""));
        hashMap.put(STR_SCHOOLID, studentSP.getString(STR_SCHOOLID, ""));
        hashMap.put(STR_SCHOOL, studentSP.getString(STR_SCHOOL, ""));
        hashMap.put(STR_CLASS_ID, studentSP.getString(STR_CLASS_ID, ""));
        hashMap.put(STR_ACADEMIC_YEAR_ID, studentSP.getString(STR_ACADEMIC_YEAR_ID, ""));

        return hashMap;
    }
}
