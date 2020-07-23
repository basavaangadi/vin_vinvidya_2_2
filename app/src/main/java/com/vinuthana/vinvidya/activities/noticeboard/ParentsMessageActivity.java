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
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.CurrentNotice;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.CurrentParentsMessageFragment;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.NoticeByDate;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.ParentsMessageByDateFragment;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.SendParentMessageFragment;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class ParentsMessageActivity extends AppCompatActivity {

    TabLayout tabLytParentsMessage;
    ViewPager vpParentsMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Parents Message");
        tabLytParentsMessage=findViewById(R.id.tabLytParentsMessage);
        vpParentsMessage=findViewById(R.id.vpParentsMessage);
        setviewPage(vpParentsMessage);
        tabLytParentsMessage.setupWithViewPager(vpParentsMessage);
    }
    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());
        hwadapter.addFragment(new SendParentMessageFragment(), "Send Message");
        hwadapter.addFragment(new CurrentParentsMessageFragment(), "Current Message");
        hwadapter.addFragment(new ParentsMessageByDateFragment(), "Message By Date");
        viewPager.setAdapter(hwadapter);
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
}
