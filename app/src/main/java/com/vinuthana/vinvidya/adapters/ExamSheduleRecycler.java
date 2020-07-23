package com.vinuthana.vinvidya.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;

import org.json.JSONArray;

/**
 * Created by KISHAN on 16-09-17.
 */

public class ExamSheduleRecycler extends RecyclerView.Adapter<ExamSheduleRecycler.MyViewHolder> {
    JSONArray exmschArray;
    Activity exmschcContext;
    String strDate, strTime, strDateTime;

    public ExamSheduleRecycler(JSONArray prntntcArray, Activity exmschcContext) {
        this.exmschArray = prntntcArray;
        this.exmschcContext = exmschcContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmschcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exm_sch_card_view_layout, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {

           // holder.tvExmSchedule.setText("Exam: " + exmschArray.getJSONObject(position).getString(exmschcContext.getString(R.string.key_Exam)));
            strDate = exmschArray.getJSONObject(position).getString(exmschcContext.getString(R.string.key_Date));
            strTime = exmschArray.getJSONObject(position).getString(exmschcContext.getString(R.string.key_ExamTime));
            strDateTime = strDate + " " + strTime;
            holder.tvDateExmSchedule.setText( strDate);
            holder.tvExmTime.setText( strTime);
            //holder.tvTimeExmSchedule.setText("Time: " + exmschArray.getJSONObject(position).getString(exmschcContext.getString(R.string.key_ExamTime)));
            holder.tvSubExmSchedule.setText(exmschArray.getJSONObject(position).getString(exmschcContext.getString(R.string.key_Subject)));
        } catch (Exception e) {
            Log.e("Schedule onBindview",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return exmschArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateExmSchedule, tvSubExmSchedule, tvExmTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDateExmSchedule = (TextView) itemView.findViewById(R.id.tvDateExmSchedule);
            tvExmTime = (TextView) itemView.findViewById(R.id.tvExmTime);
            tvSubExmSchedule = (TextView) itemView.findViewById(R.id.tvSubExmSchedule);
        }
    }
}
