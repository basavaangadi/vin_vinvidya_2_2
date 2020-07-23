package com.vinuthana.vinvidya.fragments.dayToDayFragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.DropDownLeaveItem;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.Session;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveRequestFragment extends Fragment {

    TextView tview_no_of_leaves;
    EditText edt_from_date,edt_to_date,edt_lv_disc,edt_lv_reason,edt_Password;
    Spinner sp_leave_reason;
    Button btnSubmit;
    Calendar fromCalendar,toCalendar,currentCalendar;
    DropDownLeaveItem dropDownLeaveItem;
    int pos=0,year,month,day;
    View view;
    StudentSPreference studentSPreference;
    JSONArray jsonLeaveReasonArray = new JSONArray();
    Session session;
    HashMap<String,String> hashMap = new HashMap<String, String>();
    DatePickerDialog datePickerDialogFrom,datePickerDialogTo;
    String strFromDate,strToDate,strSubject,strLeaveDiscription,strStudentId,strAcademicID,strClassId;
    String strPhoneNumber,strOtherReason,strSchoolID,strReasonTitleId="";
    DatePickerDialog.OnDateSetListener fromlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            fromCalendar.set(Calendar.YEAR, year);
            fromCalendar.set(Calendar.MONTH, month);
            fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
           // edt_to_date.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
           updateFromLabel();
        }
    };
    DatePickerDialog.OnDateSetListener tolistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            toCalendar.set(Calendar.YEAR, year);
            toCalendar.set(Calendar.MONTH, month);
            toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //edt_to_date.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            updateT0Label();
        }
    };
    public LeaveRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialization(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leave_request, container, false);
        fromCalendar = Calendar.getInstance();
        year = fromCalendar.get(Calendar.YEAR);
        month = fromCalendar.get(Calendar.MONTH);
        day = fromCalendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFrom = new DatePickerDialog(getActivity(), fromlistner, year, month, day);
        datePickerDialogFrom.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialogTo = new DatePickerDialog(getActivity(), tolistner, year, month, day);
        datePickerDialogTo.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return  view;
    }

    public void initialization(View view) {
        try {
            strOtherReason = "";
            studentSPreference = new StudentSPreference(getActivity());
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            hashMap = studentSPreference.getStudentDataFromSP(strStudentId);
            session= new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strPhoneNumber=user.get(Session.KEY_PHONE_NO);
            strSchoolID = hashMap.get(studentSPreference.STR_SCHOOLID);
            strClassId= hashMap.get(studentSPreference.STR_CLASS_ID);
            strAcademicID= hashMap.get(studentSPreference.STR_ACADEMIC_YEAR_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edt_from_date = view.findViewById(R.id.edt_from_date);
        edt_to_date = view.findViewById(R.id.edt_to_date);
        edt_lv_disc = view.findViewById(R.id.edt_lv_disc);
        edt_lv_reason = view.findViewById(R.id.edt_lv_reason);
        edt_Password = view.findViewById(R.id.edt_Password);
        sp_leave_reason = view.findViewById(R.id.sp_leave_reason);
        btnSubmit = view.findViewById(R.id.btn_submit);
        tview_no_of_leaves = view.findViewById(R.id.tview_no_of_leaves);
        fromCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();
        toCalendar = Calendar.getInstance();
        new LeaveReasonDropDownDetail().execute();

        sp_leave_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                if (pos!=0) {
                    try {
                        strReasonTitleId = jsonLeaveReasonArray.getJSONObject(i-1).getString("ReasonId");
                        strSubject = jsonLeaveReasonArray.getJSONObject(i-1).getString("Reason");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /*if (i==jsonLeaveReasonArray.length())
                    edt_lv_reason.setVisibility(View.VISIBLE);
                else
                    edt_lv_reason.setVisibility(View.GONE);*/

                if (strReasonTitleId.equalsIgnoreCase("1"))
                    edt_lv_reason.setVisibility(View.VISIBLE);
                else
                    edt_lv_reason.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pos = 0;

            }
        });
        edt_from_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                datePickerDialogFrom.show();
            }
        });

        edt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogTo.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                   /* String myFormat = "dd-MM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String strFromDate=edt_from_date.toString();
                String strToDate=edt_to_date.toString();
                Date dateFromDate = sdf.parse(strFromDate);
                Date dateToDate = sdf.parse(strToDate);*/

                if (pos ==0 || tview_no_of_leaves.getText().equals("")||edt_lv_disc.getText().equals("")||edt_from_date.getText().length()<=1||edt_to_date.getText().length()<=1) {
                   Toast toast= Toast.makeText(getActivity(),"Enter all field",Toast.LENGTH_SHORT);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();

                }/*else if (dateFromDate.after(dateToDate)) {
                    Toast toast = Toast.makeText(getActivity(),"From date Should be less then to date",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }*/
                else {
                    if (edt_lv_reason.getVisibility()==View.VISIBLE &&(edt_lv_reason.getText().toString().equalsIgnoreCase(""))) {
                        Toast toast= Toast.makeText(getActivity(), "Enter all field", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else {
                        if (edt_lv_reason.getVisibility()==View.VISIBLE)
                            strOtherReason =edt_lv_reason.getText().toString();
                        strLeaveDiscription = edt_lv_disc.getText().toString();
                        strFromDate = edt_from_date.getText().toString();
                        strToDate = edt_to_date.getText().toString();
                        new sendStudentLeaveDetail().execute();
                    }
                }

                }catch (Exception e) {
                    Log.e("doinbackground",e.toString());
                }
            }
        });

    }

    private void updateFromLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        if (fromCalendar.before(currentCalendar)) {
            edt_to_date.setText("");
            Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
        } else if (fromCalendar.after(toCalendar)) {
            tview_no_of_leaves.setText("");
            edt_to_date.setText("");
            edt_from_date.setText("");
            tview_no_of_leaves.setText("");
            Toast toast=Toast.makeText(getActivity(),"From date is greater then To date",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        else {
            edt_from_date.setText(sdf.format(fromCalendar.getTime()));
        }

    }

    private void updateT0Label() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        if (fromCalendar.after(toCalendar)) {
            tview_no_of_leaves.setText("");
            edt_to_date.setText("");
            Toast toast=Toast.makeText(getActivity(),"From date is greater then To date",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } else {
            long differance = toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis();
            int days = (int) (differance/(1000*60*60*24));
            days++;
            String noOfLeaves ="";
            if (days ==1) {
                noOfLeaves = Integer.toString(days) + " day";
            } else {
                noOfLeaves = Integer.toString(days)+ " days";
            }

            tview_no_of_leaves.setText("You have taken "+noOfLeaves+" leave");
            edt_to_date.setText(sdf.format(toCalendar.getTime()));
        }


    }

    public class LeaveReasonDropDownDetail extends AsyncTask<String, JSONArray, Void> {



        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                Date date = new Date();
                String myFormat = "dd-MM-yyyy hh:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String addedDate = sdf.format(date.getTime());
                outObject.put("OperationName", "displayReason");
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_SchoolId), strSchoolID);
                outObject.put( "studAttendanceData",studAttendanceData);
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    jsonLeaveReasonArray = inObject.getJSONArray(getString(R.string.key_Result));
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
                            builder.setNegativeButton("Cancel", null);
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
            } catch (Exception e) {
                //e.printStackTrace();
                Log.e("doinbackground",e.toString());
               /* getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast= Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                });*/

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Select Leave Type");
            for (int i = 0;i<jsonLeaveReasonArray.length();i++) {
                try {
                    JSONObject jsonObject = jsonLeaveReasonArray.getJSONObject(i);
                    arrayList.add(jsonObject.getString("Reason"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            dropDownLeaveItem = new DropDownLeaveItem(getActivity(),arrayList);
            sp_leave_reason.setAdapter(dropDownLeaveItem);
            super.onPostExecute(aVoid);
        }
    }

    public class sendStudentLeaveDetail extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Sending leave detail...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                Date date = new Date();
                String strPassword=edt_Password.getText().toString();

                String myFormat = "dd-MM-yyyy hh:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String addedDate = sdf.format(date.getTime());
                outObject.put("OperationName", "insertStudentLeaveRequest");
                JSONObject LeaveDetailData = new JSONObject();
                LeaveDetailData.put(getString(R.string.key_StudentId), strStudentId);
                LeaveDetailData.put(getString(R.string.key_SchoolId), strSchoolID);
                LeaveDetailData.put(getString(R.string.key_AcademicYearId), strAcademicID);
                LeaveDetailData.put(getString(R.string.key_ReasonTitleId), strReasonTitleId);
                LeaveDetailData.put(getString(R.string.key_Password),strPassword);
                LeaveDetailData.put(getString(R.string.key_PhoneNumber),strPhoneNumber);
                LeaveDetailData.put(getString(R.string.key_ClassId), strClassId);
                LeaveDetailData.put(getString(R.string.key_FromDate), strFromDate);
                LeaveDetailData.put(getString(R.string.key_ToDate), strToDate);
                if (strReasonTitleId.equalsIgnoreCase("1"))
                    LeaveDetailData.put(getString(R.string.key_OtherReasonTitle), strOtherReason);
                else
                    LeaveDetailData.put(getString(R.string.key_OtherReasonTitle), "");

                LeaveDetailData.put(getString(R.string.key_Description), strLeaveDiscription);
                outObject.put( "studAttendanceData",LeaveDetailData);
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousHWFeedback, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage = inObject.getString(getString(R.string.key_Message));
                if(strStatus.equalsIgnoreCase("Success")||strStatus.equalsIgnoreCase("Alert")||strStatus.equalsIgnoreCase("Unauthorised")){
                    showAlert(strStatus,strMessage);
                }else{
                    showAlert(strStatus,"Something went wrong ..");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    public void showAlert(final String strTitle, final String strMessage){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edt_from_date.setText("");
                edt_to_date.setText("");
                edt_lv_disc.setText("");
                edt_Password.setText("");
                edt_lv_reason.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                builder.setView(convertView);
                builder.setCancelable(true);
                builder.setNegativeButton("ok", null);
                builder.setTitle(strTitle);
                builder.setMessage(strMessage);
                builder.show();
            }
        });
    }
}