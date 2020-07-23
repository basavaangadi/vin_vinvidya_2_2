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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ExamSectionAdpater.ExmMarksRecyclerJV;
import com.vinuthana.vinvidya.adapters.ExmMarksRecycler;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyQuizMarksFragment extends Fragment {

    String strStudentId,strSchoolId,strExamId;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();

    LinearLayout lynrLytCurExamMarksDetailsWQJV,lnrLytCurExamMarksStudentDetailsWQJV;
    TextView tvCurExamMarksStudentWQJV, tvCurExamMarksClassWQJV, tvCurExamMarksExamWQJV;
    TextView tvCurExamMarksGradeWQJV, tvCurExamMarksTotalWQJV, tvCurExamMarksSubjectWQJV;
    TextView tvCurExamMaxMarksTotalWQJV,tvCurExamMarksPercentageWQJV, tvCurExmGradeWQJV;
    RecyclerView recyclerViewCurExamMarksWQJV;
    CardView crdViewCurExamMarksTotalGradeWQJV,crdCurExamMarksPercentageDetailsWQJV;
    
    
    

    public WeeklyQuizMarksFragment() {
       
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_weekly_quiz_marks, container, false);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strSchoolId=getActivity().getIntent().getExtras().getString("schoolId");

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            Log.e("oncreateView CurWQJV",e.toString());
        }
        return view;
    }
    public void initilation(){

        lynrLytCurExamMarksDetailsWQJV  =getActivity().findViewById(R.id.lynrLytCurExamMarksDetailsWQJV);
        lnrLytCurExamMarksStudentDetailsWQJV  =getActivity().findViewById(R.id.lnrLytCurExamMarksStudentDetailsWQJV);
        tvCurExamMarksStudentWQJV =getActivity().findViewById(R.id.tvCurExamMarksStudentWQJV);
        tvCurExamMarksClassWQJV =getActivity().findViewById(R.id.tvCurExamMarksClassWQJV);
        //tvCurExamMarksExamWQJV =getActivity().findViewById(R.id.tvCurExamMarksExamWQJV);
        tvCurExamMarksSubjectWQJV =getActivity().findViewById(R.id.tvCurExamMarksSubjectWQJV);
        tvCurExamMarksGradeWQJV =getActivity().findViewById(R.id.tvCurExamMarksGradeWQJV);
       // tvCurExamMaxMarksTotalWQJV =getActivity().findViewById(R.id.tvCurExamMaxMarksTotalWQJV);
        //tvCurExmGradeWQJV =getActivity().findViewById(R.id.tvCurExmGradeWQJV);
        recyclerViewCurExamMarksWQJV =getActivity().findViewById(R.id.recyclerViewCurExamMarksWQJV);

        /*crdViewCurExamMarksTotalGradeWQJV =getActivity().findViewById(R.id.crdViewCurExamMarksTotalGradeWQJV);
        crdCurExamMarksPercentageDetailsWQJV =getActivity().findViewById(R.id.crdCurExamMarksPercentageDetailsWQJV);
        tvCurExamMarksPercentageWQJV =getActivity().findViewById(R.id.tvCurExamMarksPercentageWQJV);
        tvCurExamMarksTotalWQJV =getActivity().findViewById(R.id.tvCurExamMarksTotalWQJV);*/

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initilation();
        new GetExamMarksByExamWQJV().execute();

    }

    @Override
    public void onStart() {
        super.onStart();
       // new GetExamMarksByExamWQJV().execute();
    }



    class GetExamMarksByExamWQJV extends AsyncTask<String, JSONArray, Void> {
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
                String Tag="ExamMarksByExamWQJV";
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getExamMarksByExam_JV));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), "41");

                outObject.put(getString(R.string.key_examData), examData);
                Log.e(Tag, " , doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e(Tag, " , doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);


                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
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
                //publishProgress(result);
            } catch (Exception e) {
                Log.e("doInBackground,e =",  e.toString());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

            ExmMarksRecyclerJV recyclerAdapter = new ExmMarksRecyclerJV(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCurExamMarksWQJV.setLayoutManager(layoutManager);
            recyclerViewCurExamMarksWQJV.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurExamMarksWQJV.setAdapter(recyclerAdapter);


            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString(getString(R.string.key_studentname));
                String strClass = jsonObject.getString(getString(R.string.key_class));

                String text = "<font color=#FF8C00>Class </font> <font color=#252f39>"+strClass+"</font>";
                String clsText=text+":"+strClass;
                tvCurExamMarksClassWQJV.setText(Html.fromHtml(clsText));


                tvCurExamMarksStudentWQJV.setText(strStudentName);
                if (strStudentName.length() > 1) {
                    lynrLytCurExamMarksDetailsWQJV.setVisibility(View.VISIBLE);
                    lnrLytCurExamMarksStudentDetailsWQJV.setVisibility(View.VISIBLE);

                }

            } catch (Exception e) {
                Log.e("doInBackground,e =",  e.toString());
            }
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
