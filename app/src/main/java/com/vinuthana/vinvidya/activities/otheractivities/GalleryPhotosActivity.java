package com.vinuthana.vinvidya.activities.otheractivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryPhotosActivity extends AppCompatActivity {
    GridView gridImages;
    String strEventID, strStudentId;
    ArrayList<GridItem> gridItems;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.syllabustoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Details");
        if (!connection.netInfo(GalleryPhotosActivity.this)) {
            connection.buildDialog(GalleryPhotosActivity.this).show();
        } else {
            init();

            try {
                strEventID = getIntent().getExtras().getString("eventID");
                strStudentId = getIntent().getExtras().getString("StudentID");
                new GetGalleryDetails().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void init() {
        gridImages = (GridView) findViewById(R.id.grid_images);
        gridItems = new ArrayList<GridItem>();
    }

    class GetGalleryDetails extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "otherOperation.jsp";
        GetResponse response = new GetResponse();
        JSONObject outObject = new JSONObject();

        @Override
        protected Void doInBackground(String... params) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStudentGAlleryImagesByEventId));
                JSONObject userData = new JSONObject();
                userData.put("StudentId", strStudentId);
                userData.put(getString(R.string.key_eventId), strEventID);
                outObject.put(getString(R.string.key_otherData), userData);
                Log.e("Tag", "OutObject = " + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "respText is = " + responseText);

                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            JSONArray jArray;
            try {
                jArray = new JSONArray(String.valueOf(values[0]));
                if (jArray != null) {
                    GridViewAdapter adapter = new GridViewAdapter(GalleryPhotosActivity.this,jArray);
                    gridImages.setAdapter(adapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
