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

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.examsection.ExamImageUploadActivity;
import com.vinuthana.vinvidya.activities.otheractivities.AdmissionFeesActivity;
import com.vinuthana.vinvidya.activities.otheractivities.AssignmentActivity;
import com.vinuthana.vinvidya.activities.otheractivities.EventsActivity;
import com.vinuthana.vinvidya.activities.otheractivities.MainGalleryActivity;
import com.vinuthana.vinvidya.activities.otheractivities.SyllabusActivity;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 1/4/2018.
 */

public class RviewAdapterOthers extends RecyclerView.Adapter<RviewAdapterOthers.MyViewHolder> {
    Activity context;
    ArrayList<String> titleName;
    ArrayList<Integer> images;
    View view;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId,strSchoolId;

    public RviewAdapterOthers(Activity context, ArrayList<String> titleName, ArrayList<Integer> images) {
        this.context = context;
        this.titleName = titleName;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RviewAdapterOthers.MyViewHolder holder, final int position) {

        try {
            strStudentId = context.getIntent().getExtras().getString("studentId");
            strSchoolId = context.getIntent().getExtras().getString("schoolId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvText.setText(titleName.get(position));
        holder.imageView.setImageResource(images.get(position));


        holder.bankcardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Position " + position, Toast.LENGTH_SHORT).show();

                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(context, EventsActivity.class);
                        intent.putExtra("studentId", strStudentId);
                        intent.putExtra("schoolId", strSchoolId);
                        break;
                    case 1:
                        intent = new Intent(context, MainGalleryActivity.class);
                        intent.putExtra("studentId", strStudentId);
                        intent.putExtra("schoolId", strSchoolId);
                        break;
                    case 2:
                        intent = new Intent(context, SyllabusActivity.class);
                        intent.putExtra("studentId", strStudentId);
                        intent.putExtra("schoolId", strSchoolId);
                        break;

                    case 3:
                        intent = new Intent(context, AssignmentActivity.class);
                        intent.putExtra("studentId", strStudentId);
                        intent.putExtra("schoolId", strSchoolId);
                        break;
                    case 4:
                        intent = new Intent (context, AdmissionFeesActivity.class);
                        intent.putExtra("studentId", strStudentId);
                        intent.putExtra("schoolId", strSchoolId);
                        break;



                    default:
                        break;
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        ImageView imageView;
        CardView bankcardId;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            bankcardId = (CardView) itemView.findViewById(R.id.bankcardId);
        }
    }
}
