package com.vinuthana.vinvidya.fragments.examSectionFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExamSheduleRecycler;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CurrentExamSchedule extends Fragment {
    View view;
    TableLayout tblCurExmSchdlsGrid;
    RecyclerView recyclerViewExmSch;
    TextView tvCurExamScheduleName, tvCurExamScheduleClass, tvCurExamScheduleSection,
            tvCurExamScheduleExam, tvCurExamScheduleStudName;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus,strClass,strStudentnName;


    public CurrentExamSchedule() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        new GetPreviousExamSchedule().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initialisation() {
        recyclerViewExmSch = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmSch);
        tvCurExamScheduleName = (TextView) getActivity().findViewById(R.id.tvCurExamScheduleStudName);
        tvCurExamScheduleClass = (TextView) getActivity().findViewById(R.id.tvCurExamScheduleClass);
        tvCurExamScheduleExam = (TextView) getActivity().findViewById(R.id.tvCurExamScheduleExam);
        tvCurExamScheduleStudName = (TextView) getActivity().findViewById(R.id.tvCurExamScheduleStudName);


        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        // String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
     strClass = hashMap.get(StudentSPreference.STR_CLASS);
     strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        //tvCurAtndRollNo.setText(strRollNo);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_exam_schedule, container, false);
        allEvents();
        return view;
    }

    private void allEvents() {
    }

    private class GetPreviousExamSchedule extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Current Exam Schedule..");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)

        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examScheduleDisplay_Studentwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "GetPreviousExamSchedule,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousExamSchedule,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
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
                Log.e("doInBackground",e.toString());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ExamSheduleRecycler recyclerAdapter = new ExamSheduleRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewExmSch.setLayoutManager(layoutManager);
            recyclerViewExmSch.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExmSch.setAdapter(recyclerAdapter);
            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String strExam = jsonObject.getString("Exam");
                tvCurExamScheduleClass.setText(strClass);
                tvCurExamScheduleStudName.setText(strStudentnName);
                tvCurExamScheduleExam.setText(strExam);
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
