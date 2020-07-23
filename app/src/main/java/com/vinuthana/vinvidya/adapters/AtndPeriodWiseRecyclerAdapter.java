package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class AtndPeriodWiseRecyclerAdapter extends RecyclerView.Adapter<AtndPeriodWiseRecyclerAdapter.MyViewHolder> {
    JSONArray atndArray;
    Context ntcContext;

    public AtndPeriodWiseRecyclerAdapter(JSONArray atndArray, Context ntcContext) {
        this.atndArray = atndArray;
        this.ntcContext = ntcContext;
    }

    @Override
    public AtndPeriodWiseRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) ntcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.atnd_period_wise_card_view_layout,null);
        return new AtndPeriodWiseRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AtndPeriodWiseRecyclerAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject atendObject= atndArray.getJSONObject(position);
            String strDate= atendObject.getString("Date");
            holder.tvDateAtndCard.setText(strDate);
            String strDay= atendObject.getString("Day");
            strDay = strDay.substring(0,3);
            holder.tvDayAtndCard.setText(strDay);
            String strStatus= atendObject.getString("Status");


            /*holder.tvDateAtndCard.setText(atndArray.getJSONObject(position).getString("Date"));
            holder.tvDayAtndCard.setText(atndArray.getJSONObject(position).getString("Day"));
            holder.tvStatusAtndCard.setText(atndArray.getJSONObject(position).getString("Status"));*/





            if (strStatus.equalsIgnoreCase("Present")) {
                strStatus = "P";
                holder.tvStatusAtndCard.setTextColor(Color.parseColor("#00b300"));
                //holder.lynrLyt.setBackgroundColor(Color.parseColor("#00ff00"));
            } else {
                strStatus = "A";
                holder.tvStatusAtndCard.setTextColor(Color.parseColor("#FF8C00"));
                //holder.lynrLyt.setBackgroundColor(Color.parseColor("#ff0000"));
            }
            holder.tvStatusAtndCard.setText(strStatus);
            String strPeriod= atendObject.getString("Period");
            holder.tvPeriodAtndCard.setText(strPeriod);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return atndArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateAtndCard, tvDayAtndCard, tvStatusAtndCard,tvPeriodAtndCard;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvDateAtndCard = (TextView) itemView.findViewById(R.id.tvDateAtndCard);
            tvDayAtndCard = (TextView) itemView.findViewById(R.id.tvDayAtndCard);
            tvStatusAtndCard = (TextView) itemView.findViewById(R.id.tvStatusAtndCard);
            tvPeriodAtndCard = (TextView) itemView.findViewById(R.id.tvPeriodAtndCard);
        }
    }
}
