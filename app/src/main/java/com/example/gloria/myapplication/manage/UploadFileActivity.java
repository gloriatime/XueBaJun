package com.example.gloria.myapplication.manage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gloria.myapplication.R;
import com.example.gloria.okhttp3helper.http.OkHttpUtil;
import com.example.gloria.okhttp3helper.http.ProgressListener;
import com.example.model.myapplication.User;
import com.leon.lfilepickerlibrary.LFilePicker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class UploadFileActivity extends AppCompatActivity {

    Button upload_button;
    TextView document_button_1,document_button_2,document_button_3,document_button_4;// 上传进度也通过此控件显示
    String path;// 需要上传的文档路径
    TextView text;// 需要更改显示的text，在显示选择文件时用到
    File[] fileList;
    String[] filesNewName;// 存储文档新名称，方便添加数据库的资料表url字段
    User user;

    // 标记第几个文件上传成功已返回
    private static final int NO1_COMPLETED = 1;
    private static final int NO2_COMPLETED = 2;
    private static final int NO3_COMPLETED = 3;
    private static final int NO4_COMPLETED = 4;
    private static String content = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        init();
        getUser();
        setClickFun();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NO1_COMPLETED:{
                    document_button_1.setText(content);
                    break;}
                case NO2_COMPLETED:{
                    document_button_2.setText(content);
                    break;}
                case NO3_COMPLETED:{
                    document_button_3.setText(content);
                    break;}
                case NO4_COMPLETED:{
                    document_button_4.setText(content);
                    break;}
            }
            content = "";
        }
    };

    private void getUser() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    private void init() {
        document_button_1 = (TextView) findViewById(R.id.document_button_1);
        document_button_2 = (TextView) findViewById(R.id.document_button_2);
        document_button_3 = (TextView) findViewById(R.id.document_button_3);
        document_button_4 = (TextView) findViewById(R.id.document_button_4);
        upload_button = (Button) findViewById(R.id.up_button);
        fileList = new File[4];// 对应UI中文档1-4
        filesNewName = new String[4];
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

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFiles();
            }
        });
    }

    private void uploadFiles() {
        String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        try {

            for(int i = 0;i<4;i++){
                if(fileList[i]!=null){
                    // 先将文档重命名放置在本地
                    String name = fileList[i].getName();
                    Log.e("##","filename=="+name);
                    String newFileName = RandomStringUtils.randomAlphanumeric(10);
                    String[] genre = name.split("\\.");

                    File file;
                    if(name.contains(".")){
                        // 文件有后缀名
                        file = new File(basePath+"/XueBaJun/document/"+user.getPhone()+"/"+newFileName+"."+genre[genre.length-1]);
                    }else{
                        file = new File(basePath+"/XueBaJun/document/"+user.getPhone()+"/"+newFileName);
                    }

                    // 如果之前有同名文件就删掉
                    if(file.exists()){
                        file.delete();
                    }
                    if(!file.exists()){
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    FileUtils.copyFile(fileList[i],file);
                    filesNewName[i] = file.getName();

                    // 不同文档位置使用不同的进度条监听器
                    ProgressListener pl_1 = new ProgressListener() {
                        @Override
                        public void onProgress(long currentBytes, long contentLength, boolean done) {
                            int progress = (int) (currentBytes * 100 / contentLength);
                            document_button_1.setText(progress + "%");
                        }
                    };
                    ProgressListener pl_2 = new ProgressListener() {
                        @Override
                        public void onProgress(long currentBytes, long contentLength, boolean done) {
                            int progress = (int) (currentBytes * 100 / contentLength);
                            document_button_2.setText(progress + "%");
                        }
                    };
                    ProgressListener pl_3 = new ProgressListener() {
                        @Override
                        public void onProgress(long currentBytes, long contentLength, boolean done) {
                            int progress = (int) (currentBytes * 100 / contentLength);
                            document_button_3.setText(progress + "%");
                        }
                    };
                    ProgressListener pl_4 = new ProgressListener() {
                        @Override
                        public void onProgress(long currentBytes, long contentLength, boolean done) {
                            int progress = (int) (currentBytes * 100 / contentLength);
                            document_button_4.setText(progress + "%");
                        }
                    };

                    Message msg = new Message();
                    // 在ui部分显示正在上传
                    switch (i){
                        case 0:{
                            msg.what = NO1_COMPLETED;
                            uploadSingleFile(pl_1,msg,file);
                            break;}
                        case 1:{
                            msg.what = NO2_COMPLETED;
                            uploadSingleFile(pl_2,msg,file);
                            break;}
                        case 2:{
                            msg.what = NO3_COMPLETED;
                            uploadSingleFile(pl_3,msg,file);
                            break;}
                        case 3:{
                            msg.what = NO4_COMPLETED;
                            uploadSingleFile(pl_4,msg,file);
                            break;}
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 上传完成后及时清理不用的数据
    private void uploadSingleFile( ProgressListener pl, final Message msg, File file){
        // 将文档发到后台储存
        String postUrl = "http://47.100.226.176:8080/XueBaJun/FileServlet";
        OkHttpUtil.postFile(postUrl, pl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    Log.e("##", "result===" + result);
                    // 返回后和filesNewName对比，查看返回的是那个
                    String[] temp = result.split("/");
                    String name = temp[temp.length-1];
                    for(int i = 0;i<4;i++){
                        if(filesNewName[i]!=null && name.compareTo(filesNewName[i]) == 0){
                            // 给主线程发送信息更新UI
                            content = "完成";
                            handler.sendMessage(msg);
                            // 清理相应的数据
                            fileList[i] = null;
                            filesNewName[i] = null;
                        }
                    }
                }
            }
        }, file);
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
                // 将文档放入文档列表
                setFileList();
                // 刷新UI显示
                flushUI();
                // 更新列表后path刷新否则会影响列表其他节点
                path = null;
            }
        }
    }

    private void setFileList() {
        File temp = new File(path);
        Log.e("##","目前text是"+text.getId()+"  from  "+ R.id.document_button_1+" "+ R.id.document_button_2+" "+ R.id.document_button_3+ " "+R.id.document_button_4);
        switch (text.getId()){
            case R.id.document_button_1: {
                fileList[0] = temp;
                break;
            }
            case R.id.document_button_2:{
                fileList[1] = temp;
                break;
            }
            case R.id.document_button_3:{
                fileList[2] = temp;
                break;
            }
            case R.id.document_button_4:{
                fileList[3] = temp;
                break;
            }
        }
       // Log.e("##","目前fileList是"+fileList[0].getName()+ " "+fileList[1].getName()+ " "+fileList[2].getName()+ " "+fileList[3].getName());

    }

    // 选择好文件后，显示需要上传的文件路径
    private void flushUI() {
        text.setText(path);
    }

}
