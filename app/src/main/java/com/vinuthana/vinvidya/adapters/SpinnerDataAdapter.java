package com.vinuthana.vinvidya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vinuthana.vinvidya.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by KISHAN on 06-09-17.
 */

public class SpinnerDataAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    JSONArray array;
    Context context;

    public SpinnerDataAdapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return array.getJSONObject(position).getString(context.getString(R.string.key_ExamId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder spinnerHolder;
        if (convertView == null) {
            spinnerHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.exam_list, parent, false);
            spinnerHolder.spinnerItemList = (TextView) convertView.findViewById(R.id.txt_exam);
            convertView.setTag(spinnerHolder);
        } else {
            spinnerHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = array.getJSONObject(position);
            spinnerHolder.spinnerItemList.setText(jsonObject.getString(context.getString(R.string.key_Exam)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView spinnerItemList;
    }
}
