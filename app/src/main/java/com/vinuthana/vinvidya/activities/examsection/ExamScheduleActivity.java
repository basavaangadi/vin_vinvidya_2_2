package com.vinuthana.vinvidya.activities.examsection;

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
import com.vinuthana.vinvidya.fragments.examSectionFragments.CurrentExamSchedule;
import com.vinuthana.vinvidya.fragments.examSectionFragments.MonthlyQuizScheduleFragment;
import com.vinuthana.vinvidya.fragments.examSectionFragments.ScheduleByExam;
import com.vinuthana.vinvidya.fragments.examSectionFragments.WeeklyQuizScheduleFragment;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class ExamScheduleActivity extends AppCompatActivity {
    TabLayout tabExamSchedule;
    ViewPager vpExamSchedule;
    public String strStudentId,strSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examScheduleToolbar);
        setSupportActionBar(toolbar);
        tabExamSchedule = (TabLayout) findViewById(R.id.tabExamSchedule);
        vpExamSchedule = (ViewPager) findViewById(R.id.vpExamSchedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Schedule");


        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            strSchoolId = getIntent().getExtras().getString("schoolId");

            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setviewPage(vpExamSchedule);
        tabExamSchedule.setupWithViewPager(vpExamSchedule);
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
        if(!strSchoolId.equalsIgnoreCase("6")){
            hwadapter.addFragment(new CurrentExamSchedule(), "Current Exam Schedule");
            hwadapter.addFragment(new ScheduleByExam(), "Schedule By Exam");
        }else{
            hwadapter.addFragment(new WeeklyQuizScheduleFragment(), "Weekly Quiz Schedule");
            hwadapter.addFragment(new MonthlyQuizScheduleFragment(), "Monthly Quiz Schedule");
        }

        viewPager.setAdapter(hwadapter);
    }
}
