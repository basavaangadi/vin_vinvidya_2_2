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
import android.text.Html;
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
import com.vinuthana.vinvidya.adapters.ClassNoticeRecylerViewAdapter;
import com.vinuthana.vinvidya.adapters.PrntNtRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassNoticeByDateFragment extends Fragment {


    private int sentOnYear, sentOnMonth, sentOnDay, noteOnYear, noteOnMonth, noteOnDay;
    private int mYear, mMonth, mDay, mYear_new, mMonth_new, mDay_new;
    DatePickerDialog datePickerDialog, datePickerDialog_new;
    Calendar calClassNtSent = Calendar.getInstance();
    Calendar calClassNoteOnDate = Calendar.getInstance();
    String strDate, strStatus;
    View view;
    static EditText edtClassNoteFrom, edtClassNoteTo;
    Button btnSubmitClassNoteByDate;
    JSONArray ntcArray= new JSONArray();
    RecyclerView rcyclrVwClassNtByDt;
    TextView tvClassNtByDtStudentName, lrlyClassNtByDtNote, tvClassNtByDtClass, tvClassNtByDtRollno;

    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId,strPrntFrom,strPrntTo;

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtClassNoteFrom.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edtClassNoteTo.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
            datePickerDialog_new.dismiss();
        }
    };
    
    
    
    
    
    public ClassNoticeByDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_class_notice_by_date, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation(view);
        allEvents();
        sentOnYear = calClassNtSent.get(Calendar.YEAR);
        sentOnMonth = calClassNtSent.get(Calendar.MONTH);
        sentOnDay = calClassNtSent.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, sentOnYear, sentOnMonth, sentOnDay);

        noteOnYear = calClassNoteOnDate.get(Calendar.YEAR);
        noteOnMonth = calClassNoteOnDate.get(Calendar.MONTH);
        noteOnDay = calClassNoteOnDate.get(Calendar.DAY_OF_MONTH);
        datePickerDialog_new = new DatePickerDialog(getActivity(), nlistner, noteOnYear, noteOnMonth, noteOnDay);

    }
    private void initialisation(View view) {
        edtClassNoteFrom = (EditText) view.findViewById(R.id.edtClassNoteFrom);
        edtClassNoteTo = (EditText) view.findViewById(R.id.edtClassNoteTo);
        btnSubmitClassNoteByDate = (Button) view.findViewById(R.id.btnSubmitClassNoteByDate);
        rcyclrVwClassNtByDt = (RecyclerView) getActivity().findViewById(R.id.rcyclrVwClassNtByDt);
        tvClassNtByDtStudentName = (TextView) getActivity().findViewById(R.id.tvClassNtByDtStudentName);
        tvClassNtByDtClass = (TextView) getActivity().findViewById(R.id.tvClassNtByDtClass);
        tvClassNtByDtRollno = (TextView) getActivity().findViewById(R.id.tvClassNtByDtRollno);

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
        String rollText = "<font color=#FF8C00>Roll No : </font> <font color=#ffffff>"+strRollNo+"</font>";
        tvClassNtByDtRollno.setText(Html.fromHtml(rollText));
        //tvCurClassNoteRollNo.setText(strRollNo);
        String classText = "<font color=#FF8C00>Class : </font> <font color=#ffffff>"+strClass+"</font>";
        tvClassNtByDtClass.setText(Html.fromHtml(classText));
        /*tvClassNtByDtRollno.setText(strRollNo);
        tvClassNtByDtClass.setText(strClass);*/
        tvClassNtByDtStudentName.setText(strStudentnName);
        initRecylerAdapter();
    }

    public void initRecylerAdapter(){
        ClassNoticeRecylerViewAdapter adapter = new ClassNoticeRecylerViewAdapter(ntcArray, getActivity());
        RecyclerView.LayoutManager prntLytMngr = new LinearLayoutManager(getActivity());
        rcyclrVwClassNtByDt.setLayoutManager(prntLytMngr);
        rcyclrVwClassNtByDt.setItemAnimator(new DefaultItemAnimator());
        rcyclrVwClassNtByDt.setAdapter(adapter);
    }
    private void allEvents() {
        edtClassNoteFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtClassNoteFrom) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });
        edtClassNoteTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (v.getId() == R.id.edtClassNoteTo) {
                    datePickerDialog_new.show();
                    return true;
                }
                return false;
            }
        });
        btnSubmitClassNoteByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPrntFrom=edtClassNoteFrom.getText().toString();
                strPrntTo=edtClassNoteTo.getText().toString();
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
                    Toast.makeText(getActivity(), "Note Sent and Note On can't be blank", Toast.LENGTH_LONG).show();
                } else if(ToDate.before(fromDate)) {
                    Toast.makeText(getActivity(), "From Date Should be less then To date ", Toast.LENGTH_SHORT).show();
                }else{
                    new GetClassNoticeByDate().execute();
                }
            }
        });
    }


    private class GetClassNoticeByDate extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        JSONArray resultArray= new JSONArray();
        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        String strPrnNtSntDt = edtClassNoteFrom.getText().toString();
        String strPrnNtonDt = edtClassNoteTo.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Class Note...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = 0; i < ntcArray.length(); i++) {
                    ntcArray.remove(i);
                }
            }else{
                ntcArray=new JSONArray();
            }

        }


        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClassNoticeForStudent));
                JSONObject parentsNoteData = new JSONObject();
                parentsNoteData.put(getString(R.string.key_StudentId), strStudentId);
                parentsNoteData.put(getString(R.string.key_fromDate), strPrntFrom);
                parentsNoteData.put(getString(R.string.key_toDate), strPrntTo);
                outObject.put(getString(R.string.key_noticeBoardData), parentsNoteData);
                Log.e("TAG", "GetClassNoticeByDate, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetClassNoticeByDate, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    rcyclrVwClassNtByDt.setVisibility(View.VISIBLE);
                    for(int i=0;i<resultArray.length();i++){
                        ntcArray.put(resultArray.getJSONObject(i));
                    }
                   // publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
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
                                    rcyclrVwClassNtByDt.setVisibility(View.GONE);
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
                // showErrorToast(e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            /*ClassNoticeRecylerViewAdapter adapter = new ClassNoticeRecylerViewAdapter(values[0], getActivity());
            RecyclerView.LayoutManager prntLytMngr = new LinearLayoutManager(getActivity());
            rcyclrVwClassNtByDt.setLayoutManager(prntLytMngr);
            rcyclrVwClassNtByDt.setItemAnimator(new DefaultItemAnimator());
            rcyclrVwClassNtByDt.setAdapter(adapter);*/

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            initRecylerAdapter();
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
