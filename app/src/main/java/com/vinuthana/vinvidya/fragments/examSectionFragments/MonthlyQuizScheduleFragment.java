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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExamSheduleRecycler;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyQuizScheduleFragment extends Fragment {
    TableLayout tblCurExmSchdlsGrid;
    RecyclerView recyclerViewExmSchMQJV;
    TextView tvCurExamScheduleNameMQJV, tvCurExamScheduleClassMQJV, tvCurExamScheduleSection,
            tvCurExamScheduleExamMQJV, tvCurExamScheduleStudNameMQJV;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus,strClass,strStudentnName;

    public MonthlyQuizScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_quiz_schedule, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        /*if(getActivity()!=null){
        new GetPreviousExamSchedule().execute();
        }*/
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){
            if(getActivity()!=null){
                new GetPreviousExamSchedule().execute();
            }
        }
    }

    public void initialisation() {
        recyclerViewExmSchMQJV = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmSchMQJV);
        tvCurExamScheduleNameMQJV = (TextView) getActivity().findViewById(R.id.tvCurExamScheduleStudNameMQJV);
        tvCurExamScheduleClassMQJV = (TextView) getActivity().findViewById(R.id.tvCurExamScheduleClassMQJV);

        tvCurExamScheduleStudNameMQJV = (TextView) getActivity().findViewById(R.id.tvCurExamScheduleStudNameMQJV);


        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


      
        strClass = hashMap.get(StudentSPreference.STR_CLASS);
        strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
      

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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examScheduleDisplayByExamid_jv));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId),"42");
                
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
            recyclerViewExmSchMQJV.setLayoutManager(layoutManager);
            recyclerViewExmSchMQJV.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExmSchMQJV.setAdapter(recyclerAdapter);
            try {
                String text = "<font color=#FF8C00>Class </font> <font color=#252f39>"+strClass+"</font>";
                String clsText=text+":"+strClass;
                tvCurExamScheduleClassMQJV.setText(Html.fromHtml(clsText));
                tvCurExamScheduleStudNameMQJV.setText(strStudentnName);

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
