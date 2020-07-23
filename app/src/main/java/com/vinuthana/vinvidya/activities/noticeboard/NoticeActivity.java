package com.vinuthana.vinvidya.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.CurrentNotice;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.NoticeByDate;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class NoticeActivity extends AppCompatActivity {
    TabLayout tabNotice;
    ViewPager vpNotice;

    public String strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.noticeActivityToolbar);
        setSupportActionBar(toolbar);
        tabNotice = (TabLayout) findViewById(R.id.tabNotice);
        vpNotice = (ViewPager) findViewById(R.id.vpNotice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");
        setviewPage(vpNotice);
        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabNotice.setupWithViewPager(vpNotice);

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
        hwadapter.addFragment(new CurrentNotice(), "Current Notice");
        hwadapter.addFragment(new NoticeByDate(), "Notice By Date");
        viewPager.setAdapter(hwadapter);
    }
}
