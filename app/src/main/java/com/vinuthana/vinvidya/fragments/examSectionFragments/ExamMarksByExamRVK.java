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
import android.view.Gravity;
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
import com.vinuthana.vinvidya.adapters.ExmMarksRecycler;
import com.vinuthana.vinvidya.adapters.ExmMarksRecylerGDGB;
import com.vinuthana.vinvidya.adapters.SpinnerDataAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamMarksByExamRVK extends Fragment {
    String strStudentId,strSchoolId,strExamId;


    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    Spinner spnrExamMarksByExamRVK;
    Button btnExamMarksByExamRVK;
    LinearLayout lynrLytExamMarksByExamDetails, lynrLytStudentDetailsRVK,lynrLytExamMarksByExamGradeDetails;
    TextView tvExamMarksByExamStudentRVK,tvExamMarksByExamClassRVK, tvExamMarksByExamExamRVK;
    JSONArray examArray= new JSONArray();

    TextView tvExmByExmPercentageRVK,tvExamMarksByExamExmMrkTotalRVK, tvExmMarksByExmGradeRVK,tvExamMarksByExamExmMaxMrkTotalRVK;
    RecyclerView recyclerViewExamMarksByExamExmMrkRVK;
    ExmMarksRecycler recyclerAdapter;
    //ExmMarksRecylerGDGB recyclerAdapter;
    CardView crdViewExamMarksByExamTotalGradeRVK,crdExmByExmPercentageDetailsRVK,crdExmByExmGradeDetailsRVK;



    public ExamMarksByExamRVK() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_exam_marks_by_exam_rvk, container, false);
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
        initialisation();
        allEvents();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new GetExams().execute();
        }
    }
    public void initialisation(){
        spnrExamMarksByExamRVK =getActivity().findViewById(R.id.spnrExamMarksByExamRVK);
        lynrLytStudentDetailsRVK =getActivity().findViewById(R.id.lynrLytStudentDetailsRVK);
        lynrLytExamMarksByExamGradeDetails =getActivity().findViewById(R.id.lynrLytExamMarksByExamGradeDetails);
        tvExamMarksByExamStudentRVK =getActivity().findViewById(R.id.tvExamMarksByExamStudentRVK);
        tvExamMarksByExamClassRVK =getActivity().findViewById(R.id.tvExamMarksByExamClassRVK);
        tvExamMarksByExamExamRVK =getActivity().findViewById(R.id.tvExamMarksByExamExamRVK);
        btnExamMarksByExamRVK =getActivity().findViewById(R.id.btnExamMarksByExamRVK);
        lynrLytExamMarksByExamDetails =getActivity().findViewById(R.id.lynrLytExamMarksByExamDetails);
        recyclerViewExamMarksByExamExmMrkRVK =getActivity().findViewById(R.id.recyclerViewExamMarksByExamExmMrkRVK);
        tvExamMarksByExamExmMrkTotalRVK =getActivity().findViewById(R.id.tvExamMarksByExamExmMrkTotalRVK);
        tvExmByExmPercentageRVK =getActivity().findViewById(R.id.tvExmByExmPercentageRVK);
        tvExmMarksByExmGradeRVK =getActivity().findViewById(R.id.tvExmMarksByExmGradeRVK);
        tvExamMarksByExamExmMaxMrkTotalRVK =getActivity().findViewById(R.id.tvExamMarksByExamExmMaxMrkTotalRVK);

     initExamRecylerAdapter();

    }

    public void initExamRecylerAdapter(){
         recyclerAdapter = new ExmMarksRecycler(examArray, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewExamMarksByExamExmMrkRVK.setLayoutManager(layoutManager);
        recyclerViewExamMarksByExamExmMrkRVK.setItemAnimator(new DefaultItemAnimator());
        recyclerViewExamMarksByExamExmMrkRVK.setAdapter(recyclerAdapter);
    }

    public void allEvents(){
        spnrExamMarksByExamRVK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExamMarksByExamRVK.getSelectedView().findViewById(R.id.txt_exam);
                tmpView.setTextColor(Color.WHITE);

                strExamId = spnrExamMarksByExamRVK.getSelectedItem().toString();

                String Tag="ByExamRVK allEvents";

                Log.e(Tag, "" + strExamId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnExamMarksByExamRVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetExamMarksByExamRVK().execute();
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
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("ExamMarksByExamRVK", "outObject =" + outObject.toString());
                Log.e("ExamMarksByExamRVK", "responseText is =" + responseText);
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
            spnrExamMarksByExamRVK.setPrompt("Choose Exam");
            spnrExamMarksByExamRVK.setAdapter(adapter);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetExamMarksByExamRVK extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "examsectionOperation.jsp";
        String strTotalMarks = "",strMaxTotalMarks="",strStatus,respText;
        JSONObject inObject=new JSONObject();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the  Exam Marks..");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = 0; i < examArray.length(); i++) {
                    examArray.remove(i);
                }
            }else{
                examArray=new JSONArray();
            }
        }
        @Override
        protected Void doInBackground(String... strings) {
            try {


                String Tag="ExamMarksByExamRVK";
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayExamwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), strExamId);

                outObject.put(getString(R.string.key_examData), examData);
                Log.e(Tag, " , doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                 respText = response.getServerResopnse(url, outObject.toString());
                Log.e(Tag, " , doInBackground, respText =" + respText);
                 inObject = new JSONObject(respText);
                JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));

                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    for(int i=0;i<resultArray.length();i++){
                        examArray.put(resultArray.getJSONObject(i));
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
                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   // new GetExamMarksByExamRVK().execute();
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

            /*ExmMarksRecycler recyclerAdapter = new ExmMarksRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewExamMarksByExamExmMrkRVK.setLayoutManager(layoutManager);
            recyclerViewExamMarksByExamExmMrkRVK.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExamMarksByExamExmMrkRVK.setAdapter(recyclerAdapter);
            Log.e("inUpdateBfrTry",values[0].toString());

            try {
                JSONArray jsonArray = values[0];
                Log.e("inUpdateAftrTry",values[0].toString());


            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }*/
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           /* publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
            publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));*/
            try{
                JSONObject object= examArray.getJSONObject(1);



                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {

                    Double dblTotalMarks=inObject.getDouble(getString(R.string.key_Total));
                    String strTotalGrade=inObject.getString(getString(R.string.key_TotalGrade));
                    String strTotalMarks=dblTotalMarks.toString();
                    int dblMaxTotalMarks=inObject.getInt(getString(R.string.key_MaxTotal));
                    String strMaxTotalMarks=String.valueOf(dblMaxTotalMarks);

                    String strPercentage=inObject.getString(getString(R.string.key_Percentage));

                    StringBuilder builderPercentage = new StringBuilder(100);
                    builderPercentage.append(strPercentage);
                    builderPercentage.append("%");
                    JSONObject jsonObject = examArray.getJSONObject(1);
                    String strStudentName = jsonObject.getString(getString(R.string.key_studentname));
                    String strClass = jsonObject.getString(getString(R.string.key_class));
                    String strExam = jsonObject.getString(getString(R.string.key_Exam));
                    tvExamMarksByExamClassRVK.setText(strClass);
                    tvExamMarksByExamExamRVK.setText(strExam);
                    tvExamMarksByExamStudentRVK.setText(strStudentName);
                    if (strStudentName.length() > 1) {
                        lynrLytExamMarksByExamDetails.setVisibility(View.VISIBLE);
                        lynrLytStudentDetailsRVK.setVisibility(View.VISIBLE);
                        recyclerViewExamMarksByExamExmMrkRVK.setVisibility(View.VISIBLE);
                        lynrLytExamMarksByExamGradeDetails.setVisibility(View.VISIBLE);
                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if (!strTotalGrade.equalsIgnoreCase("")) {
                                tvExamMarksByExamExmMrkTotalRVK.setText(strTotalMarks);
                                tvExamMarksByExamExmMaxMrkTotalRVK.setText(strMaxTotalMarks);
                                tvExmMarksByExmGradeRVK.setText(strTotalGrade);
                             if (!(strPercentage.equalsIgnoreCase("NA")) || (strPercentage.equalsIgnoreCase(""))) {
                                    tvExmByExmPercentageRVK.setText(builderPercentage);
                                } else {
                                    tvExmByExmPercentageRVK.setText(strPercentage);
                                }
                            }

                        }
                    });
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //


                                    lynrLytExamMarksByExamDetails.setVisibility(View.GONE);
                                    lynrLytStudentDetailsRVK.setVisibility(View.GONE);
                                    recyclerViewExamMarksByExamExmMrkRVK.setVisibility(View.GONE);
                                    lynrLytExamMarksByExamGradeDetails.setVisibility(View.GONE);
                                    recyclerAdapter=null;
                                    examArray= new JSONArray();
                                    initExamRecylerAdapter();
                                }
                            });
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
            }catch(Exception e){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast toast= Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        }


                });



            }
            initExamRecylerAdapter();
            progressDialog.dismiss();


        }
    }
}
