package com.vinuthana.vinvidya.fragments.dayToDayFragments;


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
import com.vinuthana.vinvidya.adapters.TimeTableAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SundayFragment extends Fragment {

    TextView tvTimeTableClsDispSun;
    RecyclerView recyclerViewSun;
    String respText;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId;
    
    
    
    public SundayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sunday, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialization();
        if(getActivity()!=null){
            new GetTimeTable().execute();
        }

    }

    public void initialization() {
        tvTimeTableClsDispSun = (TextView) getActivity().findViewById(R.id.tvTimeTableClsDispSun);
        recyclerViewSun = (RecyclerView) getActivity().findViewById(R.id.recyclerViewSun);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        tvTimeTableClsDispSun.setText(strClass);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){
            if(getActivity()!=null)
            new GetTimeTable().execute();
        }
    }

    private class GetTimeTable extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "timetableOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Time Table...");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_timetableDisplay_ClassWise));
                JSONObject timeTableData = new JSONObject();
                timeTableData.put(getString(R.string.key_StudentId), strStudentId);
                timeTableData.put(getString(R.string.key_dayId), "7");
                outObject.put(getString(R.string.key_timeTableData), timeTableData);
                GetResponse response = new GetResponse();
                respText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "GetTimeTable, doInBackground   outObject =" + outObject.toString());
                Log.e("Tag", "GetTimeTable, doInBackground   RespText =" + respText);
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
            } catch (Exception e) {
                Log.e("doinbackground",e.toString());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values[0]);
            TimeTableAdapter recyclerViewAdapter = new TimeTableAdapter(values[0], getActivity());
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewSun.setLayoutManager(prntNtLytMngr);
            recyclerViewSun.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSun.setAdapter(recyclerViewAdapter);
            

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }    
    
}
