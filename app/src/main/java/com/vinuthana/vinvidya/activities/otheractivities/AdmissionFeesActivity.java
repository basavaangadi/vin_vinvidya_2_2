package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.extraactivities.MainActivity;
import com.vinuthana.vinvidya.adapters.FeesDetailsRecyclerViewAdapter;
import com.vinuthana.vinvidya.adapters.FeesTypeSpinnerAdapter;
import com.vinuthana.vinvidya.adapters.YearSpinnerAdapter;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.Session;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class AdmissionFeesActivity extends AppCompatActivity {
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    Button btnSubmit;
    Spinner spnrFeesType,spnrYear;
    RecyclerView rcvwAdmissionFees;
    String strfees_type_Id="",strFeeType="",strAcademicYearId="",strStudentId="",strSchoolId="",strClass="",strStudentnName="";
    String url="http://192.168.43.155:8080/AdmissionFessStudApp/fees/admissionfees.jsp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_admission_fees);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Fees Collected");

        studentSPreference = new StudentSPreference(AdmissionFeesActivity.this);
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

        //strClass = hashMap.get(StudentSPreference.STR_CLASS);
        //strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        strSchoolId = hashMap.get(StudentSPreference.STR_SCHOOLID);
        strStudentId = getIntent().getExtras().getString("studentId");


        btnSubmit= findViewById(R.id.btnSubmit);
        spnrYear= findViewById(R.id.spnrYear);
        spnrFeesType= findViewById(R.id.spnrFeesType);
        rcvwAdmissionFees= findViewById(R.id.rcvwAdmissionFees);



        new  GetYear().execute();
        spnrYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = spnrYear.getSelectedView().findViewById(R.id.tvList);
                strAcademicYearId = parent.getItemAtPosition(position).toString();
                String strSection = textView.getText().toString();
                if (!(strSection.equalsIgnoreCase("Select Year"))) {
                    new GetFeesType().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnrFeesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = spnrFeesType.getSelectedView().findViewById(R.id.tvList);
                strfees_type_Id = parent.getItemAtPosition(position).toString();
                strFeeType= textView.getText().toString();
                String strfees_type_Id = textView.getText().toString();
                if (!(strfees_type_Id.equalsIgnoreCase("Select FeesType"))) {
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetAdmissionFeesStudWise().execute();
            }
        });

    }
    class GetYear extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdmissionFeesActivity.this);
            progressDialog.setMessage("please wait while we fetch your Data");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName", "GetYear");
                JSONObject userData = new JSONObject();
                userData.put("SchoolId", strSchoolId);
                outObject.put("userData", userData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    AdmissionFeesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(AdmissionFeesActivity.this, "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is", e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            YearSpinnerAdapter adapter = new YearSpinnerAdapter(values[0], AdmissionFeesActivity.this);
            spnrYear.setPrompt("Choose Year");
            spnrYear.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }


    class GetFeesType extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdmissionFeesActivity.this);
            progressDialog.setMessage("please wait while we fetch your Data");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName", "GetFeesType");
                JSONObject userData = new JSONObject();
                userData.put("SchoolId", strSchoolId);
                outObject.put("userData", userData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    AdmissionFeesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(AdmissionFeesActivity.this, "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is", e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            FeesTypeSpinnerAdapter adapter = new FeesTypeSpinnerAdapter(values[0], AdmissionFeesActivity.this);
            spnrFeesType.setPrompt("Choose Fees Type");
            spnrFeesType.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    class GetAdmissionFeesStudWise  extends AsyncTask<String, JSONArray,Void> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(AdmissionFeesActivity.this);
            progressDialog.setMessage("Fetching Admission Fees StudWise...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response= new GetResponse();
            JSONObject outObject= new JSONObject();

            try {
                outObject.put("OperationName","GetAdmissionFeesStudWise");
                JSONObject userData= new JSONObject();
                userData.put("StudentId",strStudentId);
                outObject.put("userData",userData);
                Log.e("outObject is ",outObject.toString());
                String strRespText=response.getServerResopnse(url,outObject.toString());
                Log.e("Response is",strRespText);
                JSONObject inObject=new JSONObject(strRespText);
                String strStatus=inObject.getString("Status");
                if(strStatus.equalsIgnoreCase("Success")){
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                }else{
                    AdmissionFeesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast=Toast.makeText(AdmissionFeesActivity.this,"Data not found",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            FeesDetailsRecyclerViewAdapter mAdapter =new FeesDetailsRecyclerViewAdapter(values[0],AdmissionFeesActivity.this,strFeeType);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(AdmissionFeesActivity.this);
            rcvwAdmissionFees.setLayoutManager(mLayoutManager);
            rcvwAdmissionFees.setItemAnimator(new DefaultItemAnimator());
            rcvwAdmissionFees.setAdapter(mAdapter);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
    }



}
