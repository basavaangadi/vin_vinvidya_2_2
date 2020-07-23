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
import com.vinuthana.vinvidya.adapters.NoticeRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NoticeByDate extends Fragment {
    private int sentOnYear, sentOnMonth, sentOnDay, noteOnYear, noteOnMonth, noteOnDay;
    DatePickerDialog dtPckrDlogSntDt, dtPckrDlogOnDate;
    Calendar calNoteSentDate = Calendar.getInstance();
    Calendar calNnoteOnDate = Calendar.getInstance();
    private int mYear, mMonth, mDay, mYear_new, mMonth_new, mDay_new;
    DatePickerDialog datePickerDialog, datePickerDialog_new;
    Calendar c = Calendar.getInstance();
    Calendar d = Calendar.getInstance();
    String strDate, strStatus;
    View view;
    View rootView;
    TextView tvNtcByDtStudentName, tvNtcByDtClass, tvNtcByDtRollNo;
    JSONArray ntcArray= new JSONArray();
    EditText edtNoticeFromDate, edtNoticeToDate;
    Button btnNoticeByDate;
    RecyclerView rcyclrVwNtcByDt;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strFromDate, strToDate;

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtNoticeFromDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            dtPckrDlogSntDt.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edtNoticeToDate.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
            dtPckrDlogOnDate.dismiss();
        }
    };

    public NoticeByDate() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        allEvents();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){
           // new GetPreviousNoticeByDate().execute();
        }
    }

    private void initialisation() {
        edtNoticeFromDate = (EditText) rootView.findViewById(R.id.edtNoticeFrmDate);
        edtNoticeToDate = (EditText) rootView.findViewById(R.id.edtNoticeToDate);
        btnNoticeByDate = (Button) rootView.findViewById(R.id.btnNoticeByDate);
        rcyclrVwNtcByDt = (RecyclerView) rootView.findViewById(R.id.rcyclrVwNtcByDt);
        tvNtcByDtStudentName = (TextView) rootView.findViewById(R.id.tvNtcByDtStudentName);
        tvNtcByDtClass = (TextView) rootView.findViewById(R.id.tvNtcByDtClass);
        tvNtcByDtRollNo = (TextView) rootView.findViewById(R.id.tvNtcByDtRollNo);
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
        tvNtcByDtRollNo.setText(strRollNo);
        tvNtcByDtClass.setText(strClass);
        tvNtcByDtStudentName.setText(strStudentnName);
        initRecylerAdapter();
    }

    public void initRecylerAdapter(){
        NoticeRecyclerViewAdapter recyclerViewAdapter = new NoticeRecyclerViewAdapter(ntcArray, getActivity());
        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        rcyclrVwNtcByDt.setLayoutManager(prntNtLytMngr);
        rcyclrVwNtcByDt.setItemAnimator(new DefaultItemAnimator());
        rcyclrVwNtcByDt.setAdapter(recyclerViewAdapter);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notice_by_date, container, false);

        sentOnYear = calNoteSentDate.get(Calendar.YEAR);
        sentOnMonth = calNoteSentDate.get(Calendar.MONTH);
        sentOnDay = calNoteSentDate.get(Calendar.DAY_OF_MONTH);
        dtPckrDlogSntDt = new DatePickerDialog(getActivity(), mlistner, sentOnYear, sentOnMonth, sentOnDay);

        noteOnYear = calNnoteOnDate.get(Calendar.YEAR);
        noteOnMonth = calNnoteOnDate.get(Calendar.MONTH);
        noteOnDay = calNnoteOnDate.get(Calendar.DAY_OF_MONTH);
        dtPckrDlogOnDate = new DatePickerDialog(getActivity(), nlistner, noteOnYear, noteOnMonth, noteOnDay);
        return rootView;
    }

    private void allEvents() {
        edtNoticeFromDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtNoticeFrmDate) {
                    dtPckrDlogSntDt.show();
                    return true;
                }
                return false;
            }
        });
        edtNoticeToDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtNoticeToDate) {
                    dtPckrDlogOnDate.show();
                    return true;
                }
                return false;
            }
        });
        btnNoticeByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFromDate = edtNoticeFromDate.getText().toString();
                strToDate = edtNoticeToDate.getText().toString();
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
                Date fromDate=null,ToDate=null;
                try {
                    fromDate = simpleDateFormat.parse(strFromDate);
                    ToDate = simpleDateFormat.parse(strToDate);
                }catch (Exception e){
                    Log.e("btnNoticeByDate",e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                }
                if (strFromDate.equals("") || strToDate.equals("")) {
                    Toast.makeText(getActivity(), "Both dates are mandatory ", Toast.LENGTH_SHORT).show();
                } else if (ToDate.before(fromDate)){
                    Toast.makeText(getActivity(), "From Date Should be less then To date ", Toast.LENGTH_SHORT).show();

                } else  {

                    new GetPreviousNoticeByDate().execute("", strToDate);
                }

               /* if (edtNoticeFromDate.getText().toString().equals("") && edtNoticeToDate.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Notice By Date and  Notice On can't be blank", Toast.LENGTH_LONG).show();
                } else {

                }*/
            }
        });
    }

    private class GetPreviousNoticeByDate extends AsyncTask<String, JSONArray, String> {
        ProgressDialog progressDialog;
        JSONArray resultArray= new JSONArray();
        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Notice...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = 0; i < ntcArray.length(); i++) {
                    ntcArray.remove(i);
                }
            }else{
                ntcArray=new JSONArray();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... params) {

            String strResult;
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_noticeDisplay_ByDate));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StudentId), strStudentId);
                noticeBoardData.put(getString(R.string.key_fromDate), strFromDate);
                noticeBoardData.put(getString(R.string.key_toDate), strToDate);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetPreviousNoticeByDate,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousNoticeByDate,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    rcyclrVwNtcByDt.setVisibility(View.VISIBLE);
                    for(int i=0;i<resultArray.length();i++){
                        ntcArray.put(resultArray.getJSONObject(i));
                    }
                    //  publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom,null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    rcyclrVwNtcByDt.setVisibility(View.GONE);
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
                //e.printStackTrace();

                strResult = "Exception " + e.toString();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            /*NoticeRecyclerViewAdapter recyclerViewAdapter = new NoticeRecyclerViewAdapter(values[0], getActivity());
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            rcyclrVwNtcByDt.setLayoutManager(prntNtLytMngr);
            rcyclrVwNtcByDt.setItemAnimator(new DefaultItemAnimator());
            rcyclrVwNtcByDt.setAdapter(recyclerViewAdapter);*/

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            initRecylerAdapter();
            try {
                JSONArray jsonArray = new JSONArray(ntcArray.toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                /*String strStudentName = jsonObject.getString("StudentName");
                String strClass = jsonObject.getString("Class");
                tvNtcByDtStudentName.setText(strStudentName);
                tvNtcByDtRollNo.setText(strClass);*/
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error",e.toString());
            }
            initRecylerAdapter();
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