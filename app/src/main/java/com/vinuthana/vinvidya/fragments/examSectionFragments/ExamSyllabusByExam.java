package com.vinuthana.vinvidya.fragments.examSectionFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExamSyllabusRecycler;
import com.vinuthana.vinvidya.adapters.SpinnerDataAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ExamSyllabusByExam extends Fragment {
    Spinner spnrExamSyllabusByExam;
    Button btnSyllabusExamByExam;
    View view;
    RecyclerView recyclerViewExmSch;
    TableLayout tblExmSyllbusByExmGrid;
    TextView tvExmHdrSubject, tvExmHdrSyllbus, tvExmTblRowSubject, tvExmTblRowSyllbus;
    TextView tvPrvsExmSylbsStudentName, tvPrvsExmSylbsClass, tvPrvsExmSylbsSection, tvPrvsExmSylbsExam;
    LinearLayout lrlyExmSylabsByExamStudentDetails;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus, strSchoolId, strExm, strExam,strClass,strStudentnName;
    ;

    public ExamSyllabusByExam() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();

    }

    private void initialisation() {
        tvPrvsExmSylbsStudentName = (TextView) getActivity().findViewById(R.id.tvPrvsExmSylbsStudentName);
        tvPrvsExmSylbsClass = (TextView) getActivity().findViewById(R.id.tvPrvsExmSylbsClass);
        tvPrvsExmSylbsExam = (TextView) getActivity().findViewById(R.id.tvPrvsExmSylbsExam);
        spnrExamSyllabusByExam = (Spinner) getActivity().findViewById(R.id.spnrExamSyllabusByExam);
        recyclerViewExmSch = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmSch);
        btnSyllabusExamByExam=getActivity().findViewById(R.id.btnSyllabusExamByExam);
        lrlyExmSylabsByExamStudentDetails=getActivity().findViewById(R.id.lrlyExmSylabsByExamStudentDetails);



        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        // String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
       strClass = hashMap.get(StudentSPreference.STR_CLASS);
         strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        strSchoolId = hashMap.get(StudentSPreference.STR_SCHOOLID);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        //tvCurAtndRollNo.setText(strRollNo);


        allEvents();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exam_syllabus_by_exam, container, false);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            new GetExams().execute();
        }
    }

    private void allEvents() {
        spnrExamSyllabusByExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) spnrExamSyllabusByExam.getSelectedView().findViewById(R.id.txt_exam);
                tmpView.setTextColor(Color.WHITE);
                strExam = parent.getItemAtPosition(position).toString();
                strExm = spnrExamSyllabusByExam.getSelectedItem().toString();
                Log.e("Tag", "" + strExam);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSyllabusExamByExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetExamSyllabusByExam().execute();
            }
        });
    }

    private class GetExamSyllabusByExam extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Exam Syllabus By Exam...");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examSyllabusDisplayByExam));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), strExam);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "GetExamSyllabusByExam,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetExamSyllabusByExam,doInBackground, respText =" + respText);
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
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ExamSyllabusRecycler recyclerAdapter = new ExamSyllabusRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewExmSch.setLayoutManager(layoutManager);
            recyclerViewExmSch.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExmSch.setAdapter(recyclerAdapter);
            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String strExam = jsonObject.getString("Exam");
                tvPrvsExmSylbsClass.setText(strClass);
                tvPrvsExmSylbsStudentName.setText(strStudentnName);
                tvPrvsExmSylbsExam.setText(strExam);
                lrlyExmSylabsByExamStudentDetails.setVisibility(View.VISIBLE);
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

    class GetExams extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamName));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_examData), userData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    recyclerViewExmSch.setVisibility(View.VISIBLE);
                    publishProgress(new JSONObject(responseText).getJSONArray("Result"));
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
                                    recyclerViewExmSch.setVisibility(View.GONE);
                                }
                            });
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
            spnrExamSyllabusByExam.setPrompt("Choose Exam");
            spnrExamSyllabusByExam.setAdapter(adapter);
        }
    }

}
