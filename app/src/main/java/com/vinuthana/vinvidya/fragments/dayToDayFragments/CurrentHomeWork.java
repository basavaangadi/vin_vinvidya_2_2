package com.vinuthana.vinvidya.fragments.dayToDayFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.otheractivities.SubjectSyllabusActivity;
import com.vinuthana.vinvidya.adapters.HmWrkRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class CurrentHomeWork extends Fragment {
    TextView tvCurHWStudentName, tvCurHWClass, tvCurHWSection, tvCurHWRollNo;
    TableLayout tblCurHmWrkGrid;
    RecyclerView recyclerViewHmWrk;
    String strStudentId,strSchoolId;
    JSONArray homeworkArray=new JSONArray();
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    HmWrkRecyclerViewAdapter recyclerAdapter;
    public CurrentHomeWork() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        new GetCurrentHomeWork().execute();
    }

    private void initialisation() {

        tvCurHWStudentName = (TextView) getActivity().findViewById(R.id.tvCurHWStudentName);
        tvCurHWClass = (TextView) getActivity().findViewById(R.id.tvCurHWClass);
        tvCurHWRollNo = (TextView) getActivity().findViewById(R.id.tvCurHWRollNo);
        recyclerViewHmWrk = (RecyclerView) getActivity().findViewById(R.id.recyclerViewHmWrk);

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strSchoolId = getActivity().getIntent().getExtras().getString("schoolId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);

        tvCurHWRollNo.setText(strRollNo);
        tvCurHWClass.setText(strClass);
        tvCurHWStudentName.setText(strStudentnName);

        recyclerAdapter = new HmWrkRecyclerViewAdapter(homeworkArray, getActivity(),strSchoolId);
      recyclerAdapter.setOnButtonClickListener(new HmWrkRecyclerViewAdapter.OnHomeworkClickListener() {
            @Override
            public void getSyllabus(JSONObject homeworkData, int position, String strSubjectId) {
                android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to view the Syllabus for this subject?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), SubjectSyllabusActivity.class);
                        Bundle bundle= new Bundle();
                        try{

                           String  strSubjectId=homeworkData.getString(getString(R.string.key_SubjectId));
                           String  strSubject=homeworkData.getString(getString(R.string.key_Subject));
                            bundle.putString("SubjectId",strSubjectId);
                            bundle.putString("class",strClass);
                            bundle.putString("Subject",strSubject);
                            bundle.putString("StudentId",strStudentId);

                            intent.putExtras(bundle);
                            startActivity(intent);


                        }catch(Exception e){
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                android.app.AlertDialog dailog= builder.create();
                dailog.show();
            }


            @Override
            public void onDelete(int position, String noticeId) {

            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHmWrk.setLayoutManager(layoutManager);
        recyclerViewHmWrk.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHmWrk.setAdapter(recyclerAdapter);





    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_home_work, container, false);
        return view;
    }

    private class GetCurrentHomeWork extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("fetching the HomeWork");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_homeworkDisplayDefault));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_homeworkData),homeworkData);
                Log.e("TAG", "GetCurrentHomeWork doInBackground, outObject=" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurrentHomeWork doInBackground, respText=" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    for(int i=0;i<resultArray.length();i++){
                        homeworkArray.put(resultArray.getJSONObject(i));
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
                            builder.setNegativeButton("Cancel", null);
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
            try {
                /*HmWrkRecyclerViewAdapter recyclerAdapter = new HmWrkRecyclerViewAdapter(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewHmWrk.setLayoutManager(layoutManager);
                recyclerViewHmWrk.setItemAnimator(new DefaultItemAnimator());
                recyclerViewHmWrk.setAdapter(recyclerAdapter);*/
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}
