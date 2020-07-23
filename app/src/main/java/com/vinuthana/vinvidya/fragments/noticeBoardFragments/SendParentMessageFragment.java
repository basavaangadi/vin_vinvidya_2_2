package com.vinuthana.vinvidya.fragments.noticeBoardFragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StringUtil;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendParentMessageFragment extends Fragment {
    String strStudentId,strClassId,strAcademicYearId,strSchoolId,strMessage,strSentDate;
    EditText edtSendParentMessage;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    TextView tvParentMessageRollNo,tvParentMessageClass,tvParentMessagetudentName;
    Button btnSendParentMessage;

    public SendParentMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        allEvents();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_send_parent_message, container, false);

         return view;
    }
    public void allEvents(){
        btnSendParentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new SendParentMessage().execute();
            }
        });
    }
    public void initialisation(){


        edtSendParentMessage =  getActivity().findViewById(R.id.edtSendParentMessage);
        btnSendParentMessage =  getActivity().findViewById(R.id.btnSendParentMessage);
        tvParentMessageRollNo = (TextView) getActivity().findViewById(R.id.tvParentMessageRollNo);
        tvParentMessageClass = (TextView) getActivity().findViewById(R.id.tvParentMessageClass);
        tvParentMessagetudentName = (TextView) getActivity().findViewById(R.id.tvParentMessagetudentName);


            try {
                strStudentId = getActivity().getIntent().getExtras().getString("studentId");
                strSchoolId = getActivity().getIntent().getExtras().getString("schoolId");
                //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        strSentDate = df.format(c.getTime());
            studentSPreference = new StudentSPreference(getActivity());
            hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


            String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
            String strClass = hashMap.get(studentSPreference.STR_CLASS);
            String strStudentnName = hashMap.get(studentSPreference.KEY_STUD_NAME);
            strClassId=hashMap.get(studentSPreference.STR_CLASS_ID);
            strAcademicYearId=hashMap.get(studentSPreference.STR_ACADEMIC_YEAR_ID);

        tvParentMessageRollNo.setText(strRollNo);
        tvParentMessageClass.setText(strClass);
        tvParentMessagetudentName.setText(strStudentnName);
    }

    private class SendParentMessage extends AsyncTask<String, JSONArray, Void>
    {

        ProgressDialog progressDialog;
        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("sending  the Parent message");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_sendParentMessage));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                noticeBoardData.put(getString(R.string.key_ClassId), strClassId);
                noticeBoardData.put(getString(R.string.key_StudentId), strStudentId);
                //noticeBoardData.put(getString(R.string.key_SentDate), strSentDate);
                strMessage=edtSendParentMessage.getText().toString();
                String base64Message= StringUtil.textToBase64(strMessage);
                noticeBoardData.put(getString(R.string.key_Message), base64Message);

                outObject.put(getString(R.string.key_noticeBoardData),noticeBoardData);
                Log.e("TAG", "sendParentMessage doInBackground, outObject=" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "sendParentMessage doInBackground, respText=" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage= inObject.getString(getString(R.string.key_Message));
                //JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    showAlert(strStatus,strMessage);
                    //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    showAlert(strStatus,"message couldn't be sent");

                }
            } catch (Exception e) {
                Log.e("doinbcgk",e.toString());
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
        public void showAlert(String strTitle,String strMessage){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ok",null);
                    builder.setNegativeButton("Cancel", null);
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });
        }
    }

}
