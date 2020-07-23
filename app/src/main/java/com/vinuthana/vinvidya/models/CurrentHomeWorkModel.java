package com.vinuthana.vinvidya.models;

/**
 * Created by Administrator on 12/13/2017.
 */

public class CurrentHomeWorkModel {
    int id,studentId;
    String HomeWork,chapterName,clas,StudentName,Subject,date;

    /*Table tbl_current_homework*/
    public static final String TBL_CURRENT_HOME_WORK = "tbl_current_homework";

    /*Column names*/
    public static final String HOM_WRK_ID = "id";
    public static final String STUD_ID = "studentId";
    public static final String HOME_WORK = "HomeWork";
    public static final String CHAPTER_NAME = "chapterName";
    public static final String HMWRK_CLAS = "Class";
    public static final String STUD_NAME = "StudentName";
    public static final String SUBJECT = "Subject";
    public static final String HMWRK_DATE = "date";

    public CurrentHomeWorkModel() {
        id = 0;
        studentId = 0;
        HomeWork = "";
        chapterName = "";
        StudentName = "";
        Subject = "";
        date = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getHomeWork() {
        return HomeWork;
    }

    public void setHomeWork(String homeWork) {
        HomeWork = homeWork;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
