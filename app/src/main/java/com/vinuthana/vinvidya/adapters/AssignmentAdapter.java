package com.vinuthana.vinvidya.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {
    JSONArray AssignmentArray;
    Activity subContext;
    OnAssignmentClickListener onAssignmentClickListener;

    public AssignmentAdapter(JSONArray AssignmentArray, Activity subContext, String strClassSection) {
        this.AssignmentArray = AssignmentArray;
        this.subContext = subContext;
    }

    @Override
    public AssignmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater inflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View view = inflater.inflate(R.layout.assignment_list, parent,false);
        // return new MyViewHolder(view);

        LayoutInflater inflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.assignment_list, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssignmentAdapter.MyViewHolder holder, int position) {

        try {
            String Subject = "<font color=#FF8C00>Sub : </font>"+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Subject));
            String title = "<font color=#FF8C00>Title : </font> "+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Title));
            String filename = "<font color=#FF8C00>File : </font> "+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Filename));
            String description = "<font color=#FF8C00>Description : </font> "+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Description));
            holder.tViewSubject.setText(Html.fromHtml(Subject));
            holder.tViewTitle.setText(Html.fromHtml(title));
            holder.tViewFilename.setText(Html.fromHtml(filename));
            holder.tViewDescription.setText(Html.fromHtml(description));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnDownload.setBackground(subContext.getResources().getDrawable(R.drawable.btn_shape));

            } else {
                holder.btnDownload.setBackgroundColor(Color.parseColor("#039be5"));

            }
            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!onAssignmentClickListener.equals(null)) {
                            JSONObject object= AssignmentArray.getJSONObject(position);
                            onAssignmentClickListener.onDownload(object, position);

                        }
                    }catch (Exception e)
                    {
                        Log.e("onDownload exception", e.toString());
                        Toast toast=Toast.makeText(subContext,e.toString() , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(subContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnButtonClickListener(OnAssignmentClickListener onAssignmentClickListener){
        this.onAssignmentClickListener=onAssignmentClickListener;
    }

    @Override
    public int getItemCount() {
        return AssignmentArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tViewSubject,tViewTitle,tViewFilename,tViewDescription;
        Button btnDownload;

        public MyViewHolder(View itemView) {
            super(itemView);
            tViewSubject = (TextView) itemView.findViewById(R.id.tViewSubject);
            tViewTitle = (TextView) itemView.findViewById(R.id.tViewTitle);
            tViewFilename = (TextView) itemView.findViewById(R.id.tViewFilename);
            tViewDescription = (TextView) itemView.findViewById(R.id.tViewDescription);
            btnDownload = (Button) itemView.findViewById(R.id.btnDownload);

        }
    }

    public interface OnAssignmentClickListener{
        public void onDownload(JSONObject downloadData, int position);


    }
}
