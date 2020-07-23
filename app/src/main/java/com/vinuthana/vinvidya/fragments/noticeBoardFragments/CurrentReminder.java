package com.vinuthana.vinvidya.fragments.noticeBoardFragments;

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
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ReminderRecylcerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CurrentReminder extends Fragment {
    TextView tvCurReminderStudentName, tvCurReminderClass, tvCurReminderRollNo;
    View view;
    RecyclerView rcyclrVwCurRmdr;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus;

    public CurrentReminder() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        new GetPreviousReminder().execute();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

        }
    }

    private void initialisation() {
        tvCurReminderStudentName = (TextView) getActivity().findViewById(R.id.tvCurReminderStudentName);
        tvCurReminderClass = (TextView) getActivity().findViewById(R.id.tvCurReminderClass);
        tvCurReminderRollNo = (TextView) getActivity().findViewById(R.id.tvCurReminderRollNo);
        rcyclrVwCurRmdr = (RecyclerView) getActivity().findViewById(R.id.rcyclrVwCurRmdr);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);

        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        tvCurReminderRollNo.setText(strRollNo);
        tvCurReminderClass.setText(strClass);
        tvCurReminderStudentName.setText(strStudentnName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_reminder, container, false);
        return view;
    }

    private class GetPreviousReminder extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Current Reminder...");
            progressDialog.show();

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_studentReminderDisplay_studentwise));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StudentId), strStudentId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId),  getString(R.string.key_academicYear));
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetPreviousReminder,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousReminder,doInBackground, respText =" + respText);
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
                            View convertView = (View) inflater.inflate(R.layout.custom,null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel",null);
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("doinbcgk",e.toString());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ReminderRecylcerViewAdapter adapter = new ReminderRecylcerViewAdapter(values[0], getActivity());
            RecyclerView.LayoutManager rmdrLytMngr = new LinearLayoutManager(getActivity());
            rcyclrVwCurRmdr.setLayoutManager(rmdrLytMngr);
            rcyclrVwCurRmdr.setItemAnimator(new DefaultItemAnimator());
            rcyclrVwCurRmdr.setAdapter(adapter);
           /* try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString("Studentname");
                String strClass = jsonObject.getString("Class");
                tvCurReminderStudentName.setText(strStudentName);
                tvCurReminderSection.setText(strClass);
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
