package com.vinuthana.vinvidya.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.activities.extraactivities.MainActivity;
import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Krish on 04-12-2017.
 */

public class LandingAdapter extends RecyclerView.Adapter<LandingAdapter.MyViewHolder> {
    public ArrayList<HashMap<String, String>> spList = new ArrayList<HashMap<String, String>>();
    Context landingContext;
    JSONArray landingArray;
    Activity activity;
    StudentSPreference studentSPreference;

    public LandingAdapter(JSONArray landingArray, Context landingContext, Activity activity) {
        this.landingContext = landingContext;
        this.landingArray = landingArray;
        this.activity = activity;
    }

    @Override
    public LandingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_profile, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LandingAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject jsonObject = landingArray.getJSONObject(position);
            HashMap<String, String> spHashmap = new HashMap<String, String>();

            holder.tvStdNameLanding.setText(jsonObject.getString("studentname"));
            holder.tvStandardLanding.setText(jsonObject.getString("class"));
            holder.tvRollNoLanding.setText(jsonObject.getString("RollNo"));

            spHashmap.put("strStudentname", jsonObject.getString("studentname"));
            spHashmap.put("strClass", jsonObject.getString("class"));
            spHashmap.put("strRollNo", jsonObject.getString("RollNo"));
            spHashmap.put("strStudentId", jsonObject.getString("studentId"));

            spList.add(spHashmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return landingArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStdNameLanding, tvStandardLanding, tvRollNoLanding;
        ImageView studentImageViewLanding;
        CardView cardLanding;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvStdNameLanding = (TextView) itemView.findViewById(R.id.tvStdNameLanding);
            tvStandardLanding = (TextView) itemView.findViewById(R.id.tvStandLanding);
            tvRollNoLanding = (TextView) itemView.findViewById(R.id.tvRlNoLanding);
            //studentImageViewLanding = (ImageView) itemView.findViewById(R.id.studentImageViewLanding);
            cardLanding = (CardView) itemView.findViewById(R.id.cardLanding);
            cardLanding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        studentSPreference = new StudentSPreference(landingContext);
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap = studentSPreference.getStudentDataFromSP(landingArray.getJSONObject(getAdapterPosition()).getString("studentId").toString());
                        //Toast.makeText(landingContext, "Clicked On = " + landingArray.getJSONObject(getAdapterPosition()).getString("studentId").toString(), Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(landingContext, ListActivity_new.class);
                        JSONObject landingObject=landingArray.getJSONObject(getAdapterPosition());
                        String strStudentId=landingObject.getString("studentId");
                        String strSchoolId=landingObject.getString("SchoolId");
                        Intent intent = new Intent(landingContext, MainActivity.class);
                      //  intent.putExtra("studentId", landingArray.getJSONObject(getAdapterPosition()).getString("studentId").toString());
                       intent.putExtra("studentId", strStudentId);
                       intent.putExtra("schoolId",strSchoolId);
                        landingContext.startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
