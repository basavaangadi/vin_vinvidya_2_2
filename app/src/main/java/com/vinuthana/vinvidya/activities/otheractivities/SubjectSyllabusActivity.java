package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.SyllabusAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SubjectSyllabusActivity extends AppCompatActivity {
    TextView tvsubjectSyllabus, tvSyllabusClass;
    public Bundle bundle;
    String strSubject, strStudentId,strSubjectId,strClass;
    StudentSPreference studentSPreference;
    RecyclerView rcylrViewSyllbus;
    SyllabusAdapter recyclerAdapter;
    JSONArray syllabusArray= new JSONArray();
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strDescription,  strSyllabus, strChapterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_syllabus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.subSyllabustoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subject Syllabus");

        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialization();

        bundle = null;
        bundle = this.getIntent().getExtras();
        strSubject = bundle.getString("Subject");
        tvsubjectSyllabus.setText(strSubject);
        strSubjectId = bundle.getString("SubjectId");
        strClass = bundle.getString("class");
        tvSyllabusClass.setText(strClass);
        strStudentId = bundle.getString("StudentId");

        if (bundle != null) {
            new GetSyllabusList().execute();
        } else {
            Toast.makeText(SubjectSyllabusActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void initialization() {
        tvsubjectSyllabus = (TextView) findViewById(R.id.tvsubjectSyllabus);
        tvSyllabusClass = (TextView) findViewById(R.id.tvSyllabusClass);
        rcylrViewSyllbus =  findViewById(R.id.rcylrViewSyllbus);

        recyclerAdapter  = new SyllabusAdapter(syllabusArray, getApplicationContext(),SubjectSyllabusActivity.this);
        recyclerAdapter.setOnButtonClickListener(new SyllabusAdapter.OnSyllabusClickListener() {
            @Override
            public void onSyllabusClick(JSONObject syllabusData, int position) {
                try{
                    strSyllabus=syllabusData.getString(getString(R.string.key_Syllabus));
                    strChapterName=syllabusData.getString(getString(R.string.key_ChapterName));
                    if(strSyllabus.equalsIgnoreCase("0")){
                         Toast toast= Toast.makeText(SubjectSyllabusActivity.this, "No sub-topics for this chapter", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        diplayDiscription();
                    }
                }catch (Exception e){
                    Toast.makeText(SubjectSyllabusActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        rcylrViewSyllbus.setLayoutManager(layoutManager);
        rcylrViewSyllbus.setItemAnimator(new DefaultItemAnimator());
        rcylrViewSyllbus.setAdapter(recyclerAdapter);
    }

    private class GetSyllabusList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String studentId;
        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SubjectSyllabusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Events Description...");
            progressDialog.show();
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
                Log.e("TAG", "GetSyllabusList, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus=inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray result = new JSONObject(respText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < result.length(); i++) {
                        syllabusArray.put(result.getJSONObject(i));
                    }

                }
                JSONArray result = inObject.getJSONArray("Result");
                //publishProgress(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            try {
                /*JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String syllabus = jsonObject.getString("Syllabus");
                String subject = jsonObject.getString("Subject");
                tvsubjectSyllabus.setText(syllabus);
                tvSyllabusClass.setText(subject);*/


                /*SyllabusAdapter recyclerAdapter = new SyllabusAdapter(values[0], getApplicationContext(),SubjectSyllabusActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rcylrViewSyllbus.setLayoutManager(layoutManager);
                rcylrViewSyllbus.setItemAnimator(new DefaultItemAnimator());
                rcylrViewSyllbus.setAdapter(recyclerAdapter);*/


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            recyclerAdapter.notifyDataSetChanged();
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
    public void diplayDiscription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubjectSyllabusActivity.this, R.style.MyAlertDialogStyles);
        LinearLayout layout = new LinearLayout(SubjectSyllabusActivity.this);
        TextView tvSubject = new TextView(SubjectSyllabusActivity.this);
        TextView tvEventDescription = new TextView(SubjectSyllabusActivity.this);
        TextView tvSyllabus = new TextView(SubjectSyllabusActivity.this);
        TextView tvChapter = new TextView(SubjectSyllabusActivity.this);
        TextView tvEventCreated = new TextView(SubjectSyllabusActivity.this);
        tvSubject.setText("Subject: " + strSubject);
        tvSubject.setTextColor(Color.WHITE);
        tvSubject.setPadding(0, 25, 0, 25);
        tvSubject.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        tvChapter.setText("Chapter : " + strChapterName);
        tvChapter.setTextColor(Color.WHITE);
        tvChapter.setPadding(0, 25, 0, 25);
        tvChapter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        tvSyllabus.setText("Syllabus: " + strSyllabus);
        tvSyllabus.setTextColor(Color.WHITE);
        tvSyllabus.setPadding(0, 25, 0, 25);
        tvSyllabus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            /*tvEventDescription.setText("Event Description: " + eventDescription);
            tvEventDescription.setTextColor(Color.WHITE);
            tvEventDescription.setPadding(0, 25, 0, 25);
            tvEventDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);*/
        layout.addView(tvSubject);
        layout.addView(tvChapter);
        layout.addView(tvSyllabus);
        layout.addView(tvEventDescription);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

}
