package com.vinuthana.vinvidya.activities.daytoday;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.useractivities.LoginActivity;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.AttendanceByDate;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.CurrentAttendance;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.LeaveRequestFragment;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.ViewLeaveRequestFragment;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class AttendanceActivity extends AppCompatActivity {
    TabLayout tabAttendance;
    ViewPager vpAttendance;
    public String strStudentId;
    CheckConnection connection = new CheckConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        if (!connection.netInfo(AttendanceActivity.this)) {
            connection.buildDialog(AttendanceActivity.this).show();
        }else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.atndToolbar);
            setSupportActionBar(toolbar);
            tabAttendance = (TabLayout) findViewById(R.id.tabAttendance);
            vpAttendance = (ViewPager) findViewById(R.id.vpAttendance);
            try {
                strStudentId = getIntent().getExtras().getString("studentId");

                //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Attendance");
            setviewPage(vpAttendance);
            tabAttendance.setupWithViewPager(vpAttendance);


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

    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());

        hwadapter.addFragment(new CurrentAttendance(), "Current Attendance");
        hwadapter.addFragment(new AttendanceByDate(), "Attendance By Date");
        hwadapter.addFragment(new LeaveRequestFragment(),"Leave Request");
        hwadapter.addFragment(new ViewLeaveRequestFragment(),"view Leaves");
        viewPager.setAdapter(hwadapter);
    }
}