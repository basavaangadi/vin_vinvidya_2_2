package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.AssignmentAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class AssignmentActivity extends AppCompatActivity {
    Spinner spnrAssignmentClass, spnrAssignmentSection;
    RecyclerView assignmentRecyclerView;
    TextView tvSubAssStudentName,tvLandingClass,tvSubSylRollNo;
    AssignmentAdapter recyclerAdapter;
    Button btnSubmit;
    ProgressDialog pDialog;
    TextView tmpViewSection;
    public String strStaffId, strExam, strSchoolId, strClass, strClassId, strSubjectId;
    String strFilePath,strStudentId,strFileName;
    String strClassSection, strAcademicYearId;
    JSONArray assignmentArray= new JSONArray();
    ProgressDialog progressDialog;
    StudentSPreference studentSPreference;

    HashMap<String, String> hashMap = new HashMap<String, String>();
    private static final int REQUEST_CODE_PERMISSION = 2;
    CheckConnection connection = new CheckConnection();
    String[] mPermission = {"android.permission.ACCESS_NETWORK_STATE" ,
            "android.permission.INTERNET","android.permission.WRITE_EXTERNAL_STORAGE" ,
            "android.permission.READ_PHONE_STATE","android.permission.READ_EXTERNAL_STORAGE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.assignmentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.key_Assignment);
        tvSubAssStudentName = findViewById(R.id.tvSubAssStudentName);
        tvLandingClass = findViewById(R.id.tvLandingClass);
        tvSubSylRollNo = findViewById(R.id.tvSubSylRollNo);
        if (!connection.netInfo(AssignmentActivity.this)) {
            connection.buildDialog(AssignmentActivity.this).show();
        } else {
            try {
                strStudentId = getIntent().getExtras().getString("studentId");

                //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            init();
            allEvents();
           new GetAssignment().execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void allEvents() {

    }

    public void init() {
        assignmentRecyclerView = (RecyclerView) findViewById(R.id.assignmentRecyclerView);
        studentSPreference = new StudentSPreference(AssignmentActivity.this);
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = "<font color=#FF8C00>Roll No : </font>"+hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = "<font color=#FF8C00>Class : </font>"+hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        tvSubSylRollNo.setText(Html.fromHtml(strRollNo));
        tvLandingClass.setText(Html.fromHtml(strClass));
        tvSubAssStudentName.setText(strStudentnName);
        initRecylerAdpater();
    }

    public void initRecylerAdpater(){
        recyclerAdapter = new AssignmentAdapter(assignmentArray, AssignmentActivity.this,strClassSection);
        recyclerAdapter.setOnButtonClickListener(new AssignmentAdapter.OnAssignmentClickListener() {
            @Override
            public void onDownload(JSONObject downloadData, int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AssignmentActivity.this);
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder.setMessage("Would you like to Download file");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            strFilePath=downloadData.getString(getString(R.string.key_File_path));
                            strFilePath=strFilePath.replace(" ","%20");
                            strFileName = downloadData.getString(getString(R.string.key_Filename));
                            //strFilePath = "https://api.androidhive.info/progressdialog/hive.jpg";
                            if(ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[0]) != PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[1]) != PackageManager.PERMISSION_GRANTED ||

                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[2]) != PackageManager.PERMISSION_GRANTED ||

                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[3]) != PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[4]) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(AssignmentActivity.this, mPermission, REQUEST_CODE_PERMISSION);
                            }else{
                                new Download().execute();
                            }

                        } catch (Exception e) {
                            Log.e("onAssignmet", e.toString());
                            Toast.makeText(AssignmentActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();


            }


        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AssignmentActivity.this);
        assignmentRecyclerView.setLayoutManager(layoutManager);
        assignmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        assignmentRecyclerView.setAdapter(recyclerAdapter);

    }

    private class GetAssignment extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AssignmentActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Assignments...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = 0; i < assignmentArray.length(); i++) {
                    assignmentArray.remove(i);
                }
            }else{
                assignmentArray= new JSONArray();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
               outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStudentwiseAssignmentList));
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_otherData), otherData);
                Log.e("TAG", "GetAssignment, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetAssignment, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus=inObject.getString(getString(R.string.key_Status));
                if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
                    JSONArray resultArray = inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        assignmentArray.put(resultArray.getJSONObject(i));
                    }
                }else {
                    AssignmentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AssignmentActivity.this);
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
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                recyclerAdapter.notifyDataSetChanged();
            }else{
                initRecylerAdpater();
            }
        }
    }

    public  class Download extends AsyncTask<String,String,String> {

        String folder;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AssignmentActivity.this);
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setMessage("downloading please wait");


        }

        @Override
        protected String doInBackground(String... strings) {
            int count;

            try {
                URL url = new URL(strFilePath);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lengthOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                 folder= Environment.getExternalStorageDirectory()  + "/Vinvidya/";
                File file = new File(folder);
               // File directory = new File(folder);

                if (!file.exists()) {
                    file.mkdirs();
                }
               // file.mkdirs();
                File outputFile = new File(file, strFileName);
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = conection.getInputStream();

                byte[] buffer = new byte[1024];
                long total = 0;
                int len1;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    fos.write(buffer, 0, len1);
                    publishProgress(String.valueOf(Math.min((int) (total * 100 / lengthOfFile), 100)));
                    Thread.yield();
                }
                fos.close();
                is.close();

            }catch (Exception ex){
                Log.e("Cathed Error",ex.toString());
                /*AssignmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast= Toast.makeText(AssignmentActivity.this,ex.toString(),Toast.LENGTH_SHORT);
                    }
                });*/
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
            pDialog.setMessage("downloading "+values[0]+"/100");
        }

        @Override
        protected void onPostExecute(String location) {
            super.onPostExecute(location);
            //dismissDialog(progress_bar_type);
            //removeDialog(progress_bar_type);
          //  String imagePath = Environment.getExternalStorageDirectory().toString()+strFileName;
            pDialog.setMessage("download complete");
            Toast.makeText(getApplicationContext(),
                    "File has been downloaded at "+folder+"/"+strFileName, Toast.LENGTH_LONG).show();
            pDialog.dismiss();

            //  String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }
}
