package com.vinuthana.vinvidya.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.CurrentParentNote;
import com.vinuthana.vinvidya.fragments.noticeBoardFragments.ParentNoteByDate;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class ParentNoteActivity extends AppCompatActivity {
    TabLayout tabParentNote;
    ViewPager vpParentNote;

    public String strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.parentNoteToolbar);
        setSupportActionBar(toolbar);
        tabParentNote = (TabLayout) findViewById(R.id.tabParentNote);
        vpParentNote = (ViewPager) findViewById(R.id.vpParentNote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Parent Note");
        setviewPage(vpParentNote);
        tabParentNote.setupWithViewPager(vpParentNote);

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
        hwadapter.addFragment(new CurrentParentNote(), "Current Parent Note");
        hwadapter.addFragment(new ParentNoteByDate(), "Parent Note By Date");
        viewPager.setAdapter(hwadapter);
    }
}
