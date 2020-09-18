package com.vinuthana.vinvidya.activities.examsection;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.activities.extraactivities.MainActivity;
import com.vinuthana.vinvidya.adapters.ExamSectionAdpater.SubjectSpinnerDataAdapter;
import com.vinuthana.vinvidya.adapters.SpinnerDataAdapter;
import com.vinuthana.vinvidya.utils.AD;
import com.vinuthana.vinvidya.utils.GetResponse;
import com.vinuthana.vinvidya.utils.StudentSPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ExamImageUploadActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int CAMERA_PERM_CODE = 101;
    private Button btnChooseImage,btnUploadImage;
    TextView tvRollNo,tvClass,tvStudentName;
    Spinner spnrExam,spnrSubject;
    private EditText name;
    private ImageView imageView;
    private final  int IMG_REQUEST = 1;
    final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
    int item_position;
    String strImageName="",strBase64="",strStatus="";
    StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId,strSchoolId,strStudentnName,strClassId,strExamId,strExam,strSubject;

    private Bitmap bitmap;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_image_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Image");


        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        tvClass = findViewById(R.id.tvClass);
        tvRollNo = findViewById(R.id.tvRollNo);
        tvStudentName = findViewById(R.id.tvStudentName);
        spnrSubject = findViewById(R.id.spnrSubject);
        spnrExam = findViewById(R.id.spnrExam);
        name = findViewById(R.id.name);
        imageView = findViewById(R.id.imageView);

        try {
            strStudentId = ExamImageUploadActivity.this.getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        studentSPreference = new StudentSPreference(ExamImageUploadActivity.this);
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);

        String strStudentnName = hashMap.get(StudentSPreference.KEY_STUD_NAME);
        String strRollNo = hashMap.get(studentSPreference.ROLL_NUM);

        String strClass = hashMap.get(StudentSPreference.STR_CLASS);
        strSchoolId = hashMap.get(StudentSPreference.STR_SCHOOLID);
        strClassId =hashMap.get(StudentSPreference.STR_CLASS_ID);


        tvRollNo.setText(strRollNo);
        tvClass.setText(strClass);
        tvStudentName.setText(strStudentnName);

        new GetExams().execute();

       /* spnrExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView =  spnrExam.getSelectedView().findViewById(R.id.tvList);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strExamId);
                //Toast.makeText(getActivity(), strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
                new GetSubject().execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        spnrExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                TextView tmpView = (TextView) spnrExam.getSelectedView().findViewById(R.id.txt_exam);
                tmpView.setTextColor(Color.WHITE);
                strExam = adapterView.getItemAtPosition(position).toString();
                Log.e("Tag", "" + strExam);
                //Toast.makeText(getActivity(), strExam + "You Clicked on ", Toast.LENGTH_SHORT).show();
              new GetSubject().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnChooseImage.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnChooseImage:
                selectImage();
                break;

            case R.id.btnUploadImage:
                strImageName = name.getText().toString();

                strBase64 = imageToString(bitmap);
                new UploadImage().execute();
                break;

        }
    }

    private  void selectImage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamImageUploadActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                item_position = item;

                if (options[item].equals("Take Photo"))
                {
                    checkCameraPermissions();
                    /*Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);*/
                    //startActivityForResult(intent,IMG_REQUEST);

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    /*Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,IMG_REQUEST);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }


        });
        builder.show();

    }


    private void checkCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }else{

            openCamera();
        }
    }


    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,IMG_REQUEST);
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == IMG_REQUEST){

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else {
                Toast toast = Toast.makeText(getApplicationContext(),"Camera Permission is required to use camera",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == Activity.RESULT_OK && data!=null) {

            if (options[item_position].equals("Take Photo")){

                bitmap  = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);

            }else if (options[item_position].equals("Choose from Gallery")) {
                Uri path = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    imageView.setImageBitmap(bitmap);

                    imageView.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String encodeImage(Bitmap selectedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b,Base64.DEFAULT);
        return encImage;
    }
    class GetExams extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "examsectionOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ExamImageUploadActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamName));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_examData), userData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(responseText).getJSONArray("Result"));
                } else {
                    ExamImageUploadActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExamImageUploadActivity.this);
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
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            progressDialog.dismiss();
            SpinnerDataAdapter adapter = new SpinnerDataAdapter(values[0], ExamImageUploadActivity.this);
            spnrExam.setPrompt("Choose Exam");
            spnrExam.setAdapter(adapter);
        }
    }

    class GetSubject extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseSubjects));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], ExamImageUploadActivity.this);
            spnrSubject.setPrompt("Choose Section");
            spnrSubject.setAdapter(adapter);
        }
    }

    class UploadImage extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
         //String url = "http://192.168.43.155:8080/UploadImageDemo/image/imageUpload.jsp";
         String url = "http://192.168.43.155:8080/netvinvidyawebapi/operation/examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ExamImageUploadActivity.this);
            progressDialog.setMessage("please wait while we fetch your Data");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();


            try {
                outObject.put("OperationName", "UploadExamImage");
                JSONObject examData = new JSONObject();
                examData.put("ImageName", strImageName);
                examData.put("Base64", strBase64);
                outObject.put("examData", examData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    ExamImageUploadActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(ExamImageUploadActivity.this, "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is", e.toString());
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
        }

    }

    private  String imageToString (Bitmap bitmap)
    {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        String imageString = android.util.Base64.encodeToString(imgBytes, android.util.Base64.DEFAULT);
        return imageString;

    }


}

