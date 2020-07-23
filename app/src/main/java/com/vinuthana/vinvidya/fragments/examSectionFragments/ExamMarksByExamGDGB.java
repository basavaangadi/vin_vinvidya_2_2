package com.vinuthana.vinvidya.fragments.examSectionFragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExamSectionAdpater.ExmMarksRecyclerJV;
import com.vinuthana.vinvidya.adapters.ExmMarksRecylerGDGB;
import com.vinuthana.vinvidya.adapters.SpinnerDataAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class ExamMarksByExamGDGB extends Fragment {

    String strStudentId,strSchoolId,strExamId;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    Spinner spnrExamMarksByExamGDGB;
    JSONArray examArray= new JSONArray();
    Button btnExamMarksByExamGDGB;
    LinearLayout lynrLytExmByExamDetails,lnrLytStudentDetails,lynrLytExmMrksByExmDetailsGDGB;
    TextView tvExamMarksByExamStudentGDGB,tvExamMarksByExamClassGDGB,tvExamMarksByExamGDGB,tvExamMarksByExamSubjectGDGB;
    TextView tvExamMarksByExamGradeGDGB,tvExmMrkByExmTotalGDGB;
    RecyclerView recyclerViewExamMarksByExamGDGB;
    CardView crdViewExmMrksByExmTotalGradeGDGB,crdViewExamMarksByExamGDGB,crdViewExamMarksByExamBKBI;
    public ExamMarksByExamGDGB(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view=inflater.inflate(R.layout.fragment_exam_marks_by_exam_gdgb,container,false);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strSchoolId=getActivity().getIntent().getExtras().getString("schoolId");

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            Log.e("oncreateView",e.toString());
        }
         return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);
        initilation();
    }

    @Override
    public void onStart() {
        super.onStart();
        allEvents();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new GetExams().execute();
        }
    }

    public void allEvents(){

        spnrExamMarksByExamGDGB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) spnrExamMarksByExamGDGB.getSelectedView().findViewById(R.id.txt_exam);
                tmpView.setTextColor(Color.WHITE);
                //strExam = parent.getItemAtPosition(position).toString();

                strExamId = spnrExamMarksByExamGDGB.getSelectedItem().toString();
                String Tag="ByExamGDGB allEvents";

                Log.e(Tag, "" + strExamId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnExamMarksByExamGDGB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetExamMarksByExamGDGB().execute();
            }
        });
    }

    class GetExams extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the  Exam List..");
            progressDialog.show();
        }
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getExamName));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("ExamMarksByExamGDGB", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("ExamMarksByExamGDGB", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {

                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

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
            } catch (Exception ex) {
                Log.e("doInBackground",ex.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            SpinnerDataAdapter adapter = new SpinnerDataAdapter(values[0], getActivity());
            spnrExamMarksByExamGDGB.setPrompt("Choose Exam");
            spnrExamMarksByExamGDGB.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    public void initilation(){
        spnrExamMarksByExamGDGB  =getActivity().findViewById(R.id.spnrExamMarksByExamGDGB);
        btnExamMarksByExamGDGB  =getActivity().findViewById(R.id.btnExamMarksByExamGDGB);
        lynrLytExmByExamDetails =getActivity().findViewById(R.id.lynrLytExmByExamDetails);
        lnrLytStudentDetails  =getActivity().findViewById(R.id.lnrLytStudentDetails);
        lynrLytExmMrksByExmDetailsGDGB  =getActivity().findViewById(R.id.lynrLytExmMrksByExmDetailsGDGB);
        tvExamMarksByExamStudentGDGB  =getActivity().findViewById(R.id.tvExamMarksByExamStudentGDGB);
        tvExamMarksByExamClassGDGB  =getActivity().findViewById(R.id.tvExamMarksByExamClassGDGB);
        tvExamMarksByExamGDGB  =getActivity().findViewById(R.id.tvExamMarksByExamGDGB);
        tvExamMarksByExamSubjectGDGB  =getActivity().findViewById(R.id.tvExamMarksByExamSubjectGDGB);
        tvExamMarksByExamGradeGDGB  =getActivity().findViewById(R.id.tvExamMarksByExamGradeGDGB);
        recyclerViewExamMarksByExamGDGB  =getActivity().findViewById(R.id.recyclerViewExamMarksByExamGDGB);
        crdViewExmMrksByExmTotalGradeGDGB  =getActivity().findViewById(R.id.crdViewExmMrksByExmTotalGradeGDGB);
        crdViewExamMarksByExamBKBI  =getActivity().findViewById(R.id.crdViewExamMarksByExamBKBI);
        crdViewExamMarksByExamGDGB  =getActivity().findViewById(R.id.crdViewExamMarksByExamGDGB);
        tvExmMrkByExmTotalGDGB  =getActivity().findViewById(R.id.tvExmMrkByExmTotalGDGB);
        if(strSchoolId.equalsIgnoreCase("11")){
          crdViewExamMarksByExamBKBI.setVisibility(View.VISIBLE);
            crdViewExamMarksByExamGDGB.setVisibility(View.GONE);
        }else{
            crdViewExamMarksByExamBKBI.setVisibility(View.GONE);
            crdViewExamMarksByExamGDGB.setVisibility(View.VISIBLE);
        }
    }
    class  GetExamMarksByExamGDGB extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";
        String strTotalMarks = "",strMaxTotalMarks="",strStatus;
        JSONObject inObject= new JSONObject();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the  Exam Marks..");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            try {
                String Tag="ExamMarksByExamGDGB";
                JSONObject outObject = new JSONObject();
                if(strSchoolId.equalsIgnoreCase("11")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayExamwise_BKBI));
                }else if(strSchoolId.equalsIgnoreCase("2")){

                    if(strExamId.equalsIgnoreCase("31")||strExamId.equalsIgnoreCase("32")){
                        outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayTermExam_GDGB));

                    }else {
                        outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayExamwise_GDGB));
                    }
                }else if(strSchoolId.equalsIgnoreCase("5")){
                   /* if(strExamId.equalsIgnoreCase("31")||strExamId.equalsIgnoreCase("32")){
                        outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayTermExam_GDGB));

                    }else {*/
                        outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayExamwise_HBBN));
                    //}
                }



                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e(Tag, " , doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e(Tag, " , doInBackground, respText =" + respText);
                 inObject = new JSONObject(respText);

                strStatus = inObject.getString(getString(R.string.key_Status));
                JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                    /*lynrLytExmByExamDetails.setVisibility(View.VISIBLE);
                    lynrLytExmMrksByExmDetailsGDGB.setVisibility(View.VISIBLE);
                    crdViewExamMarksByExamGDGB.setVisibility(View.VISIBLE);*/
                    /*for(int i=0;i<resultArray.length();i++){
                        examArray.put(resultArray.getJSONObject(i));
                    }*/

                }


                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {


                    Double dblTotalMarks=inObject.getDouble(getString(R.string.key_Total));
                    String strTotalGrade=inObject.getString(getString(R.string.key_TotalGrade));

                    Double dblMaxTotalMarks=inObject.getDouble(getString(R.string.key_MaxTotal));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(strSchoolId.equalsIgnoreCase("2")||strSchoolId.equalsIgnoreCase("13")||strSchoolId.equalsIgnoreCase("5")){
                                lynrLytExmByExamDetails.setVisibility(View.VISIBLE);
                                lynrLytExmMrksByExmDetailsGDGB.setVisibility(View.VISIBLE);
                                crdViewExamMarksByExamGDGB.setVisibility(View.VISIBLE);

                                if(!strTotalGrade.equalsIgnoreCase("-9999")&&!strTotalGrade.equalsIgnoreCase("")){
                                    Double dblPercentage=dblTotalMarks/dblMaxTotalMarks;
                                    tvExmMrkByExmTotalGDGB.setText(strTotalGrade);
                                    crdViewExmMrksByExmTotalGradeGDGB.setVisibility(View.VISIBLE);

                                }
                            }


                        }
                    });



                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    lynrLytExmMrksByExmDetailsGDGB.setVisibility(View.GONE);
                                }
                            });
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
                recyclerViewExamMarksByExamGDGB.setLayoutManager(layoutManager);
                recyclerViewExamMarksByExamGDGB.setItemAnimator(new DefaultItemAnimator());
                recyclerViewExamMarksByExamGDGB.setAdapter(recyclerAdapter);
            }else{
                ExmMarksRecylerGDGB recyclerAdapter = new ExmMarksRecylerGDGB(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewExamMarksByExamGDGB.setLayoutManager(layoutManager);
                recyclerViewExamMarksByExamGDGB.setItemAnimator(new DefaultItemAnimator());
                recyclerViewExamMarksByExamGDGB.setAdapter(recyclerAdapter);
            }
           /* ExmMarksRecylerGDGB recyclerAdapter = new ExmMarksRecylerGDGB(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewExamMarksByExamGDGB.setLayoutManager(layoutManager);
            recyclerViewExamMarksByExamGDGB.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExamMarksByExamGDGB.setAdapter(recyclerAdapter);*/


            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString(getString(R.string.key_studentname));
                String strClass = jsonObject.getString(getString(R.string.key_class));
                String strExam = jsonObject.getString(getString(R.string.key_Exam));
                tvExamMarksByExamClassGDGB.setText(strClass);
                tvExamMarksByExamGDGB.setText(strExam);
                tvExamMarksByExamStudentGDGB.setText(strStudentName);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (strStudentName.length() > 1) {
                            lynrLytExmByExamDetails.setVisibility(View.VISIBLE);
                            lnrLytStudentDetails.setVisibility(View.VISIBLE);
                        }

                    }
                });

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
