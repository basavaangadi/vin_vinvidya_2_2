package com.vinuthana.vinvidya.fragments.examSectionFragments;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExmMarksRecycler;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ExamMarksByExamFragment extends Fragment {
    Button btnExamMarksByExam;
    Spinner spnrExamMarksByExam;
    View view;
    JSONArray result;
    TableLayout tblExmMarksByExmGrid;
    CurrentExamMarks currentExamMarks= new CurrentExamMarks();
    RecyclerView recyclerViewExmMrkByExm;
    TextView tvExmMrksByExmStuName, tvExmMrksByExmClass, tvExmMrksByExmSection, tvTotalExmMrkByExm;
    String strTotal;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strExam, strStatus;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");

        } catch (Exception e) {
            e.printStackTrace();
        }
       // new GetPreviousExamMarksByExam().execute();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //currentExamMarks.crdViewCurExmMrk.setVisibility(View.GONE);
        strExam = getArguments().getString("strExam");
        new GetPreviousExamMarksByExam().execute();
    }

    public void initialisation() {
        recyclerViewExmMrkByExm = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmMrkByExm);
        tvTotalExmMrkByExm = (TextView) getActivity().findViewById(R.id.tvExmByExmMaxMrkTotal);
        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //strExam = getArguments().getString("strExam");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exam_marks_by_exm, container, false);
        return view;
    }

    private class GetPreviousExamMarksByExam extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        //String strStudentId;
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Exam Marks By Exam");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayExamwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), "strExam");
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "ExamMarksByExamFragment3,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "ExamMarksByExamFragment3,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strTotal = Double.toString(inObject.getDouble(getString(R.string.key_Total)));
                //result = inObject.getJSONArray("Result");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     //   tvTotalExmMrkByExm.setText(strTotal);
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
                publishProgress(result);
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
            recyclerViewExmMrkByExm.setLayoutManager(layoutManager);
            recyclerViewExmMrkByExm.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExmMrkByExm.setAdapter(recyclerAdapter);
            /*try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
