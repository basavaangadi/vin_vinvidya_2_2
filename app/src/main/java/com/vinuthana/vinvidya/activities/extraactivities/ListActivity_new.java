package com.vinuthana.vinvidya.activities.extraactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.daytoday.DaytoDay;
import com.vinuthana.vinvidya.activities.examsection.ExamSectionActivity;
import com.vinuthana.vinvidya.activities.noticeboard.NoticeboardActivity;
import com.vinuthana.vinvidya.activities.otheractivities.OthersActivity;
import com.vinuthana.vinvidya.activities.useractivities.AboutUsActivity;
import com.vinuthana.vinvidya.activities.useractivities.ChangePasswordActivity;
import com.vinuthana.vinvidya.activities.useractivities.ProfileActivity;
import com.vinuthana.vinvidya.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity_new extends AppCompatActivity {
    String[] title = {"Day To Day", "Notice Board", "Exam Section", "Others"};
    int[] images = {R.drawable.day_to_day, R.drawable.notice, R.drawable.assignment_black, R.drawable.ic_apps_black_48dp};
    ListView activityMenuListView;
    String strUsername, strEmail;
    TextView tvStatus, tvEmail;
    private Session session;
    String strStudId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.listActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vin Vidya");

        try {
            strStudId = getIntent().getExtras().getString("studentId");
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
        //tvEmail.setText(Html.fromHtml("Welcome <b> " + strEmail + "<b>"));

        activityMenuListView = (ListView) findViewById(R.id.activityMenuListView);
        CustomAdapter customAdapter = new CustomAdapter();
        activityMenuListView.setAdapter(customAdapter);

        activityMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Intent intentDaytoDay = new Intent(view.getContext(), DaytoDay.class);
                        intentDaytoDay.putExtra("studentId", strStudId);
                        startActivity(intentDaytoDay);
                        //view.getContext().startActivity(new Intent(view.getContext(), DaytoDay.class));
                        break;
                    case 1:
                        Intent intentNoticeboard = new Intent(view.getContext(), NoticeboardActivity.class);
                        intentNoticeboard.putExtra("studentId", strStudId);
                        startActivity(intentNoticeboard);
                        //view.getContext().startActivity(new Intent(view.getContext(), NoticeboardActivity.class));
                        break;
                    case 2:
                        Intent intentExamSection = new Intent(view.getContext(), ExamSectionActivity.class);
                        intentExamSection.putExtra("studentId", strStudId);
                        startActivity(intentExamSection);
                        //view.getContext().startActivity(new Intent(view.getContext(), ExamSectionActivity.class));
                        break;
                    case 3:
                        Intent intentOthers = new Intent(view.getContext(), OthersActivity.class);
                        intentOthers.putExtra("studentId", strStudId);
                        startActivity(intentOthers);
                        //view.getContext().startActivity(new Intent(view.getContext(), OthersActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        SharedPreferences preferences = getSharedPreferences("myapp", MODE_PRIVATE);
        strUsername = preferences.getString("UserName", "UNKNOWN");
        //createTables();
    }


    public void createTables() {
        /*CurrentAtndTable atndTable = new CurrentAtndTable();
        CurrentHomeWorkTable homeWorkTable = new CurrentHomeWorkTable();
        atndTable.attendanceTable();
        homeWorkTable.homeWorkTable();*/
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
                intent = new Intent(ListActivity_new.this, ProfileActivity.class);
                startActivity(intent);
                break;
           /* case R.id.action_aboutUs:
                intent = new Intent(ListActivity_new.this, AboutUsActivity.class);
                startActivity(intent);
                break;*/
            case R.id.action_changePassword:
                intent = new Intent(ListActivity_new.this, ChangePasswordActivity.class);
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

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.day_to_day, null);
            ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            TextView tvActivityTitle = (TextView) convertView.findViewById(R.id.tvActivityTitle);
            imageViewIcon.setImageResource(images[position]);
            tvActivityTitle.setText(title[position]);
            return convertView;
        }
    }
}
