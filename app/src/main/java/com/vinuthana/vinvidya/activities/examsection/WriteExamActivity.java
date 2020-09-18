package com.vinuthana.vinvidya.activities.examsection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.otheractivities.SyllabusActivity;
import com.vinuthana.vinvidya.adapters.ExamSectionAdpater.SubjectSpinnerDataAdapter;
import com.vinuthana.vinvidya.adapters.SpinnerDataAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class WriteExamActivity extends AppCompatActivity {
    TextView tvStudentName,tvClass,tvRollNo;
    Spinner spnrExam,spnrSubject;
    Button btnGetQuestionPaper;
    WebView webView;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
JSONArray jsonArray;
    String strStudentId="",strSchoolId="",strClassId="",strStudentnName="",strRollNo="",strClass="",strResult="";
    String strStatus="",strSubjectName="",strExamName="",strSubjectId="",strExamId="",strquestion_pdf_link="",strquestion_paper_attachments_id="",strAcademicYearRange="",strAcademicYearId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_exam);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Write Exam");

        tvStudentName = findViewById(R.id.tvStudentName);
        tvClass = findViewById(R.id.tvClass);
        tvRollNo = findViewById(R.id.tvRollNo);
        spnrExam = findViewById(R.id.spnrExam);
        spnrSubject = findViewById(R.id.spnrSubject);
        btnGetQuestionPaper = findViewById(R.id.btnGetQuestionPaper);
        webView= findViewById(R.id.webView);

        try {
            strStudentId = WriteExamActivity.this.getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        studentSPreference = new StudentSPreference(WriteExamActivity.this);
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

         strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
         strRollNo = hashMap.get(studentSPreference.ROLL_NUM);

         strClass = hashMap.get(StudentSPreference.STR_CLASS);
        strSchoolId = hashMap.get(StudentSPreference.STR_SCHOOLID);
        strClassId =hashMap.get(StudentSPreference.STR_CLASS_ID);

        strAcademicYearId =hashMap.get(StudentSPreference.STR_ACADEMIC_YEAR_ID);


        tvRollNo.setText(strRollNo);
        tvClass.setText(strClass);
        tvStudentName.setText(strStudentnName);


        new GetExams().execute();

        spnrExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView tmpView = (TextView) spnrExam.getSelectedView().findViewById(R.id.txt_exam);
                tmpView.setTextColor(Color.WHITE);
                strExamId=parent.getItemAtPosition(position).toString();
                strExamName= tmpView.getText().toString();
                Log.e("Tag", "" + strExamName);
                //Toast.makeText(getActivity(), strExam + "You Clicked on ", Toast.LENGTH_SHORT).show();
                new GetSubjectList().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) spnrSubject.getSelectedView().findViewById(R.id.tvList);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId=parent.getItemAtPosition(position).toString();
                strSubjectName= tmpView.getText().toString();
                Log.e("Tag", "" + strSubjectName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGetQuestionPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetQuestionPaper().execute();

            }
        });


    }

    class GetExams extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "examsectionOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WriteExamActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamName));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_examData), userData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(responseText).getJSONArray("Result"));
                } else {
                    WriteExamActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WriteExamActivity.this);
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            progressDialog.dismiss();
            SpinnerDataAdapter adapter = new SpinnerDataAdapter(values[0], WriteExamActivity.this);
            spnrExam.setPrompt("Choose Exam");
            spnrExam.setAdapter(adapter);
        }
    }
    class GetSubjectList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = "http://192.168.43.155:8080/netvinvidyawebapi/operation/otherOperation.jsp";

        //String url = AD.url.base_url + "otherOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", "getSubjectlist");
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_studentId), strStudentId);
                outObject.put("otherData", otherData);
                Log.e("TAG", "GetSubjectList, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetSubjectList, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                JSONArray result = inObject.getJSONArray("Result");
                publishProgress(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], WriteExamActivity.this);
            spnrSubject.setPrompt("Choose Subject");
            spnrSubject.setAdapter(adapter);
        }
    }



    class GetQuestionPaper extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = "http://192.168.43.155:8080/netvinvidyawebapi/operation/examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WriteExamActivity.this);
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

                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getQuestionPaper));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_SubjectId), strSubjectId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                 strStatus = inObject.getString("Status");
              //   strResult = inObject.getString("Result");
                if (strStatus.equalsIgnoreCase("Success")) {

                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                    jsonArray = new JSONObject(strRespText).getJSONArray("Result");

                    for(int i=0;i<=jsonArray.length();i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            strquestion_pdf_link = object.getString("question_pdf_link");
                            strquestion_paper_attachments_id = object.getString("question_paper_attachments_id");
                            strAcademicYearRange = object.getString("AcademicYearRange");

                        } catch (Exception e) {
                            Log.e("Error", "onPostExecute Execption is " + e.toString());
                        }
                    }

                        Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
                    Bundle bundle= new Bundle();
                    bundle.putString("question_pdf_link",strquestion_pdf_link);
                    bundle.putString("SchoolId",strSchoolId);
                    bundle.putString("StudentId",strStudentId);
                    bundle.putString("Class",strClass);
                    bundle.putString("ExamId",strExamId);
                    bundle.putString("ExamName",strExamName);
                    bundle.putString("AcademicYearRange",strAcademicYearRange);
                    bundle.putString("ClassId",strClassId);
                    bundle.putString("SubjectName",strSubjectName);
                    bundle.putString("question_paper_attachments_id",strquestion_paper_attachments_id);

                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {
                    WriteExamActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(WriteExamActivity.this, "Data not found", Toast.LENGTH_LONG);
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

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();

        }



    }


}
