package com.vinuthana.vinvidya.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.vinuthana.vinvidya.activities.useractivities.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Krish on 05-10-2017.
 */

public class Session {
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NO = "phoneNo";
   // public static final String KEY_USER_NAME = "userName";
    public static final String KEY_NUM_CHILD = "numChild";
    public static final String KEY_STUD_NAME = "studname";
    private static final String PREFE_NAME = "Session";
    private static final String IS_LOGIN = "IsLoggedin";
    public static  final String KEY_USER_TOKEN="UserToken";
    public static final String KEY_DEVICE_ID="DeviceID";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    ArrayList<String> list = new ArrayList<>();


    public Session(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean("loggedInmode", loggedIn);
        editor.commit();
    }
//String uName,
    public void createLoginSession(String name, String email, String pNum,  String numChild) {
        //editor.putBoolean(IS_LOGIN,true);
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE_NO, pNum);
     //   editor.putString(KEY_USER_NAME, uName);
        editor.putString(KEY_NUM_CHILD, numChild);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.loggedin()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    public void setDeviceDetails(Boolean tokenStored,String deviceId){
        editor.putBoolean(KEY_USER_TOKEN, tokenStored);
        editor.putString(KEY_DEVICE_ID, deviceId);
        editor.commit();
    }
    public boolean isDeviceDetailsStored(){
        return preferences.getBoolean(KEY_USER_TOKEN,false);
    }

    /*Get stored session data*/
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, preferences.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        user.put(KEY_PHONE_NO, preferences.getString(KEY_PHONE_NO, null));
       // user.put(KEY_USER_NAME, preferences.getString(KEY_USER_NAME, null));
        user.put(KEY_NUM_CHILD, preferences.getString(KEY_NUM_CHILD, null));
        return user;
    }

    public ArrayList<String> arrayList() {
        ArrayList<String> strList = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            strList.add(i,preferences.getString("list",strList.get(i)));
        }
        return strList;
    }

    /**
     * Clear session details
     */
    public void logOut() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean loggedin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
