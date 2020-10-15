package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ClassNoticeRecylerViewAdapter extends RecyclerView.Adapter<ClassNoticeRecylerViewAdapter.MyViewHolder> {

    JSONArray prntNtArray;
    Context prnNtContext;

    public ClassNoticeRecylerViewAdapter(JSONArray prntNtArray, Context prnNtContext) {
        this.prntNtArray = prntNtArray;
        this.prnNtContext = prnNtContext;
    }

    @Override
    public ClassNoticeRecylerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) prnNtContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.parent_note_crdvw_layout, null);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(ClassNoticeRecylerViewAdapter.MyViewHolder holder, int position) {
        try {

            holder.tvPrtNtNoteTitle.setText(prntNtArray.getJSONObject(position).getString("NoticeTitle"));
            holder.tvPrtNtNote.setText(prntNtArray.getJSONObject(position).getString("Notice"));
            String strSentDate=prntNtArray.getJSONObject(position).getString("NoticeCreatedDate");
            String text = "<font color=#FF8C00>Sent Date : </font> <font color=#252f39>"+strSentDate+"</font>";
            holder.tvPrtNtNoteSentDate.setText(Html.fromHtml(text));
            String strNoteOnDate=prntNtArray.getJSONObject(position).getString("NoticeDateTime");
            String Notetext = "<font color=#FF8C00>Note On Date: </font> <font color=#252f39>"+strNoteOnDate+"</font>";
            holder.tvPrtNtNoteOnDate.setText(Html.fromHtml(Notetext));

            Log.e("tag", holder.tvPrtNtNoteTitle.toString() + " " + holder.tvPrtNtNote.toString() + " " + holder.tvPrtNtNoteSentDate.toString() + " " + holder.tvPrtNtNoteOnDate.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return prntNtArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrtNtNoteTitle, tvPrtNtNote, tvPrtNtNoteSentDate, tvPrtNtNoteOnDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPrtNtNoteTitle = (TextView) itemView.findViewById(R.id.tvPrtNtNoteTitle);
            tvPrtNtNote = (TextView) itemView.findViewById(R.id.tvPrtNtNote);
            tvPrtNtNoteSentDate = (TextView) itemView.findViewById(R.id.tvPrtNtNoteSentDate);
            tvPrtNtNoteOnDate = (TextView) itemView.findViewById(R.id.tvPrtNtNoteOnDate);

            tvPrtNtNoteTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPrtNtNoteTitle.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvPrtNtNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPrtNtNote.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvPrtNtNoteSentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPrtNtNoteSentDate.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvPrtNtNoteOnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(prnNtContext.getText());
                    Toast.makeText(ntcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });



        }
    }
}
