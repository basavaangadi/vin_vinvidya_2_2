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
import com.vinuthana.vinvidya.fragments.dayToDayFragments.FridayFragment;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.MondayFragment;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.SaturdayFragment;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.SundayFragment;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.ThursdayFragment;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.TuesdayFragment;
import com.vinuthana.vinvidya.fragments.dayToDayFragments.WednesdayFragment;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class TimeTableActivity extends AppCompatActivity {

    TabLayout tabTimeTable;
    ViewPager vpTimeTable;
    String strStudentId,strSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.timeTableToolbar);
        setSupportActionBar(toolbar);
        tabTimeTable = (TabLayout) findViewById(R.id.tabTimeTable);
        vpTimeTable = (ViewPager) findViewById(R.id.vpTimeTable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Time Table");


        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            strSchoolId = getIntent().getExtras().getString("schoolId");

            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setviewPage(vpTimeTable);
        tabTimeTable.setupWithViewPager(vpTimeTable);
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
        hwadapter.addFragment(new MondayFragment(), "Mon");
        hwadapter.addFragment(new TuesdayFragment(), "Tue");
        hwadapter.addFragment(new WednesdayFragment(), "Wed");
        hwadapter.addFragment(new ThursdayFragment(), "Thu");
        hwadapter.addFragment(new FridayFragment(), "Fri");
        hwadapter.addFragment(new SaturdayFragment(), "Sat");
       // int sch=Integer.parseInt(strSchoolId);
        if(strSchoolId.equalsIgnoreCase("6"))
        {
            hwadapter.addFragment(new SundayFragment(), "Sun");
        }
        viewPager.setAdapter(hwadapter);
    }
}
