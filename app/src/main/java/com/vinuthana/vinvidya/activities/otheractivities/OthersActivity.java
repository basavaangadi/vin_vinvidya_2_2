package com.vinuthana.vinvidya.activities.otheractivities;

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

public class OthersActivity extends AppCompatActivity {
    String[] title = {"Events", "Gallery", "Syllabus"};
    int[] images = {R.drawable.ic_calendar_check_black_48dp, R.drawable.ic_folder_multiple_image_black_48dp, R.drawable.ic_book_open_variant_black_48dp};

    String strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        Toolbar toolbar = (Toolbar) findViewById(R.id.othersToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Others");
        ListView otherListView = (ListView) findViewById(R.id.otherListView);
        OtherAdapter otherAdapter = new OtherAdapter();
        otherListView.setAdapter(otherAdapter);

        try {
            strStudentId = getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        otherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentEvents = new Intent(view.getContext(), EventsActivity.class);
                        intentEvents.putExtra("studentId", strStudentId);
                        startActivity(intentEvents);
                        // view.getContext().startActivity(new Intent(view.getContext(), EventsActivity.class));
                        break;
                    case 1:
                        Intent intentMainGallery = new Intent(view.getContext(), MainGalleryActivity.class);
                        intentMainGallery.putExtra("studentId", strStudentId);
                        startActivity(intentMainGallery);

                        //view.getContext().startActivity(new Intent(view.getContext(), GalleryActivity.class));

                        break;
                    case 2:

                        Intent intentSyllabus = new Intent(view.getContext(), SyllabusActivity.class);
                        intentSyllabus.putExtra("studentId", strStudentId);
                        startActivity(intentSyllabus);

                        //view.getContext().startActivity(new Intent(view.getContext(), SyllabusActivity.class));
                        break;
                    default:
                        break;
                    //Toast.makeText(ListActivity_new.this, "the postion is" + position, Toast.LENGTH_SHORT).show();
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

    class OtherAdapter extends BaseAdapter {

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
