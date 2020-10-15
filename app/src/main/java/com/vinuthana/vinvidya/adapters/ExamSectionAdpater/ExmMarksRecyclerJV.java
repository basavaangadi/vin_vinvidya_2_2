package com.vinuthana.vinvidya.adapters.ExamSectionAdpater;

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

public class ExmMarksRecyclerJV  extends RecyclerView.Adapter<ExmMarksRecyclerJV.MyViewHolder> {
    JSONArray exmmrklArray;
    Context exmmrkcContext;

    public ExmMarksRecyclerJV(JSONArray exmmrklArray, Context exmmrkcContext) {
        this.exmmrklArray = exmmrklArray;
        this.exmmrkcContext = exmmrkcContext;
    }

    @Override
    public ExmMarksRecyclerJV.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmmrkcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exam_mrks_card_view_jv_layout, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExmMarksRecyclerJV.MyViewHolder holder, int position) {
        try {
            JSONObject marksObject=exmmrklArray.getJSONObject(position);
            String strMaxMarks=marksObject.getString(exmmrkcContext.getString(R.string.key_MaxMarks));
            String strObtainedMarks=marksObject.getString(exmmrkcContext.getString(R.string.key_MarksObtained));
            String strSubject=marksObject.getString(exmmrkcContext.getString(R.string.key_Subject));
            String strDate=marksObject.getString(exmmrkcContext.getString(R.string.key_Date));
            strSubject=strSubject.substring(0,3);
            /*holder.tvExmMarksDateJV.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_Subject)));
            holder.tvMaxExmMarksJV.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_MaxMarks)));
            holder.tvExmMarksJV.setText(marksObject.getString(exmmrkcContext.getString(R.string.key_MarksObtained)));*/
            holder.tvExmMarksDateJV.setText(strDate);
            holder.tvExmMarksSubjectJV.setText(strSubject);
            holder.tvMaxExmMarksJV.setText(strMaxMarks);
            holder.tvExmMarksJV.setText(strObtainedMarks);
        } catch (Exception e) {
            Toast.makeText(exmmrkcContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return exmmrklArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvExmMarksJV, tvMaxExmMarksJV, tvExmMarksDateJV,tvExmMarksSubjectJV;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvExmMarksDateJV = (TextView) itemView.findViewById(R.id.tvExmMarksDateJV);
            tvExmMarksSubjectJV = (TextView) itemView.findViewById(R.id.tvExmMarksSubjectJV);
            tvMaxExmMarksJV = (TextView) itemView.findViewById(R.id.tvMaxExmMarksJV);
            tvExmMarksJV = (TextView) itemView.findViewById(R.id.tvExmMarksJV);

            tvExmMarksDateJV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExmMarksDateJV.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvExmMarksSubjectJV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExmMarksSubjectJV.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvMaxExmMarksJV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvMaxExmMarksJV.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvExmMarksJV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExmMarksJV.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
