package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.SubjectRecyclerAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SyllabusActivity extends AppCompatActivity {
    ListView syllabusListView;
    RecyclerView recyclerView;
    String strStudentId;
    StudentSPreference studentSPreference;

    HashMap<String, String> hashMap = new HashMap<String, String>();
    TextView tvSubSylStudentName, tvSubSylClass, tvSubSylRollNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.syllabustoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Syllabus");
        initialisation();
        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetSubjectList().execute();
    }

    public void initialisation() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        tvSubSylStudentName = (TextView) findViewById(R.id.tvSubSylStudentName);
        tvSubSylClass = (TextView) findViewById(R.id.tvLandingClass);
        tvSubSylRollNo = (TextView) findViewById(R.id.tvSubSylRollNo);

        try {
            strStudentId = SyllabusActivity.this.getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(SyllabusActivity.this);
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        tvSubSylRollNo.setText(strRollNo);
        tvSubSylClass.setText(strClass);
        tvSubSylStudentName.setText(strStudentnName);

    }

    private class GetSubjectList extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SyllabusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Subjects...");
            progressDialog.show();
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

            SubjectRecyclerAdapter recyclerAdapter = new SubjectRecyclerAdapter(values[0],SyllabusActivity.this,strStudentId);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(recyclerAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
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
}
