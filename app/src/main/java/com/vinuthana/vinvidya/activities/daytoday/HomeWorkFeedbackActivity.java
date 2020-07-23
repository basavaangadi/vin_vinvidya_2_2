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
import com.vinuthana.vinvidya.fragments.dayToDayFragments.CurHWFeedBack;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.HWFeedBackByDate;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class HomeWorkFeedbackActivity extends AppCompatActivity {
    TabLayout tabHomeWorkFeedBack;
    ViewPager vpHomeWorkFeedBack;
    String strStudentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.homeWrkFbToolbar);
        setSupportActionBar(toolbar);
        tabHomeWorkFeedBack = (TabLayout) findViewById(R.id.tabHomeWorkFeedBack);
        vpHomeWorkFeedBack = (ViewPager) findViewById(R.id.vpHomeWorkFeedBack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home Work Feedback");
        setviewPage(vpHomeWorkFeedBack);
        tabHomeWorkFeedBack.setupWithViewPager(vpHomeWorkFeedBack);

        try {
            strStudentId = getIntent().getExtras().getString("studentId");

            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());
        hwadapter.addFragment(new CurHWFeedBack(),"Current Home Work Feedback");
        hwadapter.addFragment(new HWFeedBackByDate(),"Home Work Feedback By Date");
        viewPager.setAdapter(hwadapter);
    }
}
