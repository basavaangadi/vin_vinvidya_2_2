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
 * Created by KISHAN on 07-09-17.
 */

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.MyViewHolder> {
    JSONArray ntcArray;
    Context ntcContext;

    public NoticeRecyclerViewAdapter(JSONArray ntcArray, Context ntcContext) {
        this.ntcArray = ntcArray;
        this.ntcContext = ntcContext;
    }

    @Override
    public NoticeRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) ntcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notice_cardview_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeRecyclerViewAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvNtcNoteTitle.setText(ntcArray.getJSONObject(position).getString("NoticeTitle"));
            holder.tvNtcNote.setText(ntcArray.getJSONObject(position).getString("Notice"));
            holder.tvNtcNoteOnDate.setText("Note on : " + ntcArray.getJSONObject(position).getString("NoticeDate"));
            holder.tvNtcNoteSntDate.setText("Sent on : " + ntcArray.getJSONObject(position).getString("NoticeCreatedDate"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ntcArray.length();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNtcNoteTitle, tvNtcNote, tvNtcNoteOnDate, tvNtcNoteSntDate;

        public MyViewHolder(View myView) {
            super(myView);
            tvNtcNoteTitle = (TextView) myView.findViewById(R.id.tvNtcNoteTitle);
            tvNtcNote = (TextView) myView.findViewById(R.id.tvNtcNote);
            tvNtcNoteOnDate = (TextView) myView.findViewById(R.id.tvNtcNoteOnDate);
            tvNtcNoteSntDate = (TextView) myView.findViewById(R.id.tvNtcNoteSntDate);
        }
    }
}
