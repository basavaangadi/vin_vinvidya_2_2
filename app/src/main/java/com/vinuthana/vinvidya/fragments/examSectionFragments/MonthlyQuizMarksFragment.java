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
public class MonthlyQuizMarksFragment extends Fragment {

    String strStudentId,strSchoolId,strExamId;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();

    LinearLayout lynrLytCurExamMarksDetailsMQJV,lnrLytCurExamMarksStudentDetailsMQJV;
    TextView tvCurExamMarksStudentMQJV, tvCurExamMarksClassMQJV, tvCurExamMarksExamMQJV;
    TextView tvCurExamMarksGradeMQJV, tvCurExamMarksTotalMQJV, tvCurExamMarksSubjectMQJV;
    TextView tvCurExamMaxMarksTotalMQJV,tvCurExamMarksPercentageMQJV, tvCurExmGradeMQJV;
    RecyclerView recyclerViewCurExamMarksMQJV;
    CardView crdViewCurExamMarksTotalGradeMQJV,crdCurExamMarksPercentageDetailsMQJV;
    

    public MonthlyQuizMarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_monthly_quiz_marks, container, false);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            strSchoolId=getActivity().getIntent().getExtras().getString("schoolId");

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            Log.e("oncreateView CurMQJV",e.toString());
        }
        return view;
    }
    public void initilation(){

        lynrLytCurExamMarksDetailsMQJV  =getActivity().findViewById(R.id.lynrLytCurExamMarksDetailsMQJV);
        lnrLytCurExamMarksStudentDetailsMQJV  =getActivity().findViewById(R.id.lnrLytCurExamMarksStudentDetailsMQJV);
        tvCurExamMarksStudentMQJV =getActivity().findViewById(R.id.tvCurExamMarksStudentMQJV);
        tvCurExamMarksClassMQJV =getActivity().findViewById(R.id.tvCurExamMarksClassMQJV);
        //tvCurExamMarksExamMQJV =getActivity().findViewById(R.id.tvCurExamMarksExamMQJV);
        tvCurExamMarksSubjectMQJV =getActivity().findViewById(R.id.tvCurExamMarksSubjectMQJV);
        tvCurExamMarksGradeMQJV =getActivity().findViewById(R.id.tvCurExamMarksGradeMQJV);
       // tvCurExamMaxMarksTotalMQJV =getActivity().findViewById(R.id.tvCurExamMaxMarksTotalMQJV);
        //tvCurExmGradeMQJV =getActivity().findViewById(R.id.tvCurExmGradeMQJV);
        recyclerViewCurExamMarksMQJV =getActivity().findViewById(R.id.recyclerViewCurExamMarksMQJV);

        //crdViewCurExamMarksTotalGradeMQJV =getActivity().findViewById(R.id.crdViewCurExamMarksTotalGradeMQJV);
        //crdCurExamMarksPercentageDetailsMQJV =getActivity().findViewById(R.id.crdCurExamMarksPercentageDetailsMQJV);
        //tvCurExamMarksPercentageMQJV =getActivity().findViewById(R.id.tvCurExamMarksPercentageMQJV);
        //tvCurExamMarksTotalMQJV =getActivity().findViewById(R.id.tvCurExamMarksTotalMQJV);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initilation();
        /*if(getActivity()!=null) {
            new GetExamMarksByExamMQJV().execute();
        }*/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){
           if(getActivity()!=null)
            new GetExamMarksByExamMQJV().execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }



    class GetExamMarksByExamMQJV extends AsyncTask<String, JSONArray, Void> {
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
                String Tag="ExamMarksByExamMQJV";
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getExamMarksByExam_JV));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StudentId), strStudentId);
                examData.put(getString(R.string.key_ExamId), "42");

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

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

            ExmMarksRecyclerJV recyclerAdapter = new ExmMarksRecyclerJV(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCurExamMarksMQJV.setLayoutManager(layoutManager);
            recyclerViewCurExamMarksMQJV.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurExamMarksMQJV.setAdapter(recyclerAdapter);


            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString(getString(R.string.key_studentname));
                String strClass = jsonObject.getString(getString(R.string.key_class));
                String text = "<font color=#FF8C00>Class </font> <font color=#252f39>"+strClass+"</font>";
                String clsText=text+":"+strClass;
                tvCurExamMarksClassMQJV.setText(Html.fromHtml(clsText));

                //String strExam = jsonObject.getString(getString(R.string.key_Exam));
               // tvCurExamMarksClassMQJV.setText(strClass);
                //tvCurExamMarksExamMQJV.setText(strExam);
                tvCurExamMarksStudentMQJV.setText(strStudentName);
                if (strStudentName.length() > 1) {
                    lynrLytCurExamMarksDetailsMQJV.setVisibility(View.VISIBLE);
                    lnrLytCurExamMarksStudentDetailsMQJV.setVisibility(View.VISIBLE);

                }

            } catch (Exception e) {
                Log.e("doInBackground",e.toString());
            }
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
