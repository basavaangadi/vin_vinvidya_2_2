package com.vinuthana.vinvidya.fragments.examSectionFragments;


import android.app.ProgressDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExamSectionAdpater.ExmMarksRecyclerJV;
import com.vinuthana.vinvidya.adapters.ExmMarksRecylerGDGB;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentExamMarksGDGB extends Fragment {

    View view;
    TextView tvCurrentMarksClassGDGB,tvCurrentMarksStudentGDGB,tvCurrentMarksExamGDGB;
    TextView tvExmMrkTotalGDGB,tvExmMaxMrkTotal;
    RecyclerView recyclerViewExmMrkGDGB;
    JSONArray result;
    String strTotal;
    //BarChart chart;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus,strSchoolId;
    CardView crdViewCurExmMrk,crdViewTotalGradeGDGB,crdViewCurExmMrkBKBI,crdViewCurExmMrkGDGB;

    public CurrentExamMarksGDGB() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);
        initialisation();


    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
        }
    }*/

    public void initialisation() {
        tvCurrentMarksStudentGDGB = getActivity().findViewById(R.id.tvCurrentMarksStudentGDGB);
        tvCurrentMarksClassGDGB = getActivity().findViewById(R.id.tvCurrentMarksClassGDGB);
        tvCurrentMarksExamGDGB = getActivity().findViewById(R.id.tvCurrentMarksExamGDGB);
        tvExmMrkTotalGDGB=getActivity().findViewById(R.id.tvExmMrkTotalGDGB);
        crdViewTotalGradeGDGB=getActivity().findViewById(R.id.crdViewTotalGradeGDGB);
        crdViewCurExmMrkBKBI=getActivity().findViewById(R.id.crdViewCurExmMrkBKBI);
        recyclerViewExmMrkGDGB = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmMrkGDGB);
        crdViewCurExmMrkGDGB =  getActivity().findViewById(R.id.crdViewCurExmMrkGDGB);

            if(strSchoolId.equalsIgnoreCase("11")){
                crdViewCurExmMrkBKBI.setVisibility(view.VISIBLE);
                crdViewCurExmMrkGDGB.setVisibility(view.GONE);
            }else{
                crdViewCurExmMrkBKBI.setVisibility(view.GONE);
                crdViewCurExmMrkGDGB.setVisibility(view.VISIBLE);
            }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_exam_marks_gdgb, container, false);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strSchoolId = getActivity().getIntent().getExtras().getString("schoolId");
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            Log.e("oncreateView",e.toString());
        }


       // new GetCurrentExamMarks().execute();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        allEvents();

        new GetCurrentExamMarks().execute();
    }

    private void allEvents() {
    }

    private class GetCurrentExamMarks extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";
        String strTotalMarks = "",strMaxTotalMarks="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Current Exam Marks..");
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                if(strSchoolId.equalsIgnoreCase("11")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplay_Studentwise_BKBI));
                }else if(strSchoolId.equalsIgnoreCase("2")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplay_Studentwise_GDGB));
                }else if(strSchoolId.equalsIgnoreCase("5")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplay_Studentwise_HBBN));
                }else if(strSchoolId.equalsIgnoreCase("13")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplay_Studentwise_MNJT));
                }

                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "CurrentExamMarks1, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "CurrentExamMarks1, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                Double dblTotalMarks=inObject.getDouble(getString(R.string.key_Total));
                String strTotalGrade=inObject.getString(getString(R.string.key_TotalGrade));

                Double dblMaxTotalMarks=inObject.getDouble(getString(R.string.key_MaxTotal));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Double dblPercentage=dblTotalMarks/dblMaxTotalMarks;

                        if(!strTotalGrade.equalsIgnoreCase("-9999")){
                            tvExmMrkTotalGDGB.setText(strTotalGrade);
                            crdViewTotalGradeGDGB.setVisibility(view.VISIBLE);
                        }

                    }
                });
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
                //publishProgress(result);
            } catch (Exception e) {
                Log.e("doInBackground",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            if(strSchoolId.equalsIgnoreCase("11")){
                ExmMarksRecyclerJV recyclerAdapter = new ExmMarksRecyclerJV(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewExmMrkGDGB.setLayoutManager(layoutManager);
                recyclerViewExmMrkGDGB.setItemAnimator(new DefaultItemAnimator());
                recyclerViewExmMrkGDGB.setAdapter(recyclerAdapter);
            }else{
                ExmMarksRecylerGDGB recyclerAdapter = new ExmMarksRecylerGDGB(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewExmMrkGDGB.setLayoutManager(layoutManager);
                recyclerViewExmMrkGDGB.setItemAnimator(new DefaultItemAnimator());
                recyclerViewExmMrkGDGB.setAdapter(recyclerAdapter);
            }



            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString(getString(R.string.key_studentname));
                String strClass = jsonObject.getString(getString(R.string.key_class));
                String strExam = jsonObject.getString(getString(R.string.key_Exam));
                tvCurrentMarksClassGDGB.setText(strClass);
                tvCurrentMarksExamGDGB.setText(strExam);
                tvCurrentMarksStudentGDGB.setText(strStudentName);

            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }






}
