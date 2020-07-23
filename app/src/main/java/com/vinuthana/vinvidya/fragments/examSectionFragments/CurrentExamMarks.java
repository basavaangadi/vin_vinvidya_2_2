package com.vinuthana.vinvidya.fragments.examSectionFragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExmMarksRecycler;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CurrentExamMarks extends Fragment {
    View view;
    TextView tvExmMrksStudentName, tvExmMrksClass, tvExmMrksSection, tvExmMrksExam;
    TextView tvCurExmMrksExam, tvExmMrkTblRowMarks, tvExmMrkTblRowSubject, tvExmMrkTotal,tvExmMaxMrkTotal;
    RecyclerView recyclerViewExmMrk;
    JSONArray result;
    String strTotal;
    //BarChart chart;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus;
    CardView crdViewCurExmMrk;

    public CurrentExamMarks() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
        }
    }

    public void initialisation() {
        recyclerViewExmMrk = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmMrk);
        tvExmMrkTotal = (TextView) getActivity().findViewById(R.id.tvExmMrkTotal);
        tvExmMaxMrkTotal=(TextView) getActivity().findViewById(R.id.tvExmMaxMrkTotal);
        crdViewCurExmMrk= getActivity().findViewById(R.id.crdViewCurExmMrk);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_exam_marks, container, false);
        allEvents();
        new GetCurrentExamMarks().execute();
        return view;
    }

    private void allEvents() {
    }

    private class GetCurrentExamMarks extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";
        String strTotalMarks = "",strMaxTotalMarks;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Current Exam Marks..");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplay_Studentwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "CurrentExamMarks1, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "CurrentExamMarks1, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strTotalMarks = Double.toString(inObject.getDouble(getString(R.string.key_Total)));
                strMaxTotalMarks = Double.toString(inObject.getDouble(getString(R.string.key_MaxTotal)));
                //result = inObject.getJSONArray("Result");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvExmMrkTotal.setText(strTotalMarks);
                        tvExmMaxMrkTotal.setText(strMaxTotalMarks);
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
            ExmMarksRecycler recyclerAdapter = new ExmMarksRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewExmMrk.setLayoutManager(layoutManager);
            recyclerViewExmMrk.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExmMrk.setAdapter(recyclerAdapter);
            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strClass = jsonObject.getString(getString(R.string.key_class));
                String strExam = jsonObject.getString(getString(R.string.key_Exam));
                tvExmMrksSection.setText(strClass);
                tvCurExmMrksExam.setText(strExam);
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
