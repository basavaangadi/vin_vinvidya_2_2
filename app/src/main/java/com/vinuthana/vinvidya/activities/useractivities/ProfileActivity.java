package com.vinuthana.vinvidya.activities.useractivities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.Session;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Session session;
    TextView tvProfileFName, tvProfileUserName, tvProfilePNo, tvProfileEmail, tvProfileNoChild;
    String strFName, strProfileName, strPhoneNum, strEmail, strNumChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        initialisation();
        //allEvents();

        session = new Session(getApplicationContext());

        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        strFName = user.get(Session.KEY_NAME);
        //strProfileName = user.get(Session.KEY_USER_NAME);
        strPhoneNum = user.get(Session.KEY_PHONE_NO);
        strEmail = user.get(Session.KEY_EMAIL);
        strNumChild = user.get(Session.KEY_NUM_CHILD);

        tvProfileFName.setText(strFName);
       // tvProfileUserName.setText(strProfileName);
        tvProfilePNo.setText(strPhoneNum);
        tvProfileEmail.setText(strEmail);
        tvProfileNoChild.setText(strNumChild);
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

    public void initialisation() {
        tvProfileFName = (TextView) findViewById(R.id.tvProfileFName);
        //tvProfileUserName = (TextView) findViewById(R.id.tvProfileUserName);
        tvProfilePNo = (TextView) findViewById(R.id.tvProfilePNo);
        tvProfileEmail = (TextView) findViewById(R.id.tvProfileEmail);
        tvProfileNoChild = (TextView) findViewById(R.id.tvProfileNoChild);
    }
}
