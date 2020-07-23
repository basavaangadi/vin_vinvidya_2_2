package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 12/9/2017.
 */

public class HwrkFbRecyclerViewAdapter extends RecyclerView.Adapter<HwrkFbRecyclerViewAdapter.MyViewHolder> {
    JSONArray hmwrkFbArray;
    Context hmwrkFbContext;

    public HwrkFbRecyclerViewAdapter(JSONArray hmwrkFbArray, Context hmwrkFbContext) {
        this.hmwrkFbArray = hmwrkFbArray;
        this.hmwrkFbContext = hmwrkFbContext;
    }

    @Override
    public HwrkFbRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) hmwrkFbContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.hw_feedback_layout,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HwrkFbRecyclerViewAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject hmFDBckObject=hmwrkFbArray.getJSONObject(position);
            String strSubject=hmFDBckObject.getString("Subject");
            String strStatus=hmFDBckObject.getString("Status");
            String strFeedbackDate=hmFDBckObject.getString("FeedbackDate");
            String strhomeworkDate=hmFDBckObject.getString("homeworkDate");

            /*holder.tvCurHWFbSub.setText(hmwrkFbArray.getJSONObject(position).getString("Subject"));
            //holder.tvCurHWFbHmChptName.setText(hmwrkFbArray.getJSONObject(position).getString("ChapterName"));
            //holder.tvCurHWFbHmDescription.setText(hmwrkFbArray.getJSONObject(position).getString("Description"));
            holder.tvHWFbStatus.setText(hmwrkFbArray.getJSONObject(position).getString("Status"));
            holder.tvCurHwFbDt.setText(hmwrkFbArray.getJSONObject(position).getString("FeedbackDate"));
            holder.tvCurHWFbHmDt.setText(hmwrkFbArray.getJSONObject(position).getString("homeworkDate"));*/

            holder.tvCurHWFbSub.setText(strSubject);
            holder.tvHWFbStatus.setText(strStatus);
            if(strStatus.equalsIgnoreCase("Not Done")){
                holder.tvHWFbStatus.setTextColor(Color.parseColor("#FF8C00"));
            }
            else  {
                holder.tvHWFbStatus.setTextColor(Color.parseColor("#00b300"));
                //holder.lynrLyt.setBackgroundColor(Color.parseColor("#00ff00"));
            }
            //holder.tvCurHWFbHmChptName.setText(hmwrkFbArray.getJSONObject(position).getString("ChapterName"));
            //holder.tvCurHWFbHmDescription.setText(hmwrkFbArray.getJSONObject(position).getString("Description"));

            holder.tvCurHwFbDt.setText(strFeedbackDate);
            holder.tvCurHWFbHmDt.setText(strhomeworkDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hmwrkFbArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHWFbStatus, tvCurHWFbHmDt, tvCurHwFbDt,tvCurHWFbSub,tvCurHWFbHmChptName,tvCurHWFbHmDescription;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvCurHWFbSub = (TextView) itemView.findViewById(R.id.tvCurHWFbSub);
            tvHWFbStatus = (TextView) itemView.findViewById(R.id.tvHWFbStatus);
            tvCurHWFbHmDt = (TextView) itemView.findViewById(R.id.tvCurHWFbHmDt);
            tvCurHwFbDt = (TextView) itemView.findViewById(R.id.tvCurHwFbDt);
            /*tvCurHWFbHmChptName = (TextView) itemView.findViewById(R.id.tvCurHWFbHmChptName);
            tvCurHWFbHmDescription = (TextView) itemView.findViewById(R.id.tvCurHWFbHmDescription);*/
        }
    }
}