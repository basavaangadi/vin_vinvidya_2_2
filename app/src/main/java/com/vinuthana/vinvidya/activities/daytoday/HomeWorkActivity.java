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
import com.vinuthana.vinvidya.fragments.dayToDayFragments.CurrentHomeWork;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.HomeWorkByDate;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class HomeWorkActivity extends AppCompatActivity {
    TabLayout tabHomeWork;
    ViewPager vpHomwWork;
    public String strStudId,strSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);
        Toolbar toolbar = (Toolbar) findViewById(R.id.homeWrkToolbar);
        setSupportActionBar(toolbar);
        tabHomeWork = (TabLayout) findViewById(R.id.tabHomeWork);
        vpHomwWork = (ViewPager) findViewById(R.id.vpHomeWork);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home Work");
        setviewPage(vpHomwWork);
        tabHomeWork.setupWithViewPager(vpHomwWork);

        try {
            strStudId = getIntent().getExtras().getString("studentId");
            strSchoolId=getIntent().getExtras().getString("schoolId");
        } catch (Exception e) {
            e.printStackTrace();
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
        hwadapter.addFragment(new CurrentHomeWork(), "Current Home Work");
        hwadapter.addFragment(new HomeWorkByDate(), "Home Work By Date");
        viewPager.setAdapter(hwadapter);
    }
}
