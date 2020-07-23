package com.vinuthana.vinvidya.activities.examsection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.daytoday.AttendanceActivity;

public class ExamSectionActivity extends AppCompatActivity {
    String[] title = {"Exam Schedule", "Exam Syllabus", "Exam Marks"};
    int[] images = {R.drawable.ic_calendar_multiple_check_black_36dp, R.drawable.ic_book_open_variant_black_36dp, R.drawable.ic_format_list_numbers_black_36dp};
    String strStudentId,strSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examSectoinToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Section");
        ListView examSectionListView = (ListView) findViewById(R.id.examSectionListView);
        ExamSectionAdapter adapter = new ExamSectionAdapter();
        examSectionListView.setAdapter(adapter);

        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            strSchoolId=getIntent().getExtras().getString("schoolId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        examSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        Intent intentExamSchedule = new Intent(view.getContext(), ExamScheduleActivity.class);
                        intentExamSchedule.putExtra("studentId", strStudentId);
                        intentExamSchedule.putExtra("schoolId", strSchoolId);
                        startActivity(intentExamSchedule);
                        //view.getContext().startActivity(new Intent(view.getContext(), ExamScheduleActivity.class));
                        break;
                    case 1:

                        Intent intentExamSyllabus = new Intent(view.getContext(), ExamSyllabusActivity.class);
                        intentExamSyllabus.putExtra("studentId", strStudentId);
                        intentExamSyllabus.putExtra("schoolId", strSchoolId);
                        startActivity(intentExamSyllabus);

                        // view.getContext().startActivity(new Intent(view.getContext(), ExamSyllabusActivity.class));
                        break;
                    case 2:

                        Intent intentExamMarks = new Intent(view.getContext(), ExamMarksActivity.class);
                        intentExamMarks.putExtra("studentId", strStudentId);
                        intentExamMarks.putExtra("schoolId", strSchoolId);
                        startActivity(intentExamMarks);

                        //view.getContext().startActivity(new Intent(view.getContext(), ExamMarksActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
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

    class ExamSectionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.day_to_day, null);
            ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            TextView tvActivityTitle = (TextView) convertView.findViewById(R.id.tvActivityTitle);
            imageViewIcon.setImageResource(images[position]);
            tvActivityTitle.setText(title[position]);
            return convertView;
        }
    }
}
