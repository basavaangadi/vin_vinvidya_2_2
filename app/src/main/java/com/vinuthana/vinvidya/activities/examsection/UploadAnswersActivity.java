package com.vinuthana.vinvidya.activities.examsection;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadAnswersActivity extends AppCompatActivity {

ImageView imageView;
EditText pageNumber;
Button btnChooseImage,btnUploadImage;
String strPageNumber= "",strImageBase64;
String strSchoolId="",strExamId="",strExamName="",strAcademicYearRange="",strClass="",
        strClassId="",strSubjectName="",strquestion_paper_attachments_id="",strStudentId="";
    public static final int CAMERA_PERM_CODE = 101;
    private final  int IMG_REQUEST = 1;

    int item_position;
    final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_answers);
        imageView = findViewById(R.id.imageView);
        pageNumber = findViewById(R.id.pageNumber);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);



        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        strSchoolId = bundle.getString("SchoolId");
        strStudentId = bundle.getString("StudentId");
        strClass = bundle.getString("Class");
        strExamId = bundle.getString("ExamId");
        strExamName = bundle.getString("ExamName");
        strAcademicYearRange = bundle.getString("AcademicYearRange");
        strClassId = bundle.getString("ClassId");
        strSubjectName = bundle.getString("SubjectName");
        strquestion_paper_attachments_id = bundle.getString("question_paper_attachments_id");



        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPageNumber = pageNumber.getText().toString();
                strImageBase64 = imageToString(bitmap);
                new UploadImage().execute();

            }
        });


    }

    private  void selectImage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadAnswersActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                item_position = item;

                if (options[item].equals("Take Photo"))
                {
                    checkCameraPermissions();

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent,"select picture"),1);
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
                pageNumber.setVisibility(View.VISIBLE);

            }else if (options[item_position].equals("Choose from Gallery")) {
               /* Uri path = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    imageView.setImageBitmap(bitmap);

                    imageView.setVisibility(View.VISIBLE);
                    pageNumber.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                List <Bitmap> bitmaps = new ArrayList<>();
                ClipData clipData = null;
                    clipData = data.getClipData();

                if(clipData!= null){
                    for(int i=0;i<clipData.getItemCount();i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        Uri path = clipData.getItemAt(i).getUri();
                        try {
                          //  InputStream is = getContentResolver().openInputStream(imageUri);
                       //Bitmap bitmap= BitmapFactory.decodeStream(is);
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                            bitmaps.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }else {
                    Uri path = data.getData();
                    try (
                            InputStream is = getContentResolver().openInputStream(path)) {
                   bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                  //   for(Bitmap bitmap:bitmaps){
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 imageView.setImageBitmap(bitmap);
                                 imageView.setVisibility(View.VISIBLE);
                                 pageNumber.setVisibility(View.VISIBLE);

                             }
                         });
                         try {
                             Thread.sleep(3000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
                    }).start();




            }
        }

    }

    class UploadImage extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String url = "http://192.168.43.155:8080/netvinvidyawebapi/operation/examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UploadAnswersActivity.this);
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
                examData.put("page_no", strPageNumber);
                examData.put("SchoolId", strSchoolId);
                examData.put("Class", strClass);
                examData.put("StudentId", strStudentId);
                //examData.put("ExamId", strExamId);
                examData.put("ExamName", strExamName);
                examData.put("AcademicYearRange", strAcademicYearRange);
                examData.put("SubjectName", strSubjectName);
                examData.put("question_paper_attachments_id", strquestion_paper_attachments_id);
                examData.put("ImageBase64", strImageBase64);
                outObject.put("examData", examData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    UploadAnswersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(UploadAnswersActivity.this, "Data not found", Toast.LENGTH_LONG);
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
