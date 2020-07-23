package com.vinuthana.vinvidya.activities.useractivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.extraactivities.LandingActivity;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edtxbOldPwdChangePwd, edtxbNewPwdChangePwd, edtxbCnfrmPwdChangePwd;
    Button btnChangePassword;
    TextView tvLogin;
    public String strPassword, strConfrmPwd, strOldPwd,strResPassword="";
    public String oldPwd, newPwd, confrmPwd;
    public String strPhoneNumber;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChangePwd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        initialisation();
        allEvents();

        session = new Session(this);
        //Toast.makeText(ListActivity_new.this, "User Login Status: " + session.loggedin(), Toast.LENGTH_SHORT).show();
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        strPhoneNumber = user.get(Session.KEY_PHONE_NO);
    }

    private void allEvents() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOldPwd = edtxbOldPwdChangePwd.getText().toString();
                strPassword = edtxbNewPwdChangePwd.getText().toString();
                strConfrmPwd = edtxbCnfrmPwdChangePwd.getText().toString();
                if (strOldPwd.equals("") || strPassword.equals("") || strConfrmPwd.equals("")) {
                    //Toast.makeText(ChangePasswordActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    edtxbOldPwdChangePwd.setError("Enter Old Password");
                    edtxbNewPwdChangePwd.setError("Enter New Password");
                    edtxbCnfrmPwdChangePwd.setError("Enter Confirm Password");
                } else {
                    new getPassword().execute();
                    if(strResPassword.equals(strOldPwd)){
                        new ChangePassword().execute();
                    }else {
                        Toast.makeText(ChangePasswordActivity.this, "Wrong current password", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        edtxbCnfrmPwdChangePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                strPassword = edtxbNewPwdChangePwd.getText().toString();
                strConfrmPwd = edtxbCnfrmPwdChangePwd.getText().toString();
                if (!strConfrmPwd.equals(strPassword)) {
                    edtxbCnfrmPwdChangePwd.setError("Password Do Not Match");
                }
            }
        });
    }
class  getPassword extends AsyncTask<String, JSONObject, Void> {
    ProgressDialog progressDialog;
    String url = AD.url.base_url + "userOperations.jsp";
    String message = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Changing the Password");
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            JSONObject outObject = new JSONObject();
            outObject.put("OperationName", "getPassword");
            JSONObject userData = new JSONObject();
            userData.put("PhoneNumber", strPhoneNumber);
           // userData.put("Password", strPassword);
            outObject.put("userData", userData);
            Log.e("TAG", "ChangePassword, doInBackground, outObject =" + outObject.toString());
            GetResponse response = new GetResponse();
            String respText = response.getServerResopnse(url, outObject.toString());
            Log.e("TAG", "ChangePassword, doInBackground, respText =" + respText);
            JSONObject inObject = new JSONObject(respText);
            JSONArray result= inObject.getJSONArray("Result");
            //JSONArray result=new JSONArray();
            JSONObject respObject= result.getJSONObject(0);
            strResPassword = respObject.getString("password");
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String alertMessage, alertTitle;
                    if (message.equals("Password Changed successfully")) {
                        alertMessage = message;
                        alertTitle = "Success";
                    } else if (message.equals("Password could not be Changed")) {
                        alertMessage = message;
                        alertTitle = "Warning";
                    } else {
                        alertTitle = "Error";
                        alertMessage = "Error Occurred While Changing Password..";
                    }
                    showAlert(alertMessage, alertTitle);
                    //Toast.makeText(ChangePasswordActivity.this, "Passwor Changed Successfully", Toast.LENGTH_SHORT).show();
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        /*if (edtxbOldPwdChangePwd.equals("") || edtxbNewPwdChangePwd.equals("") || edtxbCnfrmPwdChangePwd.equals("")) {
            //showAlert("Please Enter all fields","Change Password");
            Toast.makeText(ChangePasswordActivity.this, "Please Enter all fields", Toast.LENGTH_SHORT).show();
        } else {
            //showAlert("Password Successfully Changed","Change Password");
            Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
        }
        edtxbOldPwdChangePwd.setText("");
        edtxbNewPwdChangePwd.setText("");
        edtxbCnfrmPwdChangePwd.setText("");*/
    }

    @Override
    protected void onProgressUpdate(JSONObject... values) {
        super.onProgressUpdate(values);
    }
}



    class ChangePassword extends AsyncTask<String, JSONObject, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "userOperations.jsp";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChangePasswordActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Changing the Password");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                    outObject.put("OperationName", "changePassword");
                JSONObject userData = new JSONObject();
                userData.put("PhoneNumber", strPhoneNumber);
                userData.put("Password", strPassword);
                outObject.put("userData", userData);
                Log.e("TAG", "ChangePassword, doInBackground, outObject =" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "ChangePassword, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                message = inObject.getString("Message");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (message.equals("password changed  successfully")) {
                            alertMessage = message;
                            alertTitle = "Success";
                        } else if (message.equals("Password could not be Changed")) {
                            alertMessage = message;
                            alertTitle = "Warning";
                        } else {
                            alertTitle = "Error";
                            alertMessage = "Error Occurred While Changing Password..";
                        }
                        showAlert(alertMessage, alertTitle);
                        //Toast.makeText(ChangePasswordActivity.this, "Passwor Changed Successfully", Toast.LENGTH_SHORT).show();
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

            if (edtxbOldPwdChangePwd.equals("") || edtxbNewPwdChangePwd.equals("") || edtxbCnfrmPwdChangePwd.equals("")) {
                //showAlert("Please Enter all fields","Change Password");
                Toast.makeText(ChangePasswordActivity.this, "Please Enter all fields", Toast.LENGTH_SHORT).show();
            } else {


                //showAlert("Password Successfully Changed","Change Password");
               // Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            }
            edtxbOldPwdChangePwd.setText("");
            edtxbNewPwdChangePwd.setText("");
            edtxbCnfrmPwdChangePwd.setText("");
        }

        @Override
        protected void onProgressUpdate(JSONObject... values) {
            super.onProgressUpdate(values);
        }
    }





    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent navigate= new Intent(ChangePasswordActivity.this, LandingActivity.class);
                startActivity(navigate);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialisation() {
        edtxbOldPwdChangePwd = (EditText) findViewById(R.id.edtxbOldPwdChangePwd);
        edtxbNewPwdChangePwd = (EditText) findViewById(R.id.edtxbNewPwdChangePwd);
        edtxbCnfrmPwdChangePwd = (EditText) findViewById(R.id.edtxbCnfrmPwdChangePwd);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
    }
}
