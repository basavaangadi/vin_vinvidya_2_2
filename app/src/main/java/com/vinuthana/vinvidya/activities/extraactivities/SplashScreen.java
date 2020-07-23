package com.vinuthana.vinvidya.activities.extraactivities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.otheractivities.AssignmentActivity;
import com.vinuthana.vinvidya.activities.useractivities.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    String [] mPermission={"android.permission.ACCESS_NETWORK_STATE" ,
            "android.permission.INTERNET","android.permission.WRITE_EXTERNAL_STORAGE" ,
            "android.permission.READ_PHONE_STATE","android.permission.READ_EXTERNAL_STORAGE"};
    private static final int REQUEST_CODE_PERMISSION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try{
            /*if(ActivityCompat.checkSelfPermission(this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED ||

                    ActivityCompat.checkSelfPermission(this, mPermission[2]) != MockPackageManager.PERMISSION_GRANTED ||

                    ActivityCompat.checkSelfPermission(this, mPermission[3]) != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4]) != MockPackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSION);
            }*/

            if(ActivityCompat.checkSelfPermission(this, mPermission[0]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1]) != PackageManager.PERMISSION_GRANTED ||

                    ActivityCompat.checkSelfPermission(this, mPermission[2]) != PackageManager.PERMISSION_GRANTED ||

                    ActivityCompat.checkSelfPermission(this, mPermission[3]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSION);
            }else{




                proceedNext();
            }
        }catch (Exception e){
            Log.e("SplOnCreate",e.toString());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults.length == mPermission.length){
                proceedNext();
            }
        }
    }
    public  void proceedNext(){
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }
}
