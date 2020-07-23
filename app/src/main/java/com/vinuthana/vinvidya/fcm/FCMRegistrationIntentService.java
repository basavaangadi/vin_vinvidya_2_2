package com.vinuthana.vinvidya.fcm;

import android.Manifest;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.vinuthana.vinvidya.utils.CheckConnection;

import com.google.firebase.iid.FirebaseInstanceId;
import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.AD;

import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Anirudh on 10/07/16. IntentService
 */
public class FCMRegistrationIntentService extends IntentService {
    Session session;
    String strPhoneNumber,token,strImei;
    CheckConnection connection = new CheckConnection();
    public static final String TAG = "FCMRegIntentService";

    public FCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"OnHandleIntent entered");
        this.registerFCM();
    }

    public void registerFCM() {

        Intent registrationComplete = null;
        token = null;
        session = new Session(FCMRegistrationIntentService.this);
        HashMap<String, String> user = session.getUserDetails();

        strPhoneNumber = user.get(Session.KEY_PHONE_NO);

        try {

            token = FirebaseInstanceId.getInstance().getToken();


            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
             strImei = telephonyManager.getDeviceId();
            Log.e(TAG, "token: " + token);
            Log.e(TAG, "StaffId: " + strPhoneNumber);
            Log.e(TAG, "deviceID: " + strImei);
            //notify UI that registration Successfull
            registrationComplete =  new Intent(FCMUtils.messages.REG_SUCCESS);
            registrationComplete.putExtra("token",token);
          //  SharedPreferences preferences = getSharedPreferences(config.sp.KYE_PREF_NAME,0);
           // boolean tokenStored = preferences.getBoolean(config.sp.SP_KEY_STORED_TOKEN,false);
            boolean tokenStored=session.isDeviceDetailsStored();

            Log.e("TAG", token+"Length = "+token.length());
            if(!tokenStored) {
                if (connection.netInfo(FCMRegistrationIntentService.this)) {
                    new SubmitToken().execute();
                }
            }
        }catch (Exception ee){
            Log.e(TAG,ee.toString());
            registrationComplete =  new Intent(FCMUtils.messages.REG_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

      class SubmitToken extends AsyncTask<String,JSONArray,String> {
         ProgressDialog progressDialog= new ProgressDialog(FCMRegistrationIntentService.this);
          @Override
          protected void onPreExecute() {
              super.onPreExecute();
          }

          @Override
          protected String doInBackground(String... strings) {
              GetResponse response = new GetResponse();
              JSONObject outObject = new JSONObject();
              String url= AD.url.base_url+"userOperations.jsp";
              try {                                                                         //submitStaffUserToken
                  outObject.put(getString(R.string.key_OperationName), getString(R.string.web_submitParentUserToken));
                  JSONObject userData = new JSONObject();

                  userData.put(getString(R.string.key_PhoneNumber), strPhoneNumber);
                  userData.put(getString(R.string.key_UserToken), token);
                  userData.put(getString(R.string.key_DeviceId), strImei);

                  outObject.put(getString(R.string.key_userData), userData);
                  String responseText = response.getServerResopnse(url, outObject.toString());
                  JSONObject inObject = new JSONObject(responseText);
                  Log.e("Tag", "outObject =" + outObject.toString());
                  Log.e("Tag", "responseText is =" + responseText);
                  String strMsg = inObject.getString(getString(R.string.key_Message));
                  int resid=inObject.getInt(getString(R.string.key_resultId));
                  String strStatus=inObject.getString(getString(R.string.key_Status));

                  Log.e("FCM Doinbacground",strStatus);
                  return String.valueOf(resid);

              } catch (Exception ex) {
                  Log.e("doinbg Execption", ex.toString());
                  return ex.toString();
              }
          }

          @Override
          protected void onPostExecute(String strDoinStatus) {
              super.onPostExecute(strDoinStatus);
              Log.e("PostExecute","this is run "+strDoinStatus);
              if(strDoinStatus.equalsIgnoreCase("-2")||strDoinStatus.equalsIgnoreCase("-1")) {

                 session.setDeviceDetails(true,strImei);

                  Toast.makeText(FCMRegistrationIntentService.this, "device has been Registered", Toast.LENGTH_SHORT).show();
              }else   {

                  session.setDeviceDetails(false,strImei);

                  Toast.makeText(FCMRegistrationIntentService.this, "device not Registered", Toast.LENGTH_SHORT).show();
              }
          }
      }
}
