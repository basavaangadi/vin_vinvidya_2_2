package com.vinuthana.vinvidya.fragments.dayToDayFragments;

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
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.HwrkFbRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class CurHWFeedBack extends Fragment {
    TextView tvCurHWFbStudName,tvCurHWFbClass,tvCurHWFbRollNo;
    RecyclerView recyclerViewHmWrkFd;
    View view;
    StudentSPreference studentSPreference;
    HashMap<String,String> hashMap = new HashMap<String, String>();
    String strStudentId;



    public CurHWFeedBack() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation(view);
        allEvents();
        new GetPreviousHWFeedback().execute();
    }

    private void initialisation(View view) {
        tvCurHWFbStudName = (TextView) view.findViewById(R.id.tvCurHWFbStudName);
        tvCurHWFbClass = (TextView) view.findViewById(R.id.tvCurHWFbClass);
        tvCurHWFbRollNo = (TextView) view.findViewById(R.id.tvCurHWFbRollNo);
        recyclerViewHmWrkFd = (RecyclerView) view.findViewById(R.id.recyclerViewHmWrkFd);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


       String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass=hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName=hashMap.get(StudentSPreference.KEY_STUD_NAME);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        tvCurHWFbRollNo.setText(strRollNo);
        tvCurHWFbClass.setText(strClass);
        tvCurHWFbStudName.setText(strStudentnName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cur_hwfeed_back, container, false);

        return view;
    }

    private void allEvents() {
    }

    private class GetPreviousHWFeedback extends AsyncTask<String,JSONArray,Void> {

        ProgressDialog progressDialog;

        String url = AD.url.base_url + "homeworkOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Home Work Feedback...");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_homeworkFeedback_StudentWise));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_homeworkData),homeworkData);
                Log.e("TAG","GetPreviousHWFeedback, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url,outObject.toString());
                Log.e("TAG","GetPreviousHWFeedback, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
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
            }
            catch (Exception e) {
                Log.e("doinbcgk",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            //addRowToTable(values[0]);
            try {
                HwrkFbRecyclerViewAdapter adapter = new HwrkFbRecyclerViewAdapter(values[0],getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewHmWrkFd.setLayoutManager(layoutManager);
                recyclerViewHmWrkFd.setItemAnimator(new DefaultItemAnimator());
                recyclerViewHmWrkFd.setAdapter(adapter);

               /* JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString("StudentName");
                String strClass = jsonObject.getString("Class");
                tvCurHWFbStudName.setText(strStudentName);
                tvCurHWFbClass.setText(strClass);*/
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
