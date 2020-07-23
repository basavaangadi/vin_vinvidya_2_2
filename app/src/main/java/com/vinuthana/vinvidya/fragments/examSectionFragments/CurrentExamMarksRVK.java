package com.vinuthana.vinvidya.fragments.examSectionFragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExmMarksRecycler;
import com.vinuthana.vinvidya.adapters.ExmMarksRecylerGDGB;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentExamMarksRVK extends Fragment {

    String strStudentId,strSchoolId,strExamId;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();

    LinearLayout lynrLytCurExamMarksDetailsRVK,lnrLytCurExamMarksStudentDetailsRVK;
    TextView tvCurExamMarksStudentRVK, tvCurExamMarksClassRVK, tvCurExamMarksExamRVK;
    TextView tvCurExamMarksGradeRVK, tvCurExamMarksTotalRVK, tvCurExamMarksSubjectRVK;
    TextView tvCurExamMaxMarksTotalRVK,tvCurExamMarksPercentageRVK, tvCurExmGradeRVK;
    RecyclerView recyclerViewCurExamMarksRVK;
    CardView crdViewCurExamMarksTotalGradeRVK,crdCurExamMarksPercentageDetailsRVK;

    public CurrentExamMarksRVK() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_exam_marks_rvk, container, false);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strSchoolId=getActivity().getIntent().getExtras().getString("schoolId");

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            Log.e("oncreateView CurRVK",e.toString());
        }
        return view;
    }
     public void initilation(){

         lynrLytCurExamMarksDetailsRVK  =getActivity().findViewById(R.id.lynrLytCurExamMarksDetailsRVK);
         lnrLytCurExamMarksStudentDetailsRVK  =getActivity().findViewById(R.id.lnrLytCurExamMarksStudentDetailsRVK);
         tvCurExamMarksStudentRVK =getActivity().findViewById(R.id.tvCurExamMarksStudentRVK);
         tvCurExamMarksClassRVK =getActivity().findViewById(R.id.tvCurExamMarksClassRVK);
         tvCurExamMarksExamRVK =getActivity().findViewById(R.id.tvCurExamMarksExamRVK);
         tvCurExamMarksSubjectRVK =getActivity().findViewById(R.id.tvCurExamMarksSubjectRVK);
         tvCurExamMarksGradeRVK =getActivity().findViewById(R.id.tvCurExamMarksGradeRVK);
        tvCurExamMaxMarksTotalRVK =getActivity().findViewById(R.id.tvCurExamMaxMarksTotalRVK);
         tvCurExmGradeRVK =getActivity().findViewById(R.id.tvCurExmGradeRVK);
         recyclerViewCurExamMarksRVK =getActivity().findViewById(R.id.recyclerViewCurExamMarksRVK);

         crdViewCurExamMarksTotalGradeRVK =getActivity().findViewById(R.id.crdViewCurExamMarksTotalGradeRVK);
         crdCurExamMarksPercentageDetailsRVK =getActivity().findViewById(R.id.crdCurExamMarksPercentageDetailsRVK);
         tvCurExamMarksPercentageRVK =getActivity().findViewById(R.id.tvCurExamMarksPercentageRVK);
         tvCurExamMarksTotalRVK =getActivity().findViewById(R.id.tvCurExamMarksTotalRVK);
     }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initilation();
    }

    @Override
    public void onStart() {
        super.onStart();
        new GetExamMarksByExamRVK().execute();
    }



    class GetExamMarksByExamRVK extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";
        String strTotalMarks = "",strMaxTotalMarks="",strStatus;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the  Exam Marks..");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            try {
                String Tag="ExamMarksByExamRVK";
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_examResultDisplay_Studentwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);

                outObject.put(getString(R.string.key_examData), examData);
                Log.e(Tag, " , doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e(Tag, " , doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);


                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));

                    String strTotalGrade=inObject.getString(getString(R.string.key_TotalGrade));
                  Double dblTotalMarks=inObject.getDouble(getString(R.string.key_Total));
                    String strTotalMarks=dblTotalMarks.toString();
                    int dblMaxTotalMarks=inObject.getInt(getString(R.string.key_MaxTotal));
                    String strMaxTotalMarks=String.valueOf(dblMaxTotalMarks);
                    StringBuilder builderPercentage = new StringBuilder(100);
                    String strPercentage=inObject.getString(getString(R.string.key_Percentage));
                   builderPercentage.append(strPercentage);
                   builderPercentage.append("%");



                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            if(!strTotalGrade.equalsIgnoreCase("")){
                                tvCurExamMarksTotalRVK.setText(strTotalMarks);
                                tvCurExamMaxMarksTotalRVK.setText(strMaxTotalMarks);
                                if(!((strPercentage.equalsIgnoreCase("NA"))||(strPercentage.equalsIgnoreCase("")))){
                                    tvCurExamMarksPercentageRVK.setText(builderPercentage);
                                }else {
                                    tvCurExamMarksPercentageRVK.setText(strPercentage);
                                }


                                tvCurExmGradeRVK.setText(strTotalGrade);
                               }

                        }
                    });
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
                //publishProgress(result);
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
            recyclerViewCurExamMarksRVK.setLayoutManager(layoutManager);
            recyclerViewCurExamMarksRVK.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurExamMarksRVK.setAdapter(recyclerAdapter);

            /*ExmMarksRecylerGDGB recyclerAdapter = new ExmMarksRecylerGDGB(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCurExamMarksRVK.setLayoutManager(layoutManager);
            recyclerViewCurExamMarksRVK.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurExamMarksRVK.setAdapter(recyclerAdapter);*/

            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString(getString(R.string.key_studentname));
                String strClass = jsonObject.getString(getString(R.string.key_class));
                String strExam = jsonObject.getString(getString(R.string.key_Exam));
                tvCurExamMarksClassRVK.setText(strClass);
                tvCurExamMarksExamRVK.setText(strExam);
                tvCurExamMarksStudentRVK.setText(strStudentName);
                if (strStudentName.length() > 1) {
                    lynrLytCurExamMarksDetailsRVK.setVisibility(View.VISIBLE);
                    lnrLytCurExamMarksStudentDetailsRVK.setVisibility(View.VISIBLE);

                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
