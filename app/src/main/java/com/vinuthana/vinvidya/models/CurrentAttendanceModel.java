package com.vinuthana.vinvidya.models;

/**
 * Created by Administrator on 12/13/2017.
 */

public class CurrentAttendanceModel {
    int id,studentId,RollNo;
    String Status,clas,StudentName,Day,date;
    public static final String TBL_CURRENT_ATTENDANCE = "tbl_current_attendance";

    /*Column names*/
    public static final String ID = "id";
    public static final String STUDENT_ID = "studentId";
    public static final String STATUS = "Status";
    public static final String ROLL_NO = "RollNo";
    public static final String STUDENT_NAME = "StudentName";
    public static final String DAY = "Day";
    public static final String DATE = "date";

    public CurrentAttendanceModel() {
        id = 0;
        RollNo = 0;
        studentId = 0;
        Status = "";
        clas = "";
        StudentName = "";
        Day = "";
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

    public int getRollNo() {
        return RollNo;
    }

    public void setRollNo(int rollNo) {
        RollNo = rollNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
