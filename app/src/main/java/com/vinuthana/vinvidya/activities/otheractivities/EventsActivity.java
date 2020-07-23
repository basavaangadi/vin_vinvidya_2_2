package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.EventsRecyclerAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerViewEvents;
    CardView cardEvents;
    String eventsList, strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        initialisation();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Events");

        try {
            strStudentId = getIntent().getExtras().getString("studentId");

            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetEventsList().execute();
    }

    public void initialisation() {
        toolbar = (Toolbar) findViewById(R.id.eventsToolbar);
        recyclerViewEvents = (RecyclerView) findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setItemAnimator(new DefaultItemAnimator());
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

    private class GetEventsList extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String studentId;
        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Events List...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            //studentId = "5";
            try {
                JSONObject outObject = new JSONObject();
                                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getEventList));
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_studentId), strStudentId);
                otherData.put(getString(R.string.key_AcademicYearId), getString(R.string.key_academicYear));
                outObject.put(getString(R.string.key_otherData), otherData);
                Log.e("TAG", "GetEventsList, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetEventsList, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EventsActivity.this);
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
            EventsRecyclerAdapter recyclerAdapter = new EventsRecyclerAdapter(values[0], EventsActivity.this,strStudentId);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewEvents.setLayoutManager(layoutManager);
            recyclerViewEvents.setItemAnimator(new DefaultItemAnimator());
            recyclerViewEvents.setAdapter(recyclerAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
