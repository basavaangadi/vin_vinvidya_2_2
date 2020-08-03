package com.vinuthana.vinvidya.activities.extraactivities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.useractivities.ChangePasswordActivity;
import com.vinuthana.vinvidya.activities.useractivities.ProfileActivity;
import com.vinuthana.vinvidya.adapters.LandingAdapter;
import com.vinuthana.vinvidya.fcm.FCMRegistrationIntentService;
import com.vinuthana.vinvidya.fcm.FCMUtils;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.Session;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LandingActivity extends AppCompatActivity {
    Toolbar landingToolbar;
    RecyclerView recyclerViewLanding;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Session session;
    HashMap<String, String> user;
    List<String>strSchoolList= new ArrayList<String>();
    StudentSPreference studentSharedPreference;
    LandingAdapter recyclerAdapter;
    JSONArray result;
    String strStatus = "",strToken;

    ArrayList<HashMap<String, String>> spList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initialisation();
        setSupportActionBar(landingToolbar);
        getSupportActionBar().setTitle("Choose Student");

        session = new Session(getApplicationContext());

        session.checkLogin();
        user = session.getUserDetails();

        new GetStudent().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_profile:
                intent = new Intent(LandingActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            /*case R.id.action_aboutUs:
                intent = new Intent(LandingActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;*/
            case R.id.action_changePassword:
                intent = new Intent(LandingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
               /* for (int i=0;i<=strSchoolList.size();i++){
                    String strSchoolId=strSchoolList.get(i);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(strSchoolId);
                    Log.e("unsubscribing to ",strSchoolId);
                }*/
                session.logOut();
                finish();
                break;
            default:
                break;
        }
        if (id == R.id.action_profile) {
            //Toast.makeText(ListActivity_new.this, "Clicked Profile", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialisation() {
        landingToolbar = (Toolbar) findViewById(R.id.landingToolbar);
        recyclerViewLanding = (RecyclerView) findViewById(R.id.recyclerViewLanding);
        mRegistrationBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(FCMUtils.messages.REG_SUCCESS)){
                    strToken=intent.getStringExtra("token");

                    if (strToken != null) {
                        Log.e("TAG", strToken+"Length = "+strToken.length());
                    }
                } else if (intent.getAction().equals(FCMUtils.messages.REG_ERROR)) {
                    Log.e("TAG", "Error");


                }
            }
        };
        Intent intent = new Intent(LandingActivity.this, FCMRegistrationIntentService.class);
        startService(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("LandingActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FCMUtils.messages.REG_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FCMUtils.messages.REG_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("LandingActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    private class GetStudent extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String strPhoneNo,strAcademicYearId;
        String url = AD.url.base_url + "userOperations.jsp";
        //String url ="http://192.168.43.155:8080/netvinvidyawebapi/operation/userOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LandingActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Student(s)...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            strPhoneNo = user.get(Session.KEY_PHONE_NO);
           strAcademicYearId = "5";
            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", "getStudentListByParent");
                JSONObject userData = new JSONObject();
                userData.put("phoneNumber", strPhoneNo);
                userData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put("userData", userData);
                Log.e("TAG", "GetStudent, doInBackground, userData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetStudent, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                result = inObject.getJSONArray("Result");
                publishProgress(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

            recyclerAdapter = new LandingAdapter(values[0], LandingActivity.this, LandingActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewLanding.setLayoutManager(layoutManager);
            recyclerViewLanding.setItemAnimator(new DefaultItemAnimator());
            recyclerViewLanding.setAdapter(recyclerAdapter);

            try {
                JSONArray jsonArray = values[0];

                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);

                        HashMap<String, String> spHashmap = new HashMap<String, String>();
                        spHashmap.put("strStudentname", jsonObject.getString("studentname"));
                        spHashmap.put("strClass", jsonObject.getString("class"));

                        spHashmap.put("strStudentId", jsonObject.getString("studentId"));
                        spHashmap.put("strSchoolId", jsonObject.getString("SchoolId"));
                        spHashmap.put("strSchool", jsonObject.getString("School"));
                        spHashmap.put("strClassId", jsonObject.getString(getString(R.string.key_ClassId)));
                        spHashmap.put("strAcademicID", jsonObject.getString(getString(R.string.key_AcademicYearId)));
                       // spHashmap.put("strRollNo", jsonObject.getString("RollNo"));
                        spList.add(spHashmap);
                        strSchoolList.add( jsonObject.getString("SchoolId"));
                    }
                    //Student list SP
                    studentSharedPreference = new StudentSPreference(LandingActivity.this);
                    studentSharedPreference.createStudPref(spList);
                }
            } catch (Exception e) {
                Toast.makeText(LandingActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
