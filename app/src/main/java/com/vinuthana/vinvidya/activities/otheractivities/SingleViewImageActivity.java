package com.vinuthana.vinvidya.activities.otheractivities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.SingleViewImageAdapter;

import org.json.JSONArray;

public class SingleViewImageActivity extends AppCompatActivity {
    ViewPager viewPager;
    SingleViewImageAdapter singleViewImageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view_image);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        Bundle extras = getIntent().getExtras();
        int pos = extras.getInt("position");
        try {
            JSONArray jsonArray = new JSONArray(extras.getString("array_list"));
            singleViewImageAdapter = new SingleViewImageAdapter(SingleViewImageActivity.this,jsonArray,pos);
            viewPager.setAdapter(singleViewImageAdapter);
            viewPager.setCurrentItem(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
