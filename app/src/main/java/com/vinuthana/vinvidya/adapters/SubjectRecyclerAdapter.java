package com.vinuthana.vinvidya.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.otheractivities.SubjectSyllabusActivity;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Krish on 04-10-2017.
 */

public class SubjectRecyclerAdapter extends RecyclerView.Adapter<SubjectRecyclerAdapter.MyViewHolder> {
    JSONArray subArray;
    Activity subContext;
    String subTitle, strsubheading,strSubjectId,strClass,strStudentId;
    ArrayList<String> strSubjectsList = new ArrayList<>();
    ArrayList<String> strSubjectIdList = new ArrayList<>();
    public SubjectRecyclerAdapter(JSONArray subArray, Activity subContext,String strStudentId) {
        this.subArray = subArray;
        this.subContext = subContext;
        this.strStudentId=strStudentId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.syllabus_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectRecyclerAdapter.MyViewHolder holder, int position) {

        try {

            strsubheading = subArray.getJSONObject(position).getString("Subject").toString();
            strSubjectsList.add(new String(strsubheading));
            strSubjectId=subArray.getJSONObject(position).getString("SubjectId").toString();
            strSubjectIdList.add(new String( strSubjectId));
            strClass=subArray.getJSONObject(position).getString("class").toString();
            if (strsubheading.contains(" ")) {
                String[] twoChars = strsubheading.split(" ");
                String fLetter = Character.toString(twoChars[0].charAt(0));
                String secLetter = Character.toString(twoChars[1].charAt(0));
                subTitle = fLetter + " " + secLetter;
            } else {
                subTitle = Character.toString(strsubheading.charAt(0));
            }
            holder.tvSubjectSyl.setText(subTitle);
            holder.syllabusTitleSyl.setText(subArray.getJSONObject(position).getString("Subject"));
        } catch (Exception e) {
            Toast.makeText(subContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return subArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectSyl, syllabusTitleSyl;
        ImageView imageView;
        CardView cardSyllabus;
        String strPosition;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSubjectSyl = (TextView) itemView.findViewById(R.id.tvSubjectSyl);
            syllabusTitleSyl = (TextView) itemView.findViewById(R.id.syllabusTitleSyl);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewSyl);
            cardSyllabus = (CardView) itemView.findViewById(R.id.cardSyllabus);

            tvSubjectSyl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)subContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSubjectSyl.getText());
                    Toast.makeText(subContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            cardSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(subContext, SubjectSyllabusActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Subject", strSubjectsList.get(getAdapterPosition()));
                    bundle.putString("SubjectId",strSubjectIdList.get(getAdapterPosition()));
                    bundle.putString("StudentId",strStudentId);
                    bundle.putString("class",strClass);

                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    subContext.startActivity(intent);
                }
            });
        }
    }
}
