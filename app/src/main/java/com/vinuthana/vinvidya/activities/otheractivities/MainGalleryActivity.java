package com.vinuthana.vinvidya.activities.otheractivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.GalleryCatAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainGalleryActivity extends AppCompatActivity {

    ListView listMainGallery;
    String strEvenID, strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainGalleryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery");
        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Initialization();
        AllEvents();
        new GetGalleryCategory().execute();
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

    private void Initialization() {
        listMainGallery = (ListView) findViewById(R.id.list_main_gallery);
        // strStudentID = "5";
    }

    private void AllEvents() {
    }

    class GetGalleryCategory extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "otherOperation.jsp";
        GetResponse response = new GetResponse();
        JSONObject outObject = new JSONObject();

        @Override
        protected Void doInBackground(String... params) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getGAlleryEventList));
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_studentId), strStudentId);
                otherData.put(getString(R.string.key_AcademicYearId), getString(R.string.key_academicYear));
                outObject.put(getString(R.string.key_otherData), otherData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "OutObject = " + outObject.toString());
                Log.e("Tag", "responseText is = " + responseText);

                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainGalleryActivity.this);
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
        protected void onProgressUpdate(final JSONArray... values) {
            super.onProgressUpdate(values);

            GalleryCatAdapter adapter = new GalleryCatAdapter(values[0], MainGalleryActivity.this);
            listMainGallery.setAdapter(adapter);

            listMainGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //strImageCateID
                    JSONArray jArray;
                    try {
                        jArray = new JSONArray(String.valueOf(values[0]));
                        strEvenID = jArray.getJSONObject(position).getString(getString(R.string.key_eventId));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent galleryDetailIntent = new Intent(MainGalleryActivity.this, GalleryPhotosActivity.class);
                    galleryDetailIntent.putExtra("eventID", strEvenID);
                    galleryDetailIntent.putExtra("StudentID", strStudentId);
                    startActivity(galleryDetailIntent);
                }
            });
        }
    }
}
