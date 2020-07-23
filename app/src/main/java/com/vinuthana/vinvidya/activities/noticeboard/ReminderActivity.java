package com.vinuthana.vinvidya.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.CurrentReminder;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.ReminderByDate;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class ReminderActivity extends AppCompatActivity {
    TabLayout tabReminder;
    ViewPager vpReminder;

    public String strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.reminderToolbar);
        setSupportActionBar(toolbar);
        tabReminder = (TabLayout) findViewById(R.id.tabReminder);
        vpReminder = (ViewPager) findViewById(R.id.vpReminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reminder");
        setviewPage(vpReminder);
        tabReminder.setupWithViewPager(vpReminder);

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
        hwadapter.addFragment(new CurrentReminder(), "Current Reminder");
        hwadapter.addFragment(new ReminderByDate(), "Reminder By Date");
        viewPager.setAdapter(hwadapter);
    }
}
