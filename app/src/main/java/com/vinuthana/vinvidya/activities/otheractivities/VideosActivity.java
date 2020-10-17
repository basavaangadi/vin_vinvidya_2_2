package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.examsection.PdfActivity;
import com.vinuthana.vinvidya.activities.examsection.UploadAnswersActivity;
import com.vinuthana.vinvidya.activities.examsection.WriteExamActivity;
import com.vinuthana.vinvidya.activities.extraactivities.MainActivity;
import com.vinuthana.vinvidya.adapters.ChapterSpinnerDataAdapter;
import com.vinuthana.vinvidya.adapters.ExamSectionAdpater.SubjectSpinnerDataAdapter;
import com.vinuthana.vinvidya.adapters.SpinnerDataAdapter;
import com.vinuthana.vinvidya.adapters.TopicsSpinnerDataAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class VideosActivity extends AppCompatActivity {
    TextView tvStudentName,tvClass,tvRollNo,tvTopics,tvSubject,tvChapter;
    Spinner spnrSubject,spnrChapter,spnrTopics;
    Button btnGetTopicWiseVideos;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap =
            new HashMap<String, String>();
    String strStudentId="",strStudentnName="",strRollNo="",strClass="",strSchoolId="",strClassId="",strAcademicYearId="",
            strSubjectId="",strSubjectName="",strChapterId="",strStatus="",strVideo_link="";
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        tvStudentName = findViewById(R.id.tvStudentName);
        tvClass = findViewById(R.id.tvClass);
        tvRollNo = findViewById(R.id.tvRollNo);
        tvSubject = findViewById(R.id.tvSubject);
        tvChapter = findViewById(R.id.tvChapter);
        tvTopics = findViewById(R.id.tvTopics);
        spnrSubject = findViewById(R.id.spnrSubject);
        spnrChapter = findViewById(R.id.spnrChapter);
        spnrTopics = findViewById(R.id.spnrTopics);
        btnGetTopicWiseVideos = findViewById(R.id.btnGetTopicWiseVideos);

        try {
            strStudentId = VideosActivity.this.getIntent().getExtras().getString("studentId");
        } catch (Exception e) {
            e.printStackTrace();
        }


        studentSPreference = new StudentSPreference(VideosActivity.this);
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
        new GetSubjectList().execute();

        spnrSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) spnrSubject.getSelectedView().findViewById(R.id.tvList);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId=parent.getItemAtPosition(position).toString();
                strSubjectName= tmpView.getText().toString();
                Log.e("Tag", "" + strSubjectName);
                new GetSyllabusList().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnrChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) spnrSubject.getSelectedView().findViewById(R.id.tvList);
                tmpView.setTextColor(Color.WHITE);
                strChapterId=parent.getItemAtPosition(position).toString();
               // strSubjectName= tmpView.getText().toString();
               // Log.e("Tag", "" + strSubjectName);
                new GetTopicList().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

btnGetTopicWiseVideos.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewVideosActivity.class);
        Bundle bundle= new Bundle();
        bundle.putString("video_link",strVideo_link);
        intent.putExtras(bundle);
        startActivity(intent);



    }
});
    }

    class GetSubjectList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        //String url = "http://192.168.43.155:8080/netvinvidyawebapi/operation/otherOperation.jsp";
        String url = AD.url.base_url + "otherOperation.jsp";

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
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], VideosActivity.this);
            spnrSubject.setPrompt("Choose Subject");
            spnrSubject.setAdapter(adapter);
        }
    }

    class GetSyllabusList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        //String url = "http://192.168.43.155:8080/netvinvidyawebapi/operation/otherOperation.jsp";
        String url = AD.url.base_url + "otherOperation.jsp";

        //String url = AD.url.base_url + "otherOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", "getSyllabus");
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_StudentId), strStudentId);
                otherData.put("SubjectId",strSubjectId);
                outObject.put("otherData", otherData);
                Log.e("TAG", "GetSyllabusList, doInBackground, otherData = " + outObject.toString());
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
            ChapterSpinnerDataAdapter adapter = new ChapterSpinnerDataAdapter(values[0], VideosActivity.this);
            spnrChapter.setPrompt("Choose Chapter");
            spnrChapter.setAdapter(adapter);
        }
    }
    class GetTopicList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
       // String url = "http://192.168.43.155:8080/netvinvidyawebapi/operation/otherOperation.jsp";

        String url = AD.url.base_url + "otherOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();

            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", "getTopicsAndVideoLinks");
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_ClassId), strClassId);
                otherData.put("SubjectId",strSubjectId);
                outObject.put("otherData", otherData);
                /*Log.e("TAG", "GetSyllabusList, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetSubjectList, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                JSONArray result = inObject.getJSONArray("Result");
                publishProgress(result);*/
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
                            strVideo_link = object.getString("video_link");

                        } catch (Exception e) {
                            Log.e("Error", "onPostExecute Execption is " + e.toString());
                        }
                    }

                   /* Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
                    Bundle bundle= new Bundle();
                    bundle.putString("video_link",strVideo_link);
                    intent.putExtras(bundle);
                    startActivity(intent);*/

                } else {
                    VideosActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(VideosActivity.this, "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
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
            super.onProgressUpdate(values);
            TopicsSpinnerDataAdapter adapter = new TopicsSpinnerDataAdapter(values[0], VideosActivity.this);
            spnrTopics.setPrompt("Choose Topics");
            spnrTopics.setAdapter(adapter);
        }
    }


}
