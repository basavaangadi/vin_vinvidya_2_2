package com.vinuthana.vinvidya.fragments.noticeBoardFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ReminderRecylcerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReminderByDate extends Fragment {
    EditText edtReminderFrmDate, edttReminderByDateToDate;
    Button btnReminderByDate;
    //private int mYear, mMonth, mDay,mYear_new, mMonth_new, mDay_new;
    DatePickerDialog datePickerDialog, datePickerDialog_new;
    Calendar calRmndrByDtRmdrSetOnDate = Calendar.getInstance();
    Calendar calRmndrByDtRmdOnDate = Calendar.getInstance();
    String strDate;
    RecyclerView rcyclrVwRmdrByDt;
    View view;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus,strPrntFrom,strPrntTo;
    JSONArray rmdrArray= new JSONArray();

    String strRmdrToDate, strRmdrFromDate;
    TextView tvRmdrByDtStudentName, tvRmdrByDtNote, tvRmdrByDtDate;
    TextView tvRmdrByDtClass, tvRmdrByDtNoteSntDate, tvRmdrByDtRollno;
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtReminderFrmDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edttReminderByDateToDate.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
            datePickerDialog_new.dismiss();
        }
    };
    private int sentOnYear, sentOnMonth, sentOnDay, noteOnYear, noteOnMonth, noteOnDay;

    public ReminderByDate() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        allEvents();

    }

    private void initialisation() {
        edtReminderFrmDate = (EditText) getActivity().findViewById(R.id.edtReminderFrmDate);
        edttReminderByDateToDate = (EditText) getActivity().findViewById(R.id.edttReminderByDateToDate);
        btnReminderByDate = (Button) getActivity().findViewById(R.id.btnReminderByDate);
        rcyclrVwRmdrByDt = (RecyclerView) getActivity().findViewById(R.id.rcyclrVwRmdrByDt);
        tvRmdrByDtStudentName = (TextView) getActivity().findViewById(R.id.tvRmdrByDtStudentName);
        tvRmdrByDtRollno = (TextView) getActivity().findViewById(R.id.tvRmdrByDtRollno);
        tvRmdrByDtClass = (TextView) getActivity().findViewById(R.id.tvRmdrByDtClass);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        //Toast.makeText(getActivity(), ""+strRollNo, Toast.LENGTH_LONG).show();
        tvRmdrByDtRollno.setText(strRollNo);
        tvRmdrByDtClass.setText(strClass);
        tvRmdrByDtStudentName.setText(strStudentnName);
        initRecylerAdpater();


    }
    public void initRecylerAdpater(){
        ReminderRecylcerViewAdapter adapter = new ReminderRecylcerViewAdapter(rmdrArray, getActivity());
        RecyclerView.LayoutManager rmndrBydt = new LinearLayoutManager(getActivity());
        rcyclrVwRmdrByDt.setItemAnimator(new DefaultItemAnimator());
        rcyclrVwRmdrByDt.setLayoutManager(rmndrBydt);
        rcyclrVwRmdrByDt.setAdapter(adapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){
           // new GetPreviousReminderByDate().execute();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reminder_by_date, container, false);

        sentOnYear = calRmndrByDtRmdrSetOnDate.get(Calendar.YEAR);
        sentOnMonth = calRmndrByDtRmdrSetOnDate.get(Calendar.MONTH);
        sentOnDay = calRmndrByDtRmdrSetOnDate.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, sentOnYear, sentOnMonth, sentOnDay);

        noteOnYear = calRmndrByDtRmdOnDate.get(Calendar.YEAR);
        noteOnMonth = calRmndrByDtRmdOnDate.get(Calendar.MONTH);
        noteOnDay = calRmndrByDtRmdOnDate.get(Calendar.DAY_OF_MONTH);
        datePickerDialog_new = new DatePickerDialog(getActivity(), nlistner, noteOnYear, noteOnMonth, noteOnDay);


        return view;
    }

    private void allEvents() {
        edtReminderFrmDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtReminderFrmDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });
        edttReminderByDateToDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edttReminderByDateToDate) {
                    datePickerDialog_new.show();
                    return true;
                }
                return false;
            }
        });
        btnReminderByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 strPrntFrom=edtReminderFrmDate.getText().toString();
                 strPrntTo=edttReminderByDateToDate.getText().toString();
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
                Date fromDate=null,ToDate=null;
                try {
                    fromDate = simpleDateFormat.parse(strPrntFrom);
                    ToDate = simpleDateFormat.parse(strPrntTo);
                }catch (Exception e){
                    Log.e("btnNoticeByDate",e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                }
                if (strPrntFrom.equalsIgnoreCase("") || strPrntTo.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Notice by Date and Notice by Date On can't be blank", Toast.LENGTH_LONG).show();
                }else if(ToDate.before(fromDate)){
                    Toast.makeText(getActivity(), " From Date Should be less then to Date ", Toast.LENGTH_SHORT).show();
                }else {
                   new GetPreviousReminderByDate().execute();
                }
            }
        });
    }

    private class GetPreviousReminderByDate extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        JSONArray resultArray= new JSONArray();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Reminder By Date...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = 0; i < rmdrArray.length(); i++) {
                    rmdrArray.remove(i);
                }
            }else{
                rmdrArray=new JSONArray();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {

                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_studentReminderDisplayByDate));
                JSONObject reminderByDateData = new JSONObject();
                reminderByDateData.put(getString(R.string.key_StudentId), strStudentId);
                reminderByDateData.put(getString(R.string.key_fromDate), strPrntFrom);
                reminderByDateData.put(getString(R.string.key_toDate), strPrntTo);
                outObject.put(getString(R.string.key_noticeBoardData), reminderByDateData);
                Log.e("TAG", "GetPreviousReminderByDate, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousReminderByDate, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    rcyclrVwRmdrByDt.setVisibility(View.VISIBLE);
                    for(int i=0;i<resultArray.length();i++){
                        rmdrArray.put(resultArray.getJSONObject(i));
                    }
                    //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    rcyclrVwRmdrByDt.setVisibility(View.GONE);
                                }
                            });
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("doinbcgk",e.toString());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
           /* ReminderRecylcerViewAdapter adapter = new ReminderRecylcerViewAdapter(values[0], getActivity());
            RecyclerView.LayoutManager rmndrBydt = new LinearLayoutManager(getActivity());
            rcyclrVwRmdrByDt.setItemAnimator(new DefaultItemAnimator());
            rcyclrVwRmdrByDt.setLayoutManager(rmndrBydt);
            rcyclrVwRmdrByDt.setAdapter(adapter);*/
            /*try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString("Studentname");
                String strClass = jsonObject.getString("Class");
                tvRmdrByDtStudentName.setText(strStudentName);
                tvRmdrByDtRollno.setText(strClass);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initRecylerAdpater();
            progressDialog.dismiss();
        }
        public void showErrorToast(String error ){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast= Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            });
        }
    }
}
