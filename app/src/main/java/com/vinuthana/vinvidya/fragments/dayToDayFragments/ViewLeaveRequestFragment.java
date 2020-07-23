package com.vinuthana.vinvidya.fragments.dayToDayFragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.daytoday.EditLeaveRequestActivity;
import com.vinuthana.vinvidya.adapters.DayToDayAdapter.LeaveRequestAdapter;
import com.vinuthana.vinvidya.adapters.NoticeRecyclerViewAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewLeaveRequestFragment extends Fragment {

    TextView tvStudLeaveStudentName, tvStudLeaveClass, tvStudLeaveRollNo;
    LeaveRequestAdapter recyclerViewAdapter;
    View view;
    RecyclerView rcyclrVwStudLeave;
    JSONArray studLeaveArray = new JSONArray();
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId, strStatus,strLeavrequestId;



    public ViewLeaveRequestFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialisation();
       
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    private void initialisation() {
        tvStudLeaveStudentName = (TextView) getActivity().findViewById(R.id.tvStudLeaveStudentName);
        tvStudLeaveStudentName =  getActivity().findViewById(R.id.tvStudLeaveStudentName);
        tvStudLeaveClass = (TextView) getActivity().findViewById(R.id.tvStudLeaveClass);
        tvStudLeaveRollNo = (TextView) getActivity().findViewById(R.id.tvStudLeaveRollNo);
        rcyclrVwStudLeave= (RecyclerView) getActivity().findViewById(R.id.rcyclrVwStudLeave);
        


        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);
        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);

        tvStudLeaveRollNo.setText(strRollNo);
        tvStudLeaveClass.setText(strClass);
        tvStudLeaveStudentName.setText(strStudentnName);

        recyclerViewAdapter = new LeaveRequestAdapter(studLeaveArray, getActivity());
        recyclerViewAdapter.setOnButtonClickListener(new LeaveRequestAdapter.onLeaveClickListener() {
            @Override
            public void onLeaveEdit(JSONObject leaveRequestData, int position) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Alert");
                alertDialogBuilder.setMessage("Would you like to Edit this Leave");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent intent = new Intent(getActivity(), EditLeaveRequestActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("leaveRequestData",leaveRequestData.toString());
                            Log.e("leaveRequestData",leaveRequestData.toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }

            @Override
            public void onLeaveDelete(JSONObject leaveRequestData, int position) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Alert");
                alertDialogBuilder.setMessage("Would you like to delete this Leave");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                          strLeavrequestId= leaveRequestData.getString(getString(R.string.key_StudentLeaveRequestId));
                            new DeleteStudentLeave().execute();
                        } catch (Exception e) {
                            Toast toast= Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        rcyclrVwStudLeave.setLayoutManager(prntNtLytMngr);
        rcyclrVwStudLeave.setItemAnimator(new DefaultItemAnimator());
        rcyclrVwStudLeave.setAdapter(recyclerViewAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_view_leave_request, container, false);
        try {
            strStudentId = getActivity().getIntent().getExtras().getString("studentId");
            new GetLeaveRequest().execute();
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("fetchingStudent",e.toString());
        }

        studentSPreference = new StudentSPreference(getActivity());
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);
         return view;
    }

    private class GetLeaveRequest extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog =new ProgressDialog(getActivity());;

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog =
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Leave request...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = studLeaveArray.length(); i >= 0; i--) {

                    studLeaveArray.remove(i);
                }
            }else{
                studLeaveArray= new JSONArray();
            }

        }


        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStudentwiseLeaveList));
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_StudentId), strStudentId);
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                Log.e("TAG", "view leave request,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "view leave request,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);

                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray result = inObject.getJSONArray("Result");
                    for(int i=0;i<result.length();i++)
                        studLeaveArray.put(result.getJSONObject(i));
                   // publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
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
                Log.e("curntNoticeDoinBg",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values[0]);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            //recyclerViewAdapter.notifyDataSetChanged();
            recyclerViewAdapter = new LeaveRequestAdapter(studLeaveArray, getActivity());
            recyclerViewAdapter.setOnButtonClickListener(new LeaveRequestAdapter.onLeaveClickListener() {
                @Override
                public void onLeaveEdit(JSONObject leaveRequestData, int position) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Alert");
                    alertDialogBuilder.setMessage("Would you like to Edit this Leave");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                Intent intent = new Intent(getActivity(), EditLeaveRequestActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("leaveRequestData",leaveRequestData.toString());
                                Log.e("leaveRequestData",leaveRequestData.toString());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                Log.e("onPostExecute",e.toString());
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                }

                @Override
                public void onLeaveDelete(JSONObject leaveRequestData, int position) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Alert");
                    alertDialogBuilder.setMessage("Would you like to delete this Leave");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                strLeavrequestId= leaveRequestData.getString(getString(R.string.key_StudentLeaveRequestId));
                                new DeleteStudentLeave().execute();
                            } catch (Exception e) {
                                Log.e("onLeaveDelete",e.toString());
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            });
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            rcyclrVwStudLeave.setLayoutManager(prntNtLytMngr);
            rcyclrVwStudLeave.setItemAnimator(new DefaultItemAnimator());
            rcyclrVwStudLeave.setAdapter(recyclerViewAdapter);
                  }
    }

    class DeleteStudentLeave extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog= new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Deleting the Parent's Note...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = AD.url.base_url + "studentAttendanceOperation.jsp";
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteStudentLeave));
                JSONObject studentAttendanceData = new JSONObject();
                studentAttendanceData.put(getString(R.string.key_Id), strLeavrequestId);

                outObject.put(getString(R.string.key_studAttendanceData), studentAttendanceData);
                Log.e("TAG", "Delete Parents Note,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "Delete Parents Note,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strMessage="";
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                    strMessage=inObject.getString(getString(R.string.key_Message));
                    showAlert(strMessage,strStatus);
                    //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    showAlert("Something went wrong Try again","Error");
                }

                /*JSONArray result = inObject.getJSONArray(getString(R.string.key_Result));
                publishProgress(result);*/
            } catch (Exception e) {
                Log.e("onLeaveDelete",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        private void showAlert(String alertMessage, String alertTitle) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(alertMessage);
                    builder.setTitle(alertTitle);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          //  rcyclrVwStudLeave=null;
                            new GetLeaveRequest().execute();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

}
