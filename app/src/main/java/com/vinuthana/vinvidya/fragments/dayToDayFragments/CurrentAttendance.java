package com.vinuthana.vinvidya.fragments.dayToDayFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.daytoday.AttendanceActivity;
import com.vinuthana.vinvidya.adapters.AtndPeriodWiseRecyclerAdapter;
import com.vinuthana.vinvidya.adapters.AtndRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class CurrentAttendance extends Fragment {
    //EditText editText;
    TableLayout tblCurAttenGrid;
    RecyclerView recyclerView;
    TextView tvCurAtndStudentName, tvCurAtndClass, tvCurAtndRollNo,tvPeriodAtndCrdpw;
    CardView cwCurAtnd,cwCurAtndPw;

    View view;
    boolean isFragmentLoaded = false;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId,strSchoolId;

    public CurrentAttendance() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getActivity() != null) {
            if (isVisibleToUser) {
                new GetStudentCurrentAttendance().execute();
                isFragmentLoaded = true;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        /*new GetStudentCurrentAttendance().execute();*/
    }

    private void initialisation() {
        //tblCurAttenGrid = (TableLayout) getActivity().findViewById(R.id.tblCurAttenGrid);
        tvCurAtndStudentName = (TextView) getActivity().findViewById(R.id.tvCurAtndStudentName);
        tvCurAtndClass = (TextView) getActivity().findViewById(R.id.tvCurAtndClass);
        tvCurAtndRollNo = (TextView) getActivity().findViewById(R.id.tvCurAtndRollNo);
        tvPeriodAtndCrdpw = (TextView) getActivity().findViewById(R.id.tvPeriodAtndCrdpw);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        cwCurAtnd = (CardView) view.findViewById(R.id.cwCurAtnd);
        cwCurAtndPw = (CardView) view.findViewById(R.id.cwCurAtndPw);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

        new GetStudentCurrentAttendance().execute();
        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        strSchoolId = hashMap.get(StudentSPreference.STR_SCHOOLID);
        if (strSchoolId.equals("6")) {
            cwCurAtndPw.setVisibility(View.VISIBLE);
            cwCurAtnd.setVisibility(View.GONE);
        } else {
            cwCurAtndPw.setVisibility(View.GONE);
            cwCurAtnd.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        tvCurAtndRollNo.setText(strRollNo);
        tvCurAtndClass.setText(strClass);
        tvCurAtndStudentName.setText(strStudentnName);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_attendance, container, false);
        return view;
    }

    private class GetStudentCurrentAttendance extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("fetching the Attendance...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                if (strSchoolId.equalsIgnoreCase("6"))
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getperiodwiseStudentAttendance));
                else
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_studentAttendanceDefault));
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                Log.e("TAG", "GetStudentCurrentAttendance, doInBackground, outObject =" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetStudentCurrentAttendance, doInBackground, respText =" + respText);
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
                Log.e("doinbcgk",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            if (strSchoolId.equals("6")) {
                AtndPeriodWiseRecyclerAdapter recyclerAdapter = new AtndPeriodWiseRecyclerAdapter(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(recyclerAdapter);
            } else {
                AtndRecyclerViewAdapter recyclerAdapter = new AtndRecyclerViewAdapter(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(recyclerAdapter);
            }


            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString("StudentName");
                String strClass = jsonObject.getString("Class");
                String strRollNo = jsonObject.getString("RollNo");
                tvCurAtndStudentName.setText(strStudentName);
                tvCurAtndClass.setText(strClass);
                tvCurAtndRollNo.setText(strRollNo);
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
