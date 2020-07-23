package com.vinuthana.vinvidya.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.vinuthana.vinvidya.activities.useractivities.LoginActivity;


/**
 * Created by Krish on 28-10-2017.
 */

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_PASSWORD = "keypassword";
    //private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user name in shared preference
    public void userLogin(User user) {
        preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt(KEY_ID,user.getId());
        editor.putString(KEY_USERNAME,user.getUsername());
        editor.putString(KEY_PASSWORD,user.getPassword());
        editor.apply();
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USERNAME,null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                preferences.getInt(KEY_ID, -1),
                preferences.getString(KEY_USERNAME, null),
                preferences.getString(KEY_PASSWORD, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
