package com.vinuthana.vinvidya.activities.noticeboard;

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
import com.vinuthana.vinvidya.fragments.dayToDayFragments.AttendanceByDate;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.CurrentAttendance;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.LeaveRequestFragment;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.ClassNoticeByDateFragment;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.CurrentClassNoticeFragment;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class ClassNoticeActivity extends AppCompatActivity {
    TabLayout tabClassNotice;
    ViewPager vpClassNotice;
    String strStudentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Class Notice");
        vpClassNotice=findViewById(R.id.vpClassNotice);
        tabClassNotice=findViewById(R.id.tabClassNotice);
        try {
            strStudentId = getIntent().getExtras().getString("studentId");

            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Class Notice");
        setviewPage(vpClassNotice);
        tabClassNotice.setupWithViewPager(vpClassNotice);



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
        VPager vPagerAdapter = new VPager(getSupportFragmentManager());
        vPagerAdapter.addFragment(new CurrentClassNoticeFragment(),"Current class notice");
        vPagerAdapter.addFragment(new ClassNoticeByDateFragment(), "Class notice By Date");
        viewPager.setAdapter(vPagerAdapter);
    }
}
