package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by KISHAN on 16-09-17.
 */

public class HmWrkRecyclerViewAdapter extends RecyclerView.Adapter<HmWrkRecyclerViewAdapter.MyViewHolder> {
    JSONArray hmwrkArray;
    Context hmwrkContext;
    String strSchoolId;
    OnHomeworkClickListener onHomeworkClickListener;

    public HmWrkRecyclerViewAdapter(JSONArray hmwrkArray, Context hmwrkContext,String strSchoolId) {
        this.hmwrkArray = hmwrkArray;
        this.hmwrkContext = hmwrkContext;
        this.strSchoolId=strSchoolId;
    }

    @Override
    public HmWrkRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) hmwrkContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.hmwrk_card_view_layout, parent,false);
        return new MyViewHolder(view);
    }
    public void setOnButtonClickListener(OnHomeworkClickListener onHomeworkClickListener){
        this.onHomeworkClickListener=onHomeworkClickListener;
    }
    @Override
    public void onBindViewHolder(HmWrkRecyclerViewAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvSubjectHmCrd.setText(hmwrkArray.getJSONObject(position).getString("Subject"));
            holder.tvSubjectHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(!onHomeworkClickListener.equals(null)){

                            JSONObject object = hmwrkArray.getJSONObject(position);
                            Log.e("Homework subject", "noticeId ");
                            String strSubjectId =object.getString("SubjectId");
                            onHomeworkClickListener.getSyllabus(object,position,strSubjectId);

                        }

                        else {
                            Toast.makeText(hmwrkContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Toast.makeText(hmwrkContext, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
            if(strSchoolId.equalsIgnoreCase("6")){
                holder.tvSyllabusMessage.setVisibility(View.VISIBLE);
            }
            holder.tvHomeWorkHmCrd.setText(hmwrkArray.getJSONObject(position).getString("HomeWork"));
            holder.tvDateHmCrd.setText(hmwrkArray.getJSONObject(position).getString("Date"));
            holder.tvChapterHmCrd.setText(hmwrkArray.getJSONObject(position).getString("chapterName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hmwrkArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectHmCrd, tvHomeWorkHmCrd,tvDateHmCrd,tvChapterHmCrd,tvSyllabusMessage;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSubjectHmCrd = (TextView) itemView.findViewById(R.id.tvSubjectHmCrd);
            tvHomeWorkHmCrd = (TextView) itemView.findViewById(R.id.tvHomeWorkHmCrd);
            tvDateHmCrd = (TextView) itemView.findViewById(R.id.tvDateHmCrd);
            tvChapterHmCrd = (TextView) itemView.findViewById(R.id.tvChapterHmCrd);
            tvSyllabusMessage = (TextView) itemView.findViewById(R.id.tvSyllabusMessage);
        }
    }
    public interface OnHomeworkClickListener{
        public void getSyllabus(JSONObject homeworkData, int position, String strSubjectId);
        public void onDelete(int position,String noticeId);
    }
}