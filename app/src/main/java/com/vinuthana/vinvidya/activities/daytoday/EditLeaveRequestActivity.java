package com.vinuthana.vinvidya.activities.daytoday;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditLeaveRequestActivity extends AppCompatActivity {

    EditText edt_editLeave_from_date,edt_editLeave_to_date,edtEdtLeaveDescrption;
    Button btnEditStudentLeaveDetails;
    int count=1;
    JSONArray homeworkArray=new JSONArray();
    TextView tvEdtLeaveTitle, tvHmwrTblRowHmWrk,tv_editLeave_from_date;
    private int mYear, mMonth, mDay, toYear, toMonth, toDay;
    DatePickerDialog datePickerDialogFromDate,datePickerDialogToDate;
    Calendar fromDateCalender = Calendar.getInstance();
    Calendar toDateCalender = Calendar.getInstance();
    String strReasonTitleId,strRollNo,strCurrentData,strResultId;
    String strIsEditable,strDescription,strClassId,strClass,strFromDate,strToDate,strStudentName;
    String strStudentLeaveRequestId,strReasonTitle,strStudentId,strSchoolId,strAcademicyearId;

    TextView tvHWByDtStudentName, tvHWByDtClass, tvHWByDtRollNo;
    DatePickerDialog.OnDateSetListener fromDatelistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edt_editLeave_from_date.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);

            datePickerDialogFromDate.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener toDateListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edt_editLeave_to_date.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);

            datePickerDialogToDate.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_leave_request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Student Leave Details");
        Bundle bundle = getIntent().getExtras();
        strCurrentData = bundle.getString("leaveRequestData");

        mYear = fromDateCalender.get(Calendar.YEAR);
        mMonth = fromDateCalender.get(Calendar.MONTH);
        mDay = fromDateCalender.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFromDate = new DatePickerDialog(EditLeaveRequestActivity.this, fromDatelistner, mYear, mMonth, mDay);
        datePickerDialogFromDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        toYear = toDateCalender.get(Calendar.YEAR);
        toMonth = toDateCalender.get(Calendar.MONTH);
        toDay = toDateCalender.get(Calendar.DAY_OF_MONTH);
        datePickerDialogToDate = new DatePickerDialog(EditLeaveRequestActivity.this, toDateListner, toYear, toMonth, toDay);
        datePickerDialogToDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        initialisation();
        allEvents();
        setValues();
    }
    public void initialisation() {
        tvEdtLeaveTitle = findViewById(R.id.tvEdtLeaveTitle);
        edtEdtLeaveDescrption = findViewById(R.id.edtEdtLeaveDescrption);
        edt_editLeave_from_date = findViewById(R.id.edt_editLeave_from_date);
        tv_editLeave_from_date=findViewById(R.id.tv_editLeave_from_date);

        edt_editLeave_to_date=findViewById(R.id.edt_editLeave_to_date);

        btnEditStudentLeaveDetails = findViewById(R.id.btnEditStudentLeaveDetails);



    }
    public void setValues(){

        try{

            JSONObject object = new JSONObject(strCurrentData);


            strReasonTitleId = object.getString(getString(R.string.key_ReasonTitleId));

            strRollNo = object.getString(getString(R.string.key_RollNo));

            strSchoolId = object.getString(getString(R.string.key_SchoolId));

            strAcademicyearId = object.getString(getString(R.string.key_AcademicyearId));

            strIsEditable = object.getString(getString(R.string.key_IsEditable));

            strDescription = object.getString(getString(R.string.key_Description));
            strClassId = object.getString(getString(R.string.key_ClassId));
            strClass = object.getString(getString(R.string.key_Clas));
            strFromDate = object.getString(getString(R.string.key_FromDate));
            strToDate = object.getString(getString(R.string.key_ToDate));
            strStudentName = object.getString(getString(R.string.key_StudentName));
            strReasonTitle = object.getString(getString(R.string.key_ReasonTitle));
            strStudentLeaveRequestId = object.getString(getString(R.string.key_StudentLeaveRequestId));
            strStudentId = object.getString(getString(R.string.key_StudentId));
            edt_editLeave_to_date.setText(strToDate);

            if(strIsEditable.equalsIgnoreCase("2")){
                edt_editLeave_from_date.setText(strFromDate);
                tv_editLeave_from_date.setVisibility(View.GONE);
               }else{
                tv_editLeave_from_date.setText(strFromDate);
                edt_editLeave_from_date.setVisibility(View.GONE);
            }

            edtEdtLeaveDescrption.setText(strDescription);


            edt_editLeave_to_date.setText(strToDate);
            tvEdtLeaveTitle.setText(strReasonTitle);

        }catch (Exception e){
            count=0;
            Toast toast=  Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void allEvents() {


        edt_editLeave_from_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edt_editLeave_from_date) {
                    datePickerDialogFromDate.show();
                    return true;
                }
                return false;
            }
        });

        edt_editLeave_to_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edt_editLeave_to_date) {
                    datePickerDialogToDate.show();
                    return true;
                }
                return false;
            }
        });


        btnEditStudentLeaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    if (strIsEditable.equalsIgnoreCase("2")) {
                        strFromDate = edt_editLeave_from_date.getText().toString();
                    }
                    strToDate = edt_editLeave_to_date.getText().toString();
                   /* strFromDate="10-5-2019";
                    strToDate="09-05-2019";*/
                    //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy ");
                    Date fromDate = new Date();
                    Date toDate = new Date();
                        toDate = sdf.parse(strToDate);
                        fromDate = sdf.parse(strFromDate);

                        if (fromDate.after(toDate)) {
                            Toast toast = Toast.makeText(EditLeaveRequestActivity.this, "From date is greater then To date", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (count == 0) {
                            Toast toast = Toast.makeText(EditLeaveRequestActivity.this, "Something went wrong while fetching the Leave details", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else if(strToDate.length()<=1||strFromDate.length()<=1||strDescription.length()<=3||strReasonTitle.length()<=1){
                            Toast toast = Toast.makeText(EditLeaveRequestActivity.this, "All fileds are Manadatory", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                    }
                        else {
                            new EditStudentLeaveWithDeatils().execute();
                        }
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(EditLeaveRequestActivity.this, "Something went wrong while fetching the Leave details", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }


                }
        });
        }






    private void showAlert(String alertTitle, String alertMessage) {

        EditLeaveRequestActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditLeaveRequestActivity.this);
                builder.setMessage(alertMessage);
                builder.setTitle(alertTitle);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*if(strResultId.equalsIgnoreCase("-2")){
                            Intent intent= new Intent(EditLeaveRequestActivity.this,AttendanceActivity.class);
                            startActivity(intent);
                            finish();
                        }*/
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }


    class EditStudentLeaveWithDeatils extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String url = AD.url.base_url + "studentAttendanceOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(EditLeaveRequestActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait, editing the homework..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {

                Date date = new Date();
                String myFormat = "dd-MM-yyyy hh:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String addedDate = sdf.format(date.getTime());
                outObject.put("OperationName", getString(R.string.web_editStudentLeaveRequest));
                JSONObject LeaveDetailData = new JSONObject();
                LeaveDetailData.put(getString(R.string.key_StudentLeaveRequestId), strStudentLeaveRequestId);
                LeaveDetailData.put(getString(R.string.key_StudentId), strStudentId);
                LeaveDetailData.put(getString(R.string.key_SchoolId), strSchoolId);
                LeaveDetailData.put(getString(R.string.key_AcademicYearId), strAcademicyearId);
                LeaveDetailData.put(getString(R.string.key_ReasonTitleId), strReasonTitleId);
                LeaveDetailData.put(getString(R.string.key_ClassId), strClassId);
                if(strIsEditable.equalsIgnoreCase("2")){
                  strFromDate=  edt_editLeave_from_date.getText().toString();
                }
                strToDate=edt_editLeave_to_date.getText().toString();
                LeaveDetailData.put(getString(R.string.key_FromDate), strFromDate);
                LeaveDetailData.put(getString(R.string.key_ToDate), strToDate);
                String  strOtherReason;
                if(strReasonTitleId.equalsIgnoreCase("1")){
                   String base64ReasonTitle= StringUtil.textToBase64(strReasonTitle);
                   strOtherReason =base64ReasonTitle;
                }else{
                    strOtherReason="9999";
                }
                strDescription=edtEdtLeaveDescrption.getText().toString();
                String base64Description= StringUtil.textToBase64(strDescription);
                LeaveDetailData.put(getString(R.string.key_OtherReasonTitle), strOtherReason);
                LeaveDetailData.put(getString(R.string.key_Description), base64Description);
                outObject.put( "studAttendanceData",LeaveDetailData);
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, outObject =" + outObject);

                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage = inObject.getString(getString(R.string.key_Message));
                strResultId = inObject.getString(getString(R.string.key_resultId));
                if(strResultId.equals("-2")||strResultId.equals("-1"))
                showAlert(strStatus,strMessage);
                else
                    showAlert("Error","Something went worng while editing the leave");

            } catch (Exception e) {
                EditLeaveRequestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast= Toast.makeText( EditLeaveRequestActivity.this,"Error occurred while editing",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
