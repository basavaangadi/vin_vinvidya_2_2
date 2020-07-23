package com.vinuthana.vinvidya.fragments.noticeBoardFragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ClassNoticeRecylerViewAdapter;
import com.vinuthana.vinvidya.adapters.PrntNtRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentClassNoticeFragment extends Fragment {

    View view;
    TextView tvCurClassNoteStudentName, tvCurClassNoteClass, tvCurClassNoteRollNo;
    RecyclerView rcyclrVwCurClassNt;
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus;


    public CurrentClassNoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
        new GetCurrentClassNote().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_class_notice, container, false);
    }
    private void initialisation() {
        tvCurClassNoteStudentName = (TextView) getActivity().findViewById(R.id.tvCurClassNoteStudentName);
        tvCurClassNoteClass = (TextView) getActivity().findViewById(R.id.tvCurClassNoteClass);
        tvCurClassNoteRollNo = (TextView) getActivity().findViewById(R.id.tvCurClassNoteRollNo);
        rcyclrVwCurClassNt = (RecyclerView) getActivity().findViewById(R.id.rcyclrVwCurClassNt);

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
        tvCurClassNoteRollNo.setText(Html.fromHtml(rollText));
        //tvCurClassNoteRollNo.setText(strRollNo);
        String classText = "<font color=#FF8C00>Class : </font> <font color=#ffffff>"+strClass+"</font>";
        tvCurClassNoteClass.setText(Html.fromHtml(classText));
        tvCurClassNoteStudentName.setText(strStudentnName);
    }

    private class GetCurrentClassNote extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Fetching Class Note...");
            progressDialog.show();
        }

        //@RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClassNoticeForStudent));
                JSONObject parentsNoteData = new JSONObject();
                parentsNoteData.put(getString(R.string.key_StudentId), strStudentId);
                parentsNoteData.put(getString(R.string.key_fromDate),"01-01-0001");
                parentsNoteData.put(getString(R.string.key_toDate),"01-01-0001");
                outObject.put(getString(R.string.key_noticeBoardData), parentsNoteData);
                Log.e("TAG", "GetPreviousParentNote,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetPreviousParentNote,doInBackground, respText =" + respText);
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
                            View convertView = (View) inflater.inflate(R.layout.custom,null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel",null);
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
            ClassNoticeRecylerViewAdapter adapter = new ClassNoticeRecylerViewAdapter(values[0], getActivity());
            RecyclerView.LayoutManager curPrntNtLytMngr = new LinearLayoutManager(getActivity());
            rcyclrVwCurClassNt.setLayoutManager(curPrntNtLytMngr);
            rcyclrVwCurClassNt.setItemAnimator(new DefaultItemAnimator());
            rcyclrVwCurClassNt.setAdapter(adapter);
           /* try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String strStudentName = jsonObject.getString("StudentName");
                String strClass = jsonObject.getString("Class");
                tvCurClassNoteStudentName.setText(strStudentName);
                tvCurParentNoteSection.setText(strClass);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}
