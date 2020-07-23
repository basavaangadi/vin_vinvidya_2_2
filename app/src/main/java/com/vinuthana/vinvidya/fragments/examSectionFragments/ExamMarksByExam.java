package com.vinuthana.vinvidya.fragments.examSectionFragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExmMarksRecycler;
import com.vinuthana.vinvidya.adapters.SpinnerDataAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamMarksByExam extends Fragment {
    View view;
    String strExam, strStudentnName, strClass,strStatus;
    RadioGroup radioGroup;
    Spinner spnrExamMarksByExam;
    RecyclerView recylerExmMrkByExmFragOne;
    ArrayList<String> listItem = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView tvExmMrksByExamStudentName, tvExmMrksByExamClass, tvExmMrkTotal,tvExmTvTotal,tvExmMrkByExmMaxTotal;
    CurrentExamMarks currentExamMarks= new CurrentExamMarks();
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId,strTotalMarks;
    JSONArray result;
    LinearLayout exmrksLyt;
    CardView crdViewExmMrkByMarks,crdViewCurExmMrk;
    public ExamMarksByExam() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        allEvents();
        FragmentManager examFrag = getFragmentManager();
        Fragment exmFm = new ExamMarksByExamFragment();
        Bundle arg = new Bundle();

        arg.putString("strExam", strExam);
        exmFm.setArguments(arg);
        examFrag.beginTransaction().replace(R.id.frmLayoutMarks, exmFm).commit();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new GetExams().execute();

        }
    }

    public void initialisation() {
        radioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup1);
        spnrExamMarksByExam = (Spinner) getActivity().findViewById(R.id.spnrExamMarksByExam);
        tvExmMrksByExamStudentName = (TextView) getActivity().findViewById(R.id.tvExmMrksByExamStudentName);
        tvExmMrksByExamClass = (TextView) getActivity().findViewById(R.id.tvExmMrksByExamClass);
        recylerExmMrkByExmFragOne = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmMrkByExm);
        tvExmMrkTotal = (TextView) getActivity().findViewById(R.id.tvExmMrkTotal);
        tvExmTvTotal = (TextView) getActivity().findViewById(R.id.tvExmTvTotal);
        tvExmMrkByExmMaxTotal=getActivity().findViewById(R.id.tvExmMrkByExmMaxTotal);
        crdViewExmMrkByMarks=getActivity().findViewById(R.id.crdViewExmMrkByMarks);
       /* CurrentExamMarks currentExamMarks=new CurrentExamMarks();
        currentExamMarks.crdViewCurExmMrk=getActivity().findViewById(R.id.crdViewCurExmMrk);
        currentExamMarks.crdViewCurExmMrk.setVisibility(View.GONE);
       crdViewCurExmMrk=getActivity().findViewById(R.id.crdViewCurExmMrk);*/

        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strExam = getArguments().getString("strExam");
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);
        strClass = hashMap.get(StudentSPreference.STR_CLASS);
        strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        tvExmMrksByExamClass.setText(strClass);
        tvExmMrksByExamStudentName.setText(strStudentnName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exam_marks_by_exam, container, false);

        return view;
    }

    private void allEvents() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioExamMarks:
                        FragmentManager examFrag = getFragmentManager();
                        Fragment exmFm = new ExamMarksByExamFragment();
                        Bundle arg = new Bundle();
                        arg.putString("strExam", strExam);
                        exmFm.setArguments(arg);
                        examFrag.beginTransaction().replace(R.id.frmLayoutMarks, exmFm).commit();
                        recylerExmMrkByExmFragOne.setVisibility(View.VISIBLE);
                        crdViewExmMrkByMarks.setVisibility(View.VISIBLE);
                        tvExmMrkTotal.setVisibility(View.VISIBLE);
                        tvExmTvTotal.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioGraph:
                        FragmentManager graphFrag = getFragmentManager();
                       /* Fragment graphFm = new GraphFragment();
                        Bundle args = new Bundle();
                        args.putString("strExam", strExam);
                        graphFm.setArguments(args);
                        graphFrag.beginTransaction().replace(R.id.frmLayoutMarks, graphFm).commit();
                        recylerExmMrkByExmFragOne.setVisibility(View.GONE);
                        crdViewExmMrkByMarks.setVisibility(View.GONE);
                            tvExmMrkTotal.setVisibility(View.GONE);
                            tvExmTvTotal.setVisibility(View.GONE);*/
                        break;
                    default:
                        break;
                }
            }
        });

        spnrExamMarksByExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) spnrExamMarksByExam.getSelectedView().findViewById(R.id.txt_exam);
                tmpView.setTextColor(Color.WHITE);
                //strExam = parent.getItemAtPosition(position).toString();
                strExam = spnrExamMarksByExam.getSelectedItem().toString();

               // Toast.makeText(getActivity(), strExam, Toast.LENGTH_SHORT).show();
                Log.e("Tag", "" + strExam);
                new GetExamMarksByExam().execute();
                FragmentManager examFrag = getFragmentManager();
                Fragment exmFm = new ExamMarksByExamFragment();
                Bundle arg = new Bundle();
                arg.putString("strExam", strExam);
                exmFm.setArguments(arg);
                examFrag.beginTransaction().replace(R.id.frmLayoutMarks, exmFm).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class GetExams extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getExamName));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_examData), userData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("ExamMarksByExam33", "outObject =" + outObject.toString());
                Log.e("ExamMarksByExam33", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {

                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

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
            } catch (Exception ex) {
                Log.e("doInBackground",ex.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            SpinnerDataAdapter adapter = new SpinnerDataAdapter(values[0], getActivity());
            spnrExamMarksByExam.setPrompt("Choose Exam");
            spnrExamMarksByExam.setAdapter(adapter);

        }
    }

    private class GetExamMarksByExam extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        JSONObject inObject;
        String url = AD.url.base_url + "examsectionOperation.jsp";
        String strTotalMarks = "",strTotalMaxMarks="";
        ArrayList<BarEntry> obtainedMarksSet = new ArrayList<BarEntry>();
        ArrayList<BarEntry> maxMarksList = new ArrayList<BarEntry>();
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Current Exam Marks..");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplayExamwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), strExam);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("TAG", "ExamMarksByExam, doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                inObject = new JSONObject(respText);
                result = inObject.getJSONArray(getString(R.string.key_Result));
                Log.e("ExamMarksByExam", "result = " + result);
                Log.e("tag", "resLen = " + result.length());
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    strTotalMarks = Double.toString(inObject.getDouble(getString(R.string.key_Total)));
                    strTotalMaxMarks=Double.toString(inObject.getDouble(getString(R.string.key_MaxTotal)));
                    publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));

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
                Log.e("doInBackground",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ExmMarksRecycler recyclerAdapter = new ExmMarksRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recylerExmMrkByExmFragOne.setLayoutManager(layoutManager);
            recylerExmMrkByExmFragOne.setItemAnimator(new DefaultItemAnimator());
            recylerExmMrkByExmFragOne.setAdapter(recyclerAdapter);
            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strClass = jsonObject.getString(getString(R.string.key_class));
                String strExam = jsonObject.getString(getString(R.string.key_Exam));
               // tvExmMrksSection.setText(strClass);
                tvExmMrkTotal.setText(strTotalMarks);
                tvExmMrkByExmMaxTotal.setText(strTotalMaxMarks);
                //tvCurExmMrksExam.setText(strExam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
