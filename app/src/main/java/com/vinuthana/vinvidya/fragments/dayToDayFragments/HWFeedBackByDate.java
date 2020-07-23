package com.vinuthana.vinvidya.fragments.dayToDayFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.HwrkFbRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class HWFeedBackByDate extends Fragment {
    EditText edtHwFeedbackByDate,edtHwFeedbackToDate;
    Button btnhwfeedbackByDate;
    DatePickerDialog datePickerDialogFromDate,datePickerDialogToDate;
    Calendar fromDateCalender = Calendar.getInstance();
    Calendar toDateCalender = Calendar.getInstance();
    String strFromDate,strToDate;
    View view;
    RecyclerView recyclerViewHmWrkFdByDt;
    TextView tvHWFbByDtStudName, tvHWFbByDtClass,tvHWFbByDtRollNo;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId;


    TableLayout tblHwFdByDtGrid;
    DatePickerDialog.OnDateSetListener fromDatelistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtHwFeedbackByDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFromDate.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener toDatelistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtHwFeedbackToDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogToDate.dismiss();
        }
    };

    private int mYear, mMonth, mDay,toYear,toDay,toMonth;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        allEvents();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initialisation() {
        edtHwFeedbackByDate = (EditText) view.findViewById(R.id.edtHwFeedbackByDate);
        edtHwFeedbackToDate = (EditText) view.findViewById(R.id.edtHwFeedbackToDate);
        btnhwfeedbackByDate = (Button) view.findViewById(R.id.btnhwfeedbackByDate);
        recyclerViewHmWrkFdByDt = (RecyclerView) getActivity().findViewById(R.id.recyclerViewHmWrkFdByDt);
        tvHWFbByDtStudName = (TextView) getActivity().findViewById(R.id.tvHWFbByDtStudName);
        tvHWFbByDtRollNo = (TextView) getActivity().findViewById(R.id.tvHWFbByDtRollNo);
        tvHWFbByDtClass = (TextView) getActivity().findViewById(R.id.tvHWFbByDtClass);
        //tblHwFdByDtGrid = (TableLayout) view.findViewById(R.id.tblHwFdByDtGrid);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("initialisation",e.toString());
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        tvHWFbByDtRollNo.setText(strRollNo);
        tvHWFbByDtClass.setText(strClass);
        tvHWFbByDtStudName.setText(strStudentnName);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hwfeed_back_by_date, container, false);

        mYear = fromDateCalender.get(Calendar.YEAR);
        mMonth = fromDateCalender.get(Calendar.MONTH);
        mDay = fromDateCalender.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFromDate = new DatePickerDialog(getActivity(), fromDatelistner, mYear, mMonth, mDay);

        toYear = toDateCalender.get(Calendar.YEAR);
        toMonth = toDateCalender.get(Calendar.MONTH);
        toDay = toDateCalender.get(Calendar.DAY_OF_MONTH);
        datePickerDialogToDate = new DatePickerDialog(getActivity(), toDatelistner, toYear, toMonth, toDay);

        return view;
    }

    private void allEvents() {
        edtHwFeedbackByDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtHwFeedbackByDate) {
                    datePickerDialogFromDate.show();
                    return true;
                }
                return false;
            }
        });
        edtHwFeedbackToDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtHwFeedbackToDate) {
                    datePickerDialogToDate.show();
                    return true;
                }
                return false;
            }
        });

        btnhwfeedbackByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFromDate = edtHwFeedbackByDate.getText().toString();
                strToDate = edtHwFeedbackToDate.getText().toString();
                if (strFromDate.equals("")||strToDate.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Home Work Feedback By Date can't be blank", Toast.LENGTH_LONG).show();
                }else {


                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                        Date fromDate = sdf.parse(strFromDate);
                        Date toDate = sdf.parse(strToDate);
                        if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        } else {

                            new GetHomeWorkFbByDate().execute();

                        }
                    } catch (Exception e) {
                        Log.e("btnOnclick", e.toString());
                    }
                }



            }
        });
    }



    public class GetHomeWorkFbByDate extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;

        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Home Work Feedback...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", "homeworkFeedbackDateWise");
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_StudentId), strStudentId);
                homeworkData.put(getString(R.string.key_Date), strFromDate);
                homeworkData.put(getString(R.string.key_toDate), strToDate);
                outObject.put(getString(R.string.key_homeworkData),homeworkData);
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, respText =" + respText);
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
                Log.e("doinbackground",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            try {
                HwrkFbRecyclerViewAdapter adapter = new HwrkFbRecyclerViewAdapter(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewHmWrkFdByDt.setLayoutManager(layoutManager);
                recyclerViewHmWrkFdByDt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewHmWrkFdByDt.setAdapter(adapter);

                /*JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString("StudentName");
                String strClass = jsonObject.getString("Class");
                tvHWFbByDtStudName.setText(strStudentName);
                tvHWFbByDtClass.setText(strClass);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
