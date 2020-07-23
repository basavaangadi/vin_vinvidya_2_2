package com.vinuthana.vinvidya.activities.examsection;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.fragments.examSectionFragments.CurrentExamSyllabus;
import com.vinuthana.vinvidya.fragments.examSectionFragments.ExamSyllabusByExam;
import com.vinuthana.vinvidya.fragments.examSectionFragments.MonthlyQuizSyllabusFragment;
import com.vinuthana.vinvidya.fragments.examSectionFragments.WeeklyQuizSyllabusFragment;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class ExamSyllabusActivity extends AppCompatActivity {
    ViewPager vpExamSyllabus;
    TabLayout tabExamSyllabus;

    public String strStudentId,strSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_syllabus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examSyllabusToolbar);
        setSupportActionBar(toolbar);
        tabExamSyllabus = (TabLayout) findViewById(R.id.tabExamSyllabus);
        vpExamSyllabus = (ViewPager) findViewById(R.id.vpExamSyllabus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Syllabus");


        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            strSchoolId = getIntent().getExtras().getString("schoolId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setviewPage(vpExamSyllabus);
        tabExamSyllabus.setupWithViewPager(vpExamSyllabus);
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
        if(!strSchoolId.equalsIgnoreCase("6")) {
            hwadapter.addFragment(new CurrentExamSyllabus(), "Current Exam Syllabus");
            hwadapter.addFragment(new ExamSyllabusByExam(), "Exam Syllabus By Exam");
        }else{
            hwadapter.addFragment(new WeeklyQuizSyllabusFragment(), "Weekly Quiz Syllabus");
            hwadapter.addFragment(new MonthlyQuizSyllabusFragment(), "Monthly Quiz Syllabus");
        }
        viewPager.setAdapter(hwadapter);
    }
}
