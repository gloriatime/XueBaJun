package com.example.gloria.myapplication.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gloria.myapplication.R;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.util.List;


public class UploadFileActivity extends AppCompatActivity {

    Button upload_button;
    TextView document_button_1,document_button_2,document_button_3,document_button_4;
    String path;// 需要上传的文档路径
    TextView text;// 需要更改显示的text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        init();
        setClickFun();
    }

    private void init() {
        document_button_1 = (TextView) findViewById(R.id.document_button_1);
        document_button_2 = (TextView) findViewById(R.id.document_button_2);
        document_button_3 = (TextView) findViewById(R.id.document_button_3);
        document_button_4 = (TextView) findViewById(R.id.document_button_4);
        upload_button = (Button) findViewById(R.id.uploaded_button);
        path = null;
    }

    private void setClickFun(){

        // 处理文档选择
        document_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = document_button_3;
                showFilePicker();
            }
        });
        document_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = document_button_1;
                showFilePicker();
            }
        });
        document_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = document_button_2;
                showFilePicker();
            }
        });
        document_button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = document_button_4;
                showFilePicker();
            }
        });
    }


    private void showFilePicker() {
        int REQUESTCODE_FROM_ACTIVITY = 1000;
        new LFilePicker()
                .withActivity(UploadFileActivity.this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withStartPath("/sdcard/")
                .withIsGreater(false)
                .withFileSize(1024 * 1024 * 1024)
                .withTitle("选择要上传的文件")
                .withMutilyMode(false)
                .start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int REQUESTCODE_FROM_ACTIVITY = 1000;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {

                List<String> list = data.getStringArrayListExtra("paths");
                path = list.get(0);
                Toast.makeText(getApplicationContext(), "The selected path is:" + path, Toast.LENGTH_SHORT).show();
                flushUI();
            }
        }
    }

    private void flushUI() {
        text.setText(path);
    }
}
