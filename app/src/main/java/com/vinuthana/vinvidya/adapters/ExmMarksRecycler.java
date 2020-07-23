package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class ExmMarksRecycler extends RecyclerView.Adapter<ExmMarksRecycler.MyViewHolder> {
    JSONArray exmmrklArray;
    Context exmmrkcContext;

    public ExmMarksRecycler(JSONArray exmmrklArray, Context exmmrkcContext) {
        this.exmmrklArray = exmmrklArray;
        this.exmmrkcContext = exmmrkcContext;
    }

    @Override
    public ExmMarksRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmmrkcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exam_mrks_card_view_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExmMarksRecycler.MyViewHolder holder, int position) {
        try {
            JSONObject marksObject=exmmrklArray.getJSONObject(position);
            String strMaxMarks=marksObject.getString(exmmrkcContext.getString(R.string.key_MaxMarks));
            String strObtainedMarks=marksObject.getString(exmmrkcContext.getString(R.string.key_MarksObtained));
            String strSubject=marksObject.getString(exmmrkcContext.getString(R.string.key_Subject));

            /*holder.tvExmMarksSubject.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_Subject)));
            holder.tvMaxExmMarks.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_MaxMarks)));
            holder.tvExmMarks.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_MarksObtained)));*/
            holder.tvExmMarksSubject.setText(strSubject);
            holder.tvMaxExmMarks.setText(strMaxMarks);
            holder.tvExmMarks.setText(strObtainedMarks);
            } catch (Exception e) {
            Toast.makeText(exmmrkcContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return exmmrklArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvExmMarks, tvMaxExmMarks, tvExmMarksSubject;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvExmMarksSubject = (TextView) itemView.findViewById(R.id.tvExmMarksSubject);
            tvMaxExmMarks = (TextView) itemView.findViewById(R.id.tvMaxExmMarks);
            tvExmMarks = (TextView) itemView.findViewById(R.id.tvExmMarks);
        }
    }
}
