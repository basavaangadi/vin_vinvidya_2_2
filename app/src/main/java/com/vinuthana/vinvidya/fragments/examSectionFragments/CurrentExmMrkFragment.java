package com.vinuthana.vinvidya.fragments.examSectionFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CurrentExmMrkFragment extends Fragment {
    RadioGroup radioGroupCurMrk;
    TextView tvCurExmMrksStudentName, tvCurExmMrksClass, tvCurExmMrksSection, tvCurExmMrksExam;
    String strTotal;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus;

    public CurrentExmMrkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager curExmMarks = getFragmentManager();
        Fragment curExaMrksFrg = new CurrentExamMarks();
        curExmMarks.beginTransaction().replace(R.id.frmLayoutCurExmMarks, curExaMrksFrg).commit();
        initialisation();
        allEvents();
        new GetPreviousExamMarksByExam().execute();
    }

    public void initialisation() {
        //radioGroupCurMrk = (RadioGroup) getActivity().findViewById(R.id.radioGroupCurMrk);
        tvCurExmMrksStudentName = (TextView) getActivity().findViewById(R.id.tvCurExmMrksStudentName);
        tvCurExmMrksClass = (TextView) getActivity().findViewById(R.id.tvCurExmMrksClass);
        tvCurExmMrksExam = (TextView) getActivity().findViewById(R.id.tvCurExmMrksExam);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        tvCurExmMrksClass.setText(strClass);
        tvCurExmMrksStudentName.setText(strStudentnName);
    }

    public void allEvents() {
        /*radioGroupCurMrk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioCurExmMrk:
                        FragmentManager curExmMarks = getFragmentManager();
                        Fragment curExaMrksFrg = new CurrentExamMarks();
                        curExmMarks.beginTransaction().replace(R.id.frmLayoutCurExmMarks, curExaMrksFrg).commit();
                        break;
                    case R.id.radioCurExmMrkGraph:
                        FragmentManager curExmMrkGraph = getFragmentManager();
                        Fragment frg = new CurrentExmMrkGraph();
                        curExmMrkGraph.beginTransaction().replace(R.id.frmLayoutCurExmMarks, frg).commit();
                        break;
                    default:
                        break;
                }
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_exm_mrk, container, false);
    }


    private class GetPreviousExamMarksByExam extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplay_Studentwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "CurrentExmMrkFragment,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "CurrentExmMrkFragment,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                //JSONArray result = inObject.getJSONArray("Result");
                strStatus = inObject.getString(getString(R.string.key_Status));
                strTotal = inObject.getString(getString(R.string.key_Total));
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
            super.onProgressUpdate(values[0]);
            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strExam = jsonObject.getString("Exam");
                tvCurExmMrksExam.setText(strExam);
            } catch (Exception e) {
                Log.e("doInBackground",e.toString());
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
