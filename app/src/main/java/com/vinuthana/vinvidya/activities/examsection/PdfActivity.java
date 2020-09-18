package com.vinuthana.vinvidya.activities.examsection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.vinuthana.vinvidya.R;

public class PdfActivity extends AppCompatActivity {
String strquestion_pdf_link;
    WebView webView;
    Button btnUploadAnswerSheets;
    String strSchoolId="",strStudentId="",strClass="",strExamId="",
            strExamName="",strAcademicYearRange="",strClassId="",strSubjectName="",
            strquestion_paper_attachments_id=""  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        btnUploadAnswerSheets = findViewById(R.id.btnUploadAnswerSheets);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        strquestion_pdf_link = bundle.getString("question_pdf_link");
        strSchoolId= bundle.getString("SchoolId");
        strStudentId= bundle.getString("StudentId");
        strClass= bundle.getString("Class");
        strExamId= bundle.getString("ExamId");
        strExamName= bundle.getString("ExamName");
        strAcademicYearRange=bundle.getString("AcademicYearRange");
        strClassId=  bundle.getString("ClassId");
        strSubjectName=bundle.getString("SubjectName");
        strquestion_paper_attachments_id= bundle.getString("question_paper_attachments_id");


        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());

        String finalUrl="http://drive.google.com/viewerng/viewer?embedded=true&url="+strquestion_pdf_link;
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(finalUrl);

        btnUploadAnswerSheets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadAnswersActivity.class);
                Bundle bundle= new Bundle();
                bundle.putString("SchoolId",strSchoolId);
                bundle.putString("StudentId",strStudentId);
                bundle.putString("Class",strClass);
                bundle.putString("ExamId",strExamId);
                bundle.putString("ExamName",strExamName);
                bundle.putString("AcademicYearRange",strAcademicYearRange);
                bundle.putString("ClassId",strClassId);
                bundle.putString("SubjectName",strSubjectName);
                bundle.putString("question_paper_attachments_id",strquestion_paper_attachments_id);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });


    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String strquestion_pdf_link) {
            view.loadUrl(strquestion_pdf_link);
            return true;
        }
    }

    }

