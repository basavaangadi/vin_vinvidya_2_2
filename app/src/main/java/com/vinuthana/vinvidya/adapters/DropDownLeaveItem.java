package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;

import java.util.ArrayList;

public class DropDownLeaveItem  extends BaseAdapter {
    Context context;
    ArrayList< String> hashMapArrayList;

    public DropDownLeaveItem(Context context, ArrayList<String> hashMapArrayList) {
        this.context = context;
        this.hashMapArrayList = hashMapArrayList;
    }

    @Override
    public int getCount() {
        return hashMapArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return hashMapArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        try {
            if (convertView == null) {

                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.leave_spinner_dropdown, null);
                holder = new ViewHolder();
                holder.leave_sub = (TextView) view.findViewById(R.id.leave_sub);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.leave_sub.setText(hashMapArrayList.get(position));
        }  catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    class ViewHolder {
        TextView leave_sub;
    }
}
