package com.vinuthana.vinvidya.app;

import android.app.Application;
import android.content.Context;

import com.vinuthana.vinvidya.utils.DatabaseHelper;
import com.vinuthana.vinvidya.utils.DatabaseManager;

/**
 * Created by Administrator on 12/15/2017.
 */

public class App extends Application {
    private static Context context;
    private static DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        databaseHelper = new DatabaseHelper();
        DatabaseManager.initializeInstance(databaseHelper);
    }

    public static Context getContext(){
        return context;
    }
}
