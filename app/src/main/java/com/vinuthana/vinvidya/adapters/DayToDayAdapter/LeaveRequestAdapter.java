package com.vinuthana.vinvidya.adapters.DayToDayAdapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridLayout.LayoutParams;
import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.NoticeRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveRequestAdapter  extends RecyclerView.Adapter<LeaveRequestAdapter.MyViewHolder> {
    JSONArray studLeaveArray;
    Context studLeaveContext;
    onLeaveClickListener leaveClickListener;
    String strIsEditable;
    public LeaveRequestAdapter(JSONArray studLeaveArray, Context studLeaveContext) {
        this.studLeaveArray = studLeaveArray;
        this.studLeaveContext = studLeaveContext;
    }

    @Override
    public LeaveRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) studLeaveContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.leave_details_layout, parent,false);
        return new MyViewHolder(view);
    }

    public void setOnButtonClickListener(onLeaveClickListener leaveClickListener){
        this.leaveClickListener=leaveClickListener;
    }
    
    @Override
    public void onBindViewHolder(LeaveRequestAdapter.MyViewHolder holder, int position) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnStudLeaveDispDelete.setBackground(studLeaveContext.getResources().getDrawable(R.drawable.button_orange));
                holder.btnStudLeaveDispEdit.setBackground(studLeaveContext.getResources().getDrawable(R.drawable.button_green));

            } else {
                holder.btnStudLeaveDispDelete.setBackgroundColor(Color.parseColor("#FF8C00"));
                holder.btnStudLeaveDispEdit.setBackgroundColor(Color.parseColor("#4CAF50"));

            }


            JSONObject object=studLeaveArray.getJSONObject(position);

            holder.tvStudLeaveDispTitle.setText(object.getString(studLeaveContext.getString(R.string.key_ReasonTitle)));
            holder.tvStudLeaveDispDisc.setText(object.getString(studLeaveContext.getString(R.string.key_Description)));
            strIsEditable=object.getString(studLeaveContext.getString(R.string.key_IsEditable));
            holder.tvStudLeaveDispToDate.setText("To : " + object.getString(studLeaveContext.getString(R.string.key_ToDate)));
            holder.tvStudLeaveDispFromDate.setText("From : " +object.getString(studLeaveContext.getString(R.string.key_FromDate)));
          /*if(strIsEditable.equalsIgnoreCase("0")){
               holder.lnyrLytStudLeaveDispEdit.setVisibility(View.GONE);
            }*/
            holder.btnStudLeaveDispEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!leaveClickListener.equals(null)) {
                            JSONObject object= studLeaveArray.getJSONObject(position);
                            String strIsEditable=object.getString(studLeaveContext.getString(R.string.key_IsEditable));
                            if(strIsEditable.equalsIgnoreCase("0")){
                                Toast toast= Toast.makeText(studLeaveContext,"U can't Edit this Leave now",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else {

                                Log.e("onLevEdit", "sylb Clciked");
                                leaveClickListener.onLeaveEdit(object, position);
                            }
                        }
                    }catch (Exception e)
                    {
                        Log.e("onSylbusClick exception", e.toString());
                        Toast.makeText(studLeaveContext,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.btnStudLeaveDispDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!leaveClickListener.equals(null)) {
                            JSONObject object= studLeaveArray.getJSONObject(position);
                            String strIsEditable=object.getString(studLeaveContext.getString(R.string.key_IsEditable));
                            if(!strIsEditable.equalsIgnoreCase("2")){
                                Toast toast= Toast.makeText(studLeaveContext,"U can't Delete this Leave now",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else{
                                Log.e("onLeaveDel","sylb Clciked");
                                leaveClickListener.onLeaveDelete(object,position);
                            }

                        }
                    }catch (Exception e)
                    {
                        Log.e("onSylbusClick exception", e.toString());
                        Toast.makeText(studLeaveContext,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return studLeaveArray.length();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudLeaveDispTitle, tvStudLeaveDispDisc, tvStudLeaveDispToDate, tvStudLeaveDispFromDate;
        CardView crdviewStudLeaveDisp;
        Button btnStudLeaveDispEdit,btnStudLeaveDispDelete;
        LinearLayout lnyrLytStudLeaveDispEdit;

        public MyViewHolder(View myView) {
            super(myView);
            tvStudLeaveDispTitle =  myView.findViewById(R.id.tvStudLeaveDispTitle);
            tvStudLeaveDispDisc = myView.findViewById(R.id.tvStudLeaveDispDisc);
            tvStudLeaveDispToDate = myView.findViewById(R.id.tvStudLeaveDispToDate);
            tvStudLeaveDispFromDate = myView.findViewById(R.id.tvStudLeaveDispFromDate);
            crdviewStudLeaveDisp =  myView.findViewById(R.id.crdviewStudLeaveDisp);
            btnStudLeaveDispEdit =  myView.findViewById(R.id.btnStudLeaveDispEdit);
            lnyrLytStudLeaveDispEdit =  myView.findViewById(R.id.lnyrLytStudLeaveDispEdit);
            btnStudLeaveDispDelete =  myView.findViewById(R.id.btnStudLeaveDispDelete);
           /* try {
                strIsEditable = studLeaveArray.getJSONObject(getAdapterPosition()).getString(studLeaveContext.getString(R.string.key_Syllabus)).toString();
                if (strIsEditable.equalsIgnoreCase("0")) {
                    lnyrLytStudLeaveDispEdit.setVisibility(View.GONE);
                }
            }catch (Exception e){
                *//*Toast toast= Toast.makeText(studLeaveContext,e.toString(),Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();*//*
            }*/
        }
    }

    public interface onLeaveClickListener{
        public void onLeaveEdit(JSONObject leaveRequestData,int position);
        public void onLeaveDelete(JSONObject leaveRequestData,int position);

    }
}