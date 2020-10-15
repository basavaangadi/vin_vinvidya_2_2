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

public class ReminderRecylcerViewAdapter extends RecyclerView.Adapter<ReminderRecylcerViewAdapter.MyViewHolder> {

    JSONArray rmndrArray;
    Context rmdrContext;

    public ReminderRecylcerViewAdapter(JSONArray rmndrArray, Context rmdrContext) {
        this.rmndrArray = rmndrArray;
        this.rmdrContext = rmdrContext;
    }

    @Override
    public ReminderRecylcerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) rmdrContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.reminder_cardview_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReminderRecylcerViewAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvRmndrNoteTitle.setText(rmndrArray.getJSONObject(position).getString("ReminderTitle"));
            holder.tvRmndrNote.setText(rmndrArray.getJSONObject(position).getString("Reminder"));
            holder.tvRmndrOnDate.setText("Rem. on" + rmndrArray.getJSONObject(position).getString("ReminderDate"));
            holder.tvRmndrSentDate.setText("Sent on" + rmndrArray.getJSONObject(position).getString("ReminderSetDate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rmndrArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRmndrNoteTitle, tvRmndrNote, tvRmndrSentDate, tvRmndrOnDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRmndrNoteTitle = (TextView) itemView.findViewById(R.id.tvRmndrNoteTitle);
            tvRmndrNote = (TextView) itemView.findViewById(R.id.tvRmndrNote);
            tvRmndrSentDate = (TextView) itemView.findViewById(R.id.tvRmndrSntDate);
            tvRmndrOnDate = (TextView) itemView.findViewById(R.id.tvRmndrOnDate);

            tvRmndrNoteTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrNoteTitle.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvRmndrNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrNote.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvRmndrSentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrSentDate.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvRmndrOnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrOnDate.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}
