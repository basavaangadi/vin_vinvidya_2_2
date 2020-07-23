package com.vinuthana.vinvidya.activities.noticeboard;

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
import com.vinuthana.vinvidya.activities.examsection.ExamScheduleActivity;

public class NoticeboardActivity extends AppCompatActivity {
    String[] title = {"Notice", "Parent note", "Reminder"};
    int[] images = {R.drawable.ic_clipboard_outline_black_48dp, R.drawable.people_connect_icon, R.drawable.ic_bell_ring_black_36dp};
    String strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.noticeBoardToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice Board");
        ListView noticeBoardListView = (ListView) findViewById(R.id.noticeBoardListView);
        NoticeAdapter noticeAdapter = new NoticeAdapter();
        noticeBoardListView.setAdapter(noticeAdapter);

        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        noticeBoardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentNotice = new Intent(view.getContext(), NoticeActivity.class);
                        intentNotice.putExtra("studentId", strStudentId);
                        startActivity(intentNotice);
                        // view.getContext().startActivity(new Intent(view.getContext(), NoticeActivity.class));
                        break;
                    case 1:
                        Intent intentParentNote = new Intent(view.getContext(), ParentNoteActivity.class);
                        intentParentNote.putExtra("studentId", strStudentId);
                        startActivity(intentParentNote);
                        //view.getContext().startActivity(new Intent(view.getContext(), ParentNoteActivity.class));
                        break;
                    case 2:
                        Intent intentReminder = new Intent(view.getContext(), ReminderActivity.class);
                        intentReminder.putExtra("studentId", strStudentId);
                        startActivity(intentReminder);
                        //view.getContext().startActivity(new Intent(view.getContext(), ReminderActivity.class));
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

    class NoticeAdapter extends BaseAdapter {

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
