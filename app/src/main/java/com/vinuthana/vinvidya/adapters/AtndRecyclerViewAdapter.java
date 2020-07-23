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

/**
 * Created by Administrator on 12/12/2017.
 */

public class AtndRecyclerViewAdapter extends RecyclerView.Adapter<AtndRecyclerViewAdapter.MyViewHolder> {
    JSONArray atndArray;
    Context ntcContext;

    public AtndRecyclerViewAdapter(JSONArray atndArray, Context ntcContext) {
        this.atndArray = atndArray;
        this.ntcContext = ntcContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) ntcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.atnd_card_view_layout,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            JSONObject atendObject= atndArray.getJSONObject(position);
            String strDate= atendObject.getString("Date");
            String strDay= atendObject.getString("Day");
            String strStatus= atendObject.getString("Status");

            /*holder.tvDateAtndCard.setText(atndArray.getJSONObject(position).getString("Date"));
            holder.tvDayAtndCard.setText(atndArray.getJSONObject(position).getString("Day"));
            holder.tvStatusAtndCard.setText(atndArray.getJSONObject(position).getString("Status"));*/

            holder.tvDateAtndCard.setText(strDate);
            holder.tvDayAtndCard.setText(strDay);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return atndArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateAtndCard, tvDayAtndCard, tvStatusAtndCard;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvDateAtndCard = (TextView) itemView.findViewById(R.id.tvDateAtndCard);
            tvDayAtndCard = (TextView) itemView.findViewById(R.id.tvDayAtndCard);
            tvStatusAtndCard = (TextView) itemView.findViewById(R.id.tvStatusAtndCard);
        }
    }
}
