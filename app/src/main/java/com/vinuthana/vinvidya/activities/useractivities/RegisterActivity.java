package com.vinuthana.vinvidya.activities.useractivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText  edtxbRegisterPwd, edtxbRegisterEmail, edtxbRegisterPhoneNo, edtxbRegisterConfrmPwd, edtxbRegisterName;
    //EditText edtxbRegisterUname;
    Button btnRegister;
    TextView tvLoginRegister;
    String status,message;
    int resultId;
    String strEmail, strPhoneNumber, strUserName, strPassword, strCnfrmPassword, strName;
    public Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialisation();
        allEvents();
    }

    private void allEvents() {
        edtxbRegisterPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

              //  new CheckPhoneNumber().execute();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = edtxbRegisterName.getText().toString();
              //  strUserName = edtxbRegisterUname.getText().toString();
                strPassword = edtxbRegisterPwd.getText().toString();
                strCnfrmPassword = edtxbRegisterConfrmPwd.getText().toString();
                strEmail = edtxbRegisterEmail.getText().toString();
                strPhoneNumber = edtxbRegisterPhoneNo.getText().toString();
                if (strName.equalsIgnoreCase("") || strPassword.equalsIgnoreCase("") || strCnfrmPassword.toString().equalsIgnoreCase("")||strPhoneNumber.equalsIgnoreCase("")) {
                    //Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    if(strName.equalsIgnoreCase("")){
                        edtxbRegisterName.setError("Enter Name");
                    }
                    if(strPassword.equalsIgnoreCase("")){
                        edtxbRegisterPwd.setError("Enter Password");
                    }
                    if(strCnfrmPassword.equalsIgnoreCase("")){
                        edtxbRegisterConfrmPwd.setError("please confirm password");
                    }
                    if(strPhoneNumber.equalsIgnoreCase("")){
                        edtxbRegisterPhoneNo.setError("Please Enter Valid Phone Number");
                    }

                  //  edtxbRegisterUname.setError("Enter User Name");


                    //edtxbRegisterEmail.setError("Please Enter Valid Email");

                }else if (!validation.isValidMobile(strPhoneNumber)) {
                    edtxbRegisterPhoneNo.setError("Please Enter Valid Phone Number");
                }else if(!strPassword.equals(strCnfrmPassword)){
                    edtxbRegisterConfrmPwd.setError("password does not  match");
                }
                else {
                    new AddUser().execute();
                    // Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                }
            }
        });


        tvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initialisation() {
        edtxbRegisterName = (EditText) findViewById(R.id.edtxbRegisterName);
       // edtxbRegisterUname = (EditText) findViewById(R.id.edtxbRegisterUname);
        edtxbRegisterPwd = (EditText) findViewById(R.id.edtxbRegisterPwd);
        edtxbRegisterEmail = (EditText) findViewById(R.id.edtxbRegisterEmail);
        edtxbRegisterPhoneNo = (EditText) findViewById(R.id.edtxbRegisterPhoneNo);
        edtxbRegisterConfrmPwd = (EditText) findViewById(R.id.edtxbRegisterConfrmPwd);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvLoginRegister = (TextView) findViewById(R.id.tvLogin);
    }

    class AddUser extends AsyncTask<String, JSONObject, Void> {
        String url = AD.url.base_url + "userOperations.jsp";
        // String message = "";

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Registering the User");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", "addUser");
                JSONObject userData = new JSONObject();
               // userData.put("UserId", strUserName);
                userData.put("password", strPassword);
                userData.put("email", strEmail);
                userData.put("mobileNumber", strPhoneNumber);
                userData.put("name", strName);
                outObject.put("userData", userData);
                Log.e("Tag","OutObject : "+outObject.toString());
                Log.e("URL",url);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(respText);
                resultId=inObject.getInt(getString(R.string.key_ResultId));
                status=inObject.getString(getString(R.string.key_Status));
                message = inObject.getString("Message");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage="", alertTitle="";
                        if (resultId==-9999) {
                            alertMessage = message;
                            alertTitle = status;
                        } else if (resultId>0||resultId==-1) {
                            alertMessage = message;
                            alertTitle = status;
                        } else if(resultId==-3) {
                            alertTitle = "Error";
                            alertMessage = "Error Occured While Adding the user..";
                        }else{

                            alertTitle = "Error";
                            alertMessage = "Error Occured While Adding the user..";
                        }
                        showAlert(alertMessage, alertTitle);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
           // edtxbRegisterUname.setText("");
            edtxbRegisterPwd.setText("");
            edtxbRegisterConfrmPwd.setText("");
            edtxbRegisterEmail.setText("");
            edtxbRegisterPhoneNo.setText("");
        }

        @Override
        protected void onProgressUpdate(JSONObject... values) {
            super.onProgressUpdate(values);
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(resultId==1){
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);}
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*class CheckPhoneNumber extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "userOperation.jsp";

        String strMessage,strTitle;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_checkStudentAttendance));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_PhoneNumber), strPhoneNumber);
                //classSubjectData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_userData), userData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
               strTitle= inObject.getString(getString(R.string.key_Status));
               strMessage= inObject.getString(getString(R.string.key_Message));
                resultId = Integer.parseInt(inObject.getString(getString(R.string.key_ResultId)));



                *//*if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                }*//*
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            *//*ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrPutAtndClass.setPrompt("Choose Class");
            spnrPutAtndClass.setAdapter(adapter);*//*
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
             if (resultId ==0) {
              showAlert(str);
            }
        }
        private void showAlert(String alertTitle, String alertMessage) {
            RegisterActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage(alertMessage);
                    builder.setTitle(alertTitle);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

    }*/



}