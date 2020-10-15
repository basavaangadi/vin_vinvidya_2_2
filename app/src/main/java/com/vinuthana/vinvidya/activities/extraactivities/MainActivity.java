package com.vinuthana.vinvidya.activities.extraactivities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.useractivities.AboutUsActivity;
import com.vinuthana.vinvidya.activities.useractivities.ChangePasswordActivity;
import com.vinuthana.vinvidya.activities.useractivities.ProfileActivity;
import com.vinuthana.vinvidya.adapters.RecyclerAdapterDayToDay;
import com.vinuthana.vinvidya.adapters.RviewAdapterExamSection;
import com.vinuthana.vinvidya.adapters.RviewAdapterNoticeBoard;
import com.vinuthana.vinvidya.adapters.RviewAdapterOthers;
import com.vinuthana.vinvidya.fcm.FCMRegistrationIntentService;
import com.vinuthana.vinvidya.fcm.FCMUtils;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.Session;
import com.vinuthana.vinvidya.utils.StudentSPreference;
import com.vinuthana.vinvidya.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String strStudentId;
    String strUsername, strEmail,strToken;
    TextView tvStatus, tvEmail;
    int versionCode=1;
    private Session session;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    int appVersionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;
    public String strStudId,strSchoolId;
    StudentSPreference studentSPreference;
    CheckConnection connection = new CheckConnection();
    HashMap<String, String> hashMap = new HashMap<String, String>();
    RecyclerView rViewDayTODay, rViewNoticeBoard, rViewExamSection, rViewOthers;
    ArrayList<String> titleDayToDay = new ArrayList<>(Arrays.asList("Attendance", "Home Work", "Home Work FeedBack", "Time Table"));
    ArrayList<Integer> imagesDayToDay = new ArrayList<>(Arrays.asList(R.drawable.day_to_day, R.drawable.notice, R.drawable.assignment_black, R.drawable.ic_apps_black_48dp));

    ArrayList<String> titleNoticeBoard = new ArrayList<>(Arrays.asList("Notice", "Parent note","Class Notice" ,"Reminder"));
    ArrayList<Integer> imagesNoticeBoard = new ArrayList<>(Arrays.asList(R.drawable.ic_clipboard_outline_black_48dp, R.drawable.people_connect_icon, R.drawable.presentation,R.drawable.ic_bell_ring_black_36dp));

    ArrayList<String> titleExamSection = new ArrayList<>(Arrays.asList("Exam Schedule", "Exam Syllabus", "Exam Marks","Write Exam"));
    ArrayList<Integer> imagesExamSection = new ArrayList<>(Arrays.asList(R.drawable.ic_calendar_multiple_check_black_36dp, R.drawable.ic_book_open_variant_black_36dp, R.drawable.ic_format_list_numbers_black_36dp,R.drawable.ic_photo_black_24dp));

    ArrayList<String> titleOthers = new ArrayList<>(Arrays.asList("Events", "Gallery", "Syllabus","Assignment","Fees Section","Chapter Videos"));
    ArrayList<Integer> imagesOthers = new ArrayList<>(Arrays.asList(R.drawable.ic_calendar_check_black_48dp, R.drawable.ic_folder_multiple_image_black_48dp, R.drawable.ic_book_open_variant_black_48dp,R.drawable.ic_assignment_24dp,R.drawable.ic_attach_money_black_24dp,R.drawable.ic_play_circle_outline_black_24dp));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vin Vidya");
        new CheckRecentVersion().execute();

        if (!connection.netInfo(MainActivity.this)) {
            connection.buildDialog(MainActivity.this).show();
        }/*else if(versionCode<appVersionCode){
            Toast.makeText(MainActivity.this, "Please Update", Toast.LENGTH_SHORT).show();
            Intent inactiv= new Intent(MainActivity.this,UpdateActivity.class);
            startActivity(inactiv);
             finish();

        }*/
            else{

        initialisation();
        recycler();

        try {
            strStudId = getIntent().getExtras().getString("studentId");
            strSchoolId = getIntent().getExtras().getString("schoolId");

            FirebaseMessaging.getInstance().subscribeToTopic(strSchoolId)


                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.e("subscribed to  ", strSchoolId);
                           /* Toast toast= Toast.makeText(MainActivity.this,"User Registered for school ",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();*/
                        }
                    });


            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        session = new Session(this);
        //Toast.makeText(ListActivity_new.this, "User Login Status: " + session.loggedin(), Toast.LENGTH_SHORT).show();
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        ArrayList<String> strUser = new ArrayList<>();
        //int size =

        strUsername = user.get(Session.KEY_NAME);
        //strEmail = user.get(Session.KEY_EMAIL);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        for (int i = 0; i < strUser.size(); i++) {
            //strUser.add(session.getUserDetails())
        }
        //tvEmail = (TextView) findViewById(R.id.tvEmail);

        tvStatus.setText(Html.fromHtml("Welcome <b> " + strUsername + "<b>"));

        SharedPreferences preferences = getSharedPreferences("myapp", MODE_PRIVATE);
        strUsername = preferences.getString("UserName", "UNKNOWN");
         }
    }

    public void recycler() {
        /*Day To Day Start*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewDayTODay.setLayoutManager(linearLayoutManager);
        RecyclerAdapterDayToDay rcAdapter = new RecyclerAdapterDayToDay(MainActivity.this, titleDayToDay, imagesDayToDay);
        rViewDayTODay.setAdapter(rcAdapter);
        /*Day To Day End*/

        /*Notice Board Start*/
        LinearLayoutManager managerNoticeBoard = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewNoticeBoard.setLayoutManager(managerNoticeBoard);
        RviewAdapterNoticeBoard rcAdapterNoticeBoard = new RviewAdapterNoticeBoard(MainActivity.this, titleNoticeBoard, imagesNoticeBoard);
        rViewNoticeBoard.setAdapter(rcAdapterNoticeBoard);
        /*Notice Board End*/

        /*Exam Section Start*/
        LinearLayoutManager managerExamSection = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewExamSection.setLayoutManager(managerExamSection);
        RviewAdapterExamSection rcAdapterExamSection = new RviewAdapterExamSection(MainActivity.this, titleExamSection, imagesExamSection);
        rViewExamSection.setAdapter(rcAdapterExamSection);
        /*Exam Section End*/

        /*Others Start*/
        LinearLayoutManager managerOthers = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewOthers.setLayoutManager(managerOthers);
        RviewAdapterOthers rcAdapterOthers = new RviewAdapterOthers(MainActivity.this, titleOthers, imagesOthers);
        rViewOthers.setAdapter(rcAdapterOthers);
        /*Others End*/
    }

    private void initialisation() {
        rViewDayTODay = (RecyclerView) findViewById(R.id.recyclerViewDayToDay);
        rViewNoticeBoard = (RecyclerView) findViewById(R.id.recyclerviewNoticeBoard);
        rViewExamSection = (RecyclerView) findViewById(R.id.recyclerviewExamSection);
        rViewOthers = (RecyclerView) findViewById(R.id.recyclerviewOthers);
        mRegistrationBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equalsIgnoreCase(FCMUtils.messages.REG_SUCCESS)){
                    strToken=intent.getStringExtra("token");

                    if (strToken != null) {
                        Log.e("TAG", strToken+"Length = "+strToken.length());
                    }
                } else if (intent.getAction().equals(FCMUtils.messages.REG_ERROR)) {
                    Log.e("TAG", "Error");


                }
            }
        };


        Intent intent = new Intent(this, FCMRegistrationIntentService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FCMUtils.messages.REG_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FCMUtils.messages.REG_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_profile:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
          /*  case R.id.action_aboutUs:
                intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;*/
            case R.id.action_changePassword:
                intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                session.logOut();
                finish();
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class  CheckRecentVersion extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "userOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait Checking user...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_checkRecentAppVersion));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_App), "1");
                outObject.put(getString(R.string.key_userData), userData);

                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);


                Log.e("Tag", "responseText is =" + responseText);

                versionCode = inObject.getInt(getString(R.string.key_VersionCode));
                progressDialog.dismiss();
                if(appVersionCode <versionCode){
                    //  Toast.makeText(MainActivity.this, "Your Account is inactive", Toast.LENGTH_SHORT).show();
                    Intent inactiv= new Intent(MainActivity.this,UpdateActivity.class);
                    startActivity(inactiv);
                    finish();
                }

            } catch (Exception ex) {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(MainActivity.this, "check internet if internet strength is good check then also problem exist contact service provider", Toast.LENGTH_SHORT).show();
                        //update ui here
                        // display toast here
                    }
                });
                // Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }

            return null;
        }


    }

}
