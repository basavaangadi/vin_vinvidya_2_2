package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExmMarksRecylerGDGB  extends RecyclerView.Adapter<ExmMarksRecylerGDGB.MyViewHolder> {
    JSONArray exmmrklArray;
    Context exmmrkcContext;
    int arryLen;
    public ExmMarksRecylerGDGB(JSONArray exmmrklArray, Context exmmrkcContext) {
        this.exmmrklArray = exmmrklArray;
        this.arryLen=exmmrklArray.length();
        this.exmmrkcContext = exmmrkcContext;
    }

    @Override
    public ExmMarksRecylerGDGB.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmmrkcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exam_marks_card_list_gdgb, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExmMarksRecylerGDGB.MyViewHolder holder, int position) {
        try {
            JSONObject marksObject=exmmrklArray.getJSONObject(position);
           // String strMaxMarks=marksObject.getString(exmmrkcContext.getString(R.string.key_MaxMarks));
            String strObtainedMarks=marksObject.getString(exmmrkcContext.getString(R.string.key_Grade));
            String strSubject=marksObject.getString(exmmrkcContext.getString(R.string.key_Subject));

            holder.tvExmMarksSubjectGDGB.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_Subject)));
           // holder.tvMaxExmMarks.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_MaxMarks)));
            holder.tvExmMarksGDGB.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_MarksObtained)));//MarksObtained


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        int len= exmmrklArray.length();
        int ilen=arryLen;
        return len;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvExmMarksGDGB, tvMaxExmMarks, tvExmMarksSubjectGDGB;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvExmMarksSubjectGDGB = (TextView) itemView.findViewById(R.id.tvExmMarksSubjectGDGB);
            tvMaxExmMarks = (TextView) itemView.findViewById(R.id.tvMaxExmMarks);
            tvExmMarksGDGB = (TextView) itemView.findViewById(R.id.tvExmMarksGDGB);

            tvExmMarksSubjectGDGB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExmMarksSubjectGDGB.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvMaxExmMarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvMaxExmMarks.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvExmMarksGDGB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExmMarksGDGB.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}
