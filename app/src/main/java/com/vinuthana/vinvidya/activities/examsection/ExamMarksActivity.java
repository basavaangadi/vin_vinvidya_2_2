package com.vinuthana.vinvidya.activities.examsection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vinuthana.vinvidya.fragments.examSectionFragments.CurrentExamMarks;
import  com.vinuthana.vinvidya.fragments.examSectionFragments.CurrentExamMarksGDGB;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.fragments.examSectionFragments.CurrentExamMarksGDGB;
import com.vinuthana.vinvidya.fragments.examSectionFragments.CurrentExamMarksRVK;
import com.vinuthana.vinvidya.fragments.examSectionFragments.CurrentExmMrkFragment;
import com.vinuthana.vinvidya.fragments.examSectionFragments.ExamMarksByExam;
import com.vinuthana.vinvidya.fragments.examSectionFragments.ExamMarksByExamFragment;
import com.vinuthana.vinvidya.fragments.examSectionFragments.ExamMarksByExamGDGB;
import com.vinuthana.vinvidya.fragments.examSectionFragments.ExamMarksByExamRVK;
import com.vinuthana.vinvidya.fragments.examSectionFragments.MonthlyQuizMarksFragment;
import com.vinuthana.vinvidya.fragments.examSectionFragments.WeeklyQuizMarksFragment;
import com.vinuthana.vinvidya.viewPagerAdapter.VPager;

public class ExamMarksActivity extends AppCompatActivity {

    ViewPager vpExamMarks;
    TabLayout tabExamMarks;
    public String strStudentId,strSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_marks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examMarkstoolbar);
        setSupportActionBar(toolbar);
        vpExamMarks = (ViewPager) findViewById(R.id.vpExamMarks);
        tabExamMarks = (TabLayout) findViewById(R.id.tabExamMarks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Marks");
        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            strSchoolId=getIntent().getExtras().getString("schoolId");
        } catch (Exception e) {
            Log.e("onCreate Exception",e.toString());
        }
        if(!strSchoolId.equalsIgnoreCase(null)){
            setviewPage(vpExamMarks);
            tabExamMarks.setupWithViewPager(vpExamMarks);
        }else{
            Toast.makeText(this, "strSchoolId is null", Toast.LENGTH_SHORT).show();
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
    public String fetchSchoolId(){
        return strSchoolId;
    }
    public  String fetchStudentId(){
        return strStudentId;
    }

    private OnAboutDataReceivedListener mAboutDataListener;

    public interface OnAboutDataReceivedListener {
        void onDataReceived(String strSchoolId,String strStudentId);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());
        int schoolId=Integer.parseInt(strSchoolId);
        if(schoolId==2||schoolId==13||schoolId==11||schoolId==5){

            hwadapter.addFragment(new CurrentExamMarksGDGB(), "Current Exam Marks");
            hwadapter.addFragment(new ExamMarksByExamGDGB(), "Exam Marks By Exam");
        }else if(schoolId==3||schoolId==4) {
            hwadapter.addFragment(new CurrentExamMarksRVK(), "Current Exam Marks");
            hwadapter.addFragment(new ExamMarksByExamRVK(), "Exam Marks By Exam");

        }else if(schoolId==6)  {
            hwadapter.addFragment(new WeeklyQuizMarksFragment(), "Weekly Quiz Marks");
            hwadapter.addFragment(new MonthlyQuizMarksFragment(), "Monthly Quiz Marks");
        }
        else {
            hwadapter.addFragment(new CurrentExmMrkFragment(), "Current Exam Marks");
            hwadapter.addFragment(new ExamMarksByExam(), "Exam Marks By Exam");
        }
        viewPager.setAdapter(hwadapter);
    }
}
