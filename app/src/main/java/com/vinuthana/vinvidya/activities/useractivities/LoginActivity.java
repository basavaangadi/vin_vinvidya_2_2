package com.vinuthana.vinvidya.activities.useractivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.daytoday.AttendanceActivity;
import com.vinuthana.vinvidya.activities.extraactivities.LandingActivity;
import com.vinuthana.vinvidya.fcm.FCMRegistrationIntentService;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.CheckConnection;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    TextView tvForgotPassword, tvSignUp;
    String strPhonenumber, strPassword;
    JSONArray result;

    ArrayList<String> list = new ArrayList<>();
    private EditText etPhoneNumber;
    private EditText etPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private Session session;
    FCMRegistrationIntentService fcmRegistrationIntentService= new FCMRegistrationIntentService();


    //Shared Preference
    public SharedPreferences preferences;
    SharedPreferences.Editor edit;
    CheckConnection connection = new CheckConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!connection.netInfo(LoginActivity.this)) {
            connection.buildDialog(LoginActivity.this).show();
        }else{
            initialisation();
            allEvents();
          //fcmRegistrationIntentService.registerFCM();
            //Toast.makeText(LoginActivity.this, "User Login Status: " + session.loggedin(), Toast.LENGTH_SHORT).show();
            if (session.loggedin()) {
                Intent i = new Intent(LoginActivity.this, LandingActivity.class);
                startActivity(i);
                finish();
            }

            //SP
            preferences = getSharedPreferences("StudentSession", MODE_PRIVATE);

            edit = preferences.edit();
            // on login or putting data in SP file
            edit.putString("Name", "Basu");
            edit.putString("Id", "1");
            edit.putBoolean("IsLoggin", true);
            edit.commit();

            //on logout or clear data
            edit.clear();
            edit.commit();

            //getting data from SP
            String strName = preferences.getString("Name", "Name not fount");
            String strID = preferences.getString("Id", "Id not fount");
            boolean isLogin = preferences.getBoolean("IsLoggin", false);
        }





    }

    private void allEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
                finish();
            }
        });
    }

    public void login() {
        strPhonenumber = etPhoneNumber.getText().toString();
        strPassword = etPassword.getText().toString();
        if (strPhonenumber.equals("") || strPassword.equals("")) {
            etPhoneNumber.setError("Enter Username");
            etPassword.setError("Enter Password");
        } else if (strPhonenumber.equals("")) {
            etPhoneNumber.setError("Enter Username");
        } else if (strPassword.equals("")) {
            etPassword.setError("Enter Password");
        } else {
            new GetUserDetails().execute();
            /*Intent intent = new Intent(LoginActivity.this, ListActivity_new.class);
            startActivity(intent);
            finish();*/
        }
    }

    private void initialisation() {
        session = new Session(this);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class GetUserDetails extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String studentId;
        String url = AD.url.base_url + "userOperations.jsp";
        //String url ="http://192.168.43.155:8080/netvinvidyawebapi/operation/userOperations.jsp";
        Context mContext;
        SharedPreferences pref;
        String status = "";
        String strStatus = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait verifying user...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", "authUser");
                JSONObject userData = new JSONObject();
                userData.put("Phonenumber", strPhonenumber);
                userData.put("Password", strPassword);
                outObject.put("userData", userData);
                Log.e("TAG", "GetUserDetails, doInBackground, outObject =" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetUserDetails, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                result = inObject.getJSONArray("result");
                strStatus = result.getJSONObject(0).getString("Status");
                getDetails(strStatus);
                publishProgress(result);

            } catch (Exception e) {
                e.printStackTrace();
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
            if (strStatus.equals("Success")) {
                getDetails(strStatus);
                Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong UserName/Password", Toast.LENGTH_SHORT).show();
            }
        }

        public void getDetails(String strStatus) {
            if (strStatus.equals("Success")) {
                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject values = result.getJSONObject(i);
                        String strName = values.getString("name");
                        String strEmail = values.getString("email");
                        String strPhoneNumber = values.getString("mobile_number");
                      // String strUserId = values.getString("userID");
                        String strChilds = values.getString("students");
                        /*for (int j = 0; j < list.size(); j++) {
                            session.createLoginSession();
                        }*/
                        //removed from below code by basu at 23-07-2018
                        session.createLoginSession(strName, strEmail, strPhoneNumber,  strChilds);
                        Log.e("OK", "take the data");
                        Log.e("Login doinbackground", result.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Log.e("error", "Error");
            }
        }
    }
}
