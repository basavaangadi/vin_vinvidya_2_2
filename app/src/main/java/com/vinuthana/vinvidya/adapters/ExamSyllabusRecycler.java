package com.vinuthana.vinvidya.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;

import org.json.JSONArray;

/**
 * Created by KISHAN on 16-09-17.
 */

public class ExamSyllabusRecycler extends RecyclerView.Adapter<ExamSyllabusRecycler.MyViewHolder> {
    JSONArray exmsylArray;
    Activity exmsylcContext;
    String strDate, strTime, strDateTime;

    public ExamSyllabusRecycler(JSONArray exmsylArray, Activity exmsylcContext) {
        this.exmsylArray = exmsylArray;
        this.exmsylcContext = exmsylcContext;
    }

    @Override
    public ExamSyllabusRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmsylcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exm_sylbs_card_view_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamSyllabusRecycler.MyViewHolder holder, int position) {
        try {

            //holder.tvSyllabusExm.setText("Exam: " + exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_Exam)));
            strDate = exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_ExamDate));
            strTime = exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_ExamTime));
            strDateTime = strDate + " " + strTime;
            holder.tvSubjectExmSyllabus.setText( exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_Subject)));
            holder.tvDtExmSyllabus.setText( strDate);
            //holder.tvTimExmSyllabus.setText("Time: " + exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_ExamTime)));
            holder.tvSyllabusExmSyllabus.setText(exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_Syllabus)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return exmsylArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectExmSyllabus, tvSyllabusExmSyllabus, tvSyllabusExm,tvDtExmSyllabus;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSubjectExmSyllabus = (TextView) itemView.findViewById(R.id.tvSubjectExmSyllabus);
           // tvSyllabusExm = (TextView) itemView.findViewById(R.id.tvSyllabusExm);
            tvDtExmSyllabus = (TextView) itemView.findViewById(R.id.tvDtExmSyllabus);
            tvSyllabusExmSyllabus = (TextView) itemView.findViewById(R.id.tvSyllabusExmSyllabus);

            tvSubjectExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSubjectExmSyllabus.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvDtExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvDtExmSyllabus.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvSyllabusExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSyllabusExmSyllabus.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });




        }
    }
}
