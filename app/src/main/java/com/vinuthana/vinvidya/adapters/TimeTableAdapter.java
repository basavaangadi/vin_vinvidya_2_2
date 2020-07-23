package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Krish on 14-10-2017.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.MyViewHolder> {
    JSONArray timeTableArray;
    Context timeTableContext;

    public TimeTableAdapter(JSONArray timeTableArray, Context timeTableContext) {
        this.timeTableArray = timeTableArray;
        this.timeTableContext = timeTableContext;
    }

    @Override
    public TimeTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_card_layout, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeTableAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvTimeTableSTime.setText(timeTableArray.getJSONObject(position).getString("StartTime"));
            holder.tvTimeTableSubject.setText(timeTableArray.getJSONObject(position).getString("Subject"));
           // holder.tvTimeTableTeacher.setText(timeTableArray.getJSONObject(position).getString("StaffName"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return timeTableArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeTableSTime, tvTimeTableETime, tvTimeTableSubject, tvTimeTableTeacher;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTimeTableSTime = (TextView) itemView.findViewById(R.id.tvTimeTableSTime);
            tvTimeTableSubject = (TextView) itemView.findViewById(R.id.tvTimeTableSubject);
           // tvTimeTableTeacher = (TextView) itemView.findViewById(R.id.tvTimeTableTeacher);
        }
    }
}
