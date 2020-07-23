package com.vinuthana.vinvidya.fragments.dayToDayFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.AtndPeriodWiseRecyclerAdapter;
import com.vinuthana.vinvidya.adapters.AtndRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;


public class AttendanceByDate extends Fragment {
    EditText edtAttendanceByDate;
    Button btnAtndByDate;
    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    RecyclerView recyclerViewAtndByDt;
    CardView cwAtndByDate,cwAtndByDatePw;
    Calendar c = Calendar.getInstance();
    String strDate;
    View view;
    TextView tvAtndByDtStudentName, tvAtndByDtClass, tvAtndByDtSection, tvAtndByDtRollNo;
    TableLayout tblAttenByDtGrid;

    String strStudentId,strSchoolID;
    StudentSPreference studentSPreference;
    HashMap<String,String> hashMap = new HashMap<String, String>();
    JSONArray array;

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtAttendanceByDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    public AttendanceByDate() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation(view);
        allEvents();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(getActivity(), strStudentId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_by_date, container, false);

        return view;
    }

    public void initialisation(View view) {
        edtAttendanceByDate = (EditText) view.findViewById(R.id.edtAttendanceByDate);
        cwAtndByDate = (CardView) view.findViewById(R.id.cwAtndByDate);
        cwAtndByDatePw = (CardView) view.findViewById(R.id.cwAtndByDatePw);
        btnAtndByDate = (Button) view.findViewById(R.id.btnAtndByDate);
        tvAtndByDtStudentName = (TextView) view.findViewById(R.id.tvAtndByDtStudentName);
        tvAtndByDtClass = (TextView) view.findViewById(R.id.tvAtndByDtClass);
        recyclerViewAtndByDt = (RecyclerView) getActivity().findViewById(R.id.recyclerViewAtndByDt);
        tvAtndByDtRollNo = (TextView) getActivity().findViewById(R.id.tvAtndByDtRollNo);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass=hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName=hashMap.get(StudentSPreference.KEY_STUD_NAME);
        strSchoolID = hashMap.get(StudentSPreference.STR_SCHOOLID);
        if (strSchoolID.equals("6")) {
            cwAtndByDatePw.setVisibility(View.VISIBLE);
            cwAtndByDate.setVisibility(View.GONE);
        } else {
            cwAtndByDatePw.setVisibility(View.GONE);
            cwAtndByDate.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        tvAtndByDtRollNo.setText(strRollNo);
        tvAtndByDtClass.setText(strClass);
        tvAtndByDtStudentName.setText(strStudentnName);
    }

    public void allEvents() {
        edtAttendanceByDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtAttendanceByDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        btnAtndByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAttendanceByDate.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Attendance By Date can't be blank", Toast.LENGTH_LONG).show();
                } else {
                    strDate = edtAttendanceByDate.getText().toString();
                    new GetStudentPreviousAttendance().execute();
                    //Toast.makeText(getActivity(), edtAttendanceByDate.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class GetStudentPreviousAttendance extends AsyncTask<String, JSONArray, String> {
        ProgressDialog progressDialog;


        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("fetching the Attendance...");
            progressDialog.show();
            /*for(int i=array.length();i>=0;i--){
           if(android.os.Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.KITKAT){

                    array.remove(i);
                }
          }
          recyclerAdapter.notifyDataSetChanged();*/

        }

        @Override
        protected String doInBackground(String... params) {
            String strResult;
            try {
                JSONObject outObject = new JSONObject();
                if (strSchoolID.equals("6"))
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getperiodwiseStudentAttendanceByMonth));
                else
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_studentAttendanceByMonth));
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_StudentId), strStudentId);
                studAttendanceData.put(getString(R.string.key_Date), strDate);
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                Log.e("TAG", "GetStudentPreviousAttendance, doInBackground, outObject =" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetStudentPreviousAttendance, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", null);
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("doinbcgk",e.toString());
                strResult = "Exception " + e.toString();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            array=values[0];
            if (strSchoolID.equals("6")) {
                AtndPeriodWiseRecyclerAdapter recyclerAdapter = new AtndPeriodWiseRecyclerAdapter(array,getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewAtndByDt.setLayoutManager(layoutManager);
                recyclerViewAtndByDt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewAtndByDt.setAdapter(recyclerAdapter);
            } else {
                AtndRecyclerViewAdapter recyclerAdapter = new AtndRecyclerViewAdapter(array,getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewAtndByDt.setLayoutManager(layoutManager);
                recyclerViewAtndByDt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewAtndByDt.setAdapter(recyclerAdapter);
            }


            /*try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}