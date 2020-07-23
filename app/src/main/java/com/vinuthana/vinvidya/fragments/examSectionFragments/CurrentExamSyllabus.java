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
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExamSyllabusRecycler;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CurrentExamSyllabus extends Fragment {
    TextView tvCurExamSyllabusStudentName, tvCurExamSyllabusClass, tvCurExamSyllabusSection, tvCurExmSylbsExam;
    TextView tvExmTblRowSubject, tvExmTblRowSyllbus, tvExmHdrSubject, tvExmHdrSyllbus;
    View view;
    TableLayout tblCurExmSyllabus;
    RecyclerView recyclerViewExmByExm;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus,strClass,strStudentnName;


    public CurrentExamSyllabus() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        new GetCurrentExamSyllabus().execute();
    }

    private void initialisation() {
        tvCurExamSyllabusStudentName = (TextView) getActivity().findViewById(R.id.tvCurExamSyllabusStudentName);
        tvCurExamSyllabusClass = (TextView) getActivity().findViewById(R.id.tvCurExamSyllabusClass);
        tvCurExmSylbsExam = (TextView) getActivity().findViewById(R.id.tvCurExmSylbsExam);
        recyclerViewExmByExm = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmByExm);



        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        // String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
         strClass = hashMap.get(StudentSPreference.STR_CLASS);
         strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        //tvCurAtndRollNo.setText(strRollNo);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_exam_syllabus, container, false);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }



    private class GetCurrentExamSyllabus extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Current Exam Syllabus...");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examSyllabus));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "GetCurrentExamSyllabus,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurrentExamSyllabus,doInBackground, respText =" + respText);
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
            ExamSyllabusRecycler recyclerAdapter = new ExamSyllabusRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewExmByExm.setLayoutManager(layoutManager);
            recyclerViewExmByExm.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExmByExm.setAdapter(recyclerAdapter);
            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String strExam = jsonObject.getString("Exam");
                tvCurExamSyllabusClass.setText(strClass);
                tvCurExamSyllabusStudentName.setText(strStudentnName);
                tvCurExmSylbsExam.setText(strExam);


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
