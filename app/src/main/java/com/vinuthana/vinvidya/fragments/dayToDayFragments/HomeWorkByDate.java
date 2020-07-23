package com.vinuthana.vinvidya.fragments.dayToDayFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.vinuthana.vinvidya.activities.otheractivities.SubjectSyllabusActivity;
import com.vinuthana.vinvidya.adapters.HmWrkRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class HomeWorkByDate extends Fragment {
    EditText edtHomeWorkByDate,edtHomeWorkByToDate;
    Button btnHWByDate;
    JSONArray homeworkArray=new JSONArray();
    TextView tvHmwrTblRowSubject, tvHmwrTblRowHmWrk;
    private int mYear, mMonth, mDay, toYear, toMonth, toDay;
    DatePickerDialog datePickerDialogFromDate,datePickerDialogToDate;
    Calendar fromDateCalender = Calendar.getInstance();
    Calendar toDateCalender = Calendar.getInstance();
    String strHmWrkFromDate, strHmWrkToDate;

    View view;
    TableLayout tblHwByDtGrid;
    RecyclerView recyclerViewHmWrkByDt;
    HmWrkRecyclerViewAdapter recyclerAdapter;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId,strSchoolId;

    TextView tvHWByDtStudentName, tvHWByDtClass, tvHWByDtRollNo;
    DatePickerDialog.OnDateSetListener fromDatelistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtHomeWorkByDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);

            datePickerDialogFromDate.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener toDateListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtHomeWorkByToDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);

            datePickerDialogToDate.dismiss();
        }
    };
    public HomeWorkByDate() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mYear = fromDateCalender.get(Calendar.YEAR);
        mMonth = fromDateCalender.get(Calendar.MONTH);
        mDay = fromDateCalender.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFromDate = new DatePickerDialog(getActivity(), fromDatelistner, mYear, mMonth, mDay);

        toYear = toDateCalender.get(Calendar.YEAR);
        toMonth = toDateCalender.get(Calendar.MONTH);
        toDay = toDateCalender.get(Calendar.DAY_OF_MONTH);
        datePickerDialogToDate = new DatePickerDialog(getActivity(), toDateListner, toYear, toMonth, toDay);
        initialisation();
        allEvent();
    }

    private void initialisation() {
        edtHomeWorkByDate = (EditText) view.findViewById(R.id.edtHomeWorkByDate);
        edtHomeWorkByToDate = (EditText) view.findViewById(R.id.edtHomeWorkByToDate);
        btnHWByDate = (Button) view.findViewById(R.id.btnHWByDate);
        tvHWByDtStudentName = (TextView) view.findViewById(R.id.tvHWByDtStudentName);
        tvHWByDtClass = (TextView) view.findViewById(R.id.tvHWByDtClass);
        tvHWByDtRollNo = (TextView) view.findViewById(R.id.tvHWByDtRollNo);
        strHmWrkFromDate = edtHomeWorkByDate.getText().toString();
        recyclerViewHmWrkByDt = (RecyclerView) view.findViewById(R.id.recyclerViewHmWrkByDt);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strSchoolId = getActivity().getIntent().getExtras().getString("schoolId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);

        tvHWByDtRollNo.setText(strRollNo);
        tvHWByDtClass.setText(strClass);
        tvHWByDtStudentName.setText(strStudentnName);


        recyclerAdapter = new HmWrkRecyclerViewAdapter(homeworkArray, getActivity(),strSchoolId);

        recyclerAdapter.setOnButtonClickListener(new HmWrkRecyclerViewAdapter.OnHomeworkClickListener() {
            @Override
            public void getSyllabus(JSONObject homeworkData, int position, String strSubjectId) {
                android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to view the Syllabus for this subject?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), SubjectSyllabusActivity.class);
                        Bundle bundle= new Bundle();
                        try{

                            String  strSubjectId=homeworkData.getString(getString(R.string.key_SubjectId));
                            String  strSubject=homeworkData.getString(getString(R.string.key_Subject));
                            bundle.putString("SubjectId",strSubjectId);
                            bundle.putString("Class",strClass);
                            bundle.putString("Subject",strSubject);
                            bundle.putString("StudentId",strStudentId);
                            /*bundle.putString("StudentList",studentArrayList.toString());
                            bundle.putString("ClassId",strClassId);*/
                            intent.putExtras(bundle);
                            startActivity(intent);


                        }catch(Exception e){
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                android.app.AlertDialog dailog= builder.create();
                dailog.show();
            }

            @Override
            public void onDelete(int position, String noticeId) {

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHmWrkByDt.setLayoutManager(layoutManager);
        recyclerViewHmWrkByDt.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHmWrkByDt.setAdapter(recyclerAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_work_by_date, container, false);
        return view;
    }

    private void allEvent() {
        edtHomeWorkByDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtHomeWorkByDate) {
                    datePickerDialogFromDate.show();

                    return true;
                }
                return false;
            }
        });
        edtHomeWorkByToDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtHomeWorkByToDate) {
                    datePickerDialogToDate.show();

                    return true;
                }
                return false;
            }
        });
        btnHWByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strHmWrkFromDate = edtHomeWorkByDate.getText().toString();
                strHmWrkToDate = edtHomeWorkByToDate.getText().toString();
                if (strHmWrkFromDate.equalsIgnoreCase("") || strHmWrkToDate.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Home Work By Date can't be blank", Toast.LENGTH_LONG).show();
                } else {


                    try {

                       // SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                        String myFormat = "dd-MM-yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                        Date fromDate = sdf.parse(strHmWrkFromDate);
                        Date toDate = sdf.parse(strHmWrkToDate);
                        if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        } else {

                            new GetPreviousHomeWork().execute();
                        }
                    } catch (Exception e) {
                        Log.e("btnOnclick", e.toString());
                    }
                }

            }
        });
    }

    private class GetPreviousHomeWork extends AsyncTask<String, JSONArray, String> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("fetching the homework");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String strResult;
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_homeworkDisplayBydate));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_StudentId), strStudentId);
                homeworkData.put(getString(R.string.key_Date), strHmWrkFromDate);
                homeworkData.put(getString(R.string.key_toDate), strHmWrkToDate);
                outObject.put("homeworkData", homeworkData);
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, outObject =" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    for(int i=0;i<resultArray.length();i++){
                        homeworkArray.put(resultArray.getJSONObject(i));
                    }
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
            //addRowToTable(values[0]);
             /*recyclerAdapter = new HmWrkRecyclerViewAdapter(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewHmWrkByDt.setLayoutManager(layoutManager);
            recyclerViewHmWrkByDt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewHmWrkByDt.setAdapter(recyclerAdapter);*/

            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());

                // This gets you the first (zero indexed) element of the above array.
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString("StudentName");
                String strClass = jsonObject.getString("Class");
                //String strSection = jsonObject.getString("Section");
                //String strExam = jsonObject.getString("RollNo");
                //String strClasSection = strClas + " " + strSection;
                tvHWByDtStudentName.setText(strStudentName);
                tvHWByDtClass.setText(strClass);
            } catch (Exception e) {

            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}
