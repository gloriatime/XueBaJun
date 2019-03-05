package com.example.gloria.myapplication.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.ListItemViewHolder;
import com.example.gloria.myapplication.R;
import com.example.gloria.okhttp3helper.http.OkHttpUtil;
import com.example.gloria.okhttp3helper.http.ProgressListener;
import com.example.model.myapplication.CollectDocument;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import com.leon.lfilepickerlibrary.LFilePicker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class UploadFileActivity extends AppCompatActivity {

    ListView list;

    Button upload_button;
    TextView document_button_1,document_button_2,document_button_3,document_button_4;// 上传进度也通过此控件显示
    String path;// 需要上传的文档路径
    int item;// 需要更改显示的item，在显示选择文件时用到
    File[] fileList;
    String[] filesNewName;// 存储文档新名称，方便添加数据库的资料表url字段
    Document[] documents; // 与fileList相对应的document列表
    DocumentAdapter mAdapter;// 本页面list的适配器
    RequestQueue mQueue;
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
        setList();
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
            setList();
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
        list= (ListView) findViewById(R.id.list);
        mQueue = Volley.newRequestQueue(this);
        fileList = new File[4];// 对应UI中文档1-4
        documents = new Document[4];
        filesNewName = new String[4];
        path = null;
    }

    private void setClickFun(){

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
                    documents[i].setUrl("document/"+file.getName());
                    Log.e("##","设置document的url为："+"document/"+file.getName());

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
                            content = "完成啦";
                            handler.sendMessage(msg);
                            // 清理相应的数据
                            fileList[i] = null;
                            filesNewName[i] = null;
                            //documents[i] = null;
                            // 发送对应的信息到数据库
                            sendInfoToServer(i);
                        }
                    }
                }
            }
        }, file);

    }

    private void sendInfoToServer(final int i) {
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("name", documents[i].getName());
            jsonObject.put("author",documents[i].getAuthor());
            jsonObject.put("url",documents[i].getUrl());
            jsonObject.put("up_user",user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/AddDocument";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                Log.e("##", "document插入数据库已返回");
                // 返回后清除数据
                documents[i] = null;
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
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
        fileList[item] = temp;
        Document d = new Document();
        d.setName(temp.getName());
        d.setAuthor("佚名");
        documents[item] = d;
    }

    // 选择好文件后，刷新item的UI
    private void flushUI() {
        setList();
    }

    private void setList() {
        mAdapter = new DocumentAdapter(this);//得到一个自定义的ListAdapter对象
        list.setAdapter(mAdapter);

        /*为ListView添加点击事件*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("##", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                // 弹出文件选择器
                item = arg2;// 标记被点击的条目
                showFilePicker();
            }
        });
    }

    // 准备展示的数据
    private ArrayList<HashMap<String, Object>> getData(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
        //Log.e("##", "此时User列表为" );//在LogCat中输出信息

        //Log.e("##", "此时doc列表为" +fileList.size());//在LogCat中输出信息
        // 4个文件
        for (int i = 0; i < 4; i++) {

            HashMap<String, Object> map = new HashMap<>();
            if(fileList[i]!=null){
                map.put("name", documents[i].getName());
                map.put("path", fileList[i].getPath());
                map.put("author",documents[i].getAuthor());
                if(documents[i].getAuthor()==null){
                    map.put("author","佚名");
                }
            }else{
                map.put("author","暂无");
                map.put("name", "暂无");
                map.put("path", "点击选择文件");
            }

            listItem.add(map);
        }
        return listItem;
    }

    private void showChangeDialog(final int position) {
        final EditText newName = new EditText(this);
        final EditText newAuthor = new EditText(this);
        newName.setHint("请输入资料名");
        newAuthor.setHint("请输入作者名");
        newName.setText(documents[position].getName());
        String author = documents[position].getAuthor();
        if(author ==null){
            newAuthor.setText("佚名");
        }else {
            newAuthor.setText(author);
        }

        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入新的资料名")
                   .setView(newName);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       // 更改列表的值
                        String name = newName.getText().toString();
                        //String author = newAuthor.getText().toString();
                        if(name!=null){
                            documents[position].setName(name);
                        }
                        // 显示修改作者的对话框
                        showChangeAuthorDialog(position);
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        inputDialog.show();
    }

    private void showChangeAuthorDialog(final int position) {

        final EditText newName = new EditText(this);
        final EditText newAuthor = new EditText(this);
        newName.setHint("请输入资料名");
        newAuthor.setHint("请输入作者名");
        newName.setText(documents[position].getName());
        String author = documents[position].getAuthor();
        if(author ==null){
            newAuthor.setText("佚名");
        }else {
            newAuthor.setText(author);
        }

        AlertDialog.Builder inputDialog2 =
                new AlertDialog.Builder(this);
        inputDialog2.setTitle("请输入新的作者名")
                .setView(newAuthor);
        inputDialog2.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 更改列表的值
                        //String name = newName.getText().toString();
                        String author = newAuthor.getText().toString();
                        if(author!=null){
                            documents[position].setAuthor(author);
                        }

                        // 刷新list
                        setList();
                    }
                });
        inputDialog2.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        inputDialog2.show();
    }

    // 为上传列表list准备自定义的adapter
    private class DocumentAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        DocumentAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 4;//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ListItemViewHolder holder;

            Log.e("##", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_upload_document_2,null);
                holder = new ListItemViewHolder();

                holder.name = (TextView) convertView.findViewById(R.id.document_name_text);
                holder.author = (TextView) convertView.findViewById(R.id.document_author_text);
                holder.path = (TextView) convertView.findViewById(R.id.document_path_text);
                holder.button = (Button)  convertView.findViewById(R.id.change_document_info_button);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.button.setVisibility(View.INVISIBLE);
            holder.name.setText("资料名："+getData().get(position).get("name").toString());
            holder.author.setText("作者："+getData().get(position).get("author").toString());
            holder.path.setText("文件路径："+getData().get(position).get("path").toString());
            // 没有文件不显示修改信息按钮
            if(getData().get(position).get("path").toString().compareTo("点击选择文件")!=0){
                holder.button.setVisibility(View.VISIBLE);
            }


            // 为Button添加点击事件
            holder.button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.e("##", "你点击了按钮" + position);
                    // 弹出修改信息的对话框
                    showChangeDialog(position);
                }
            });

            return convertView;
        }

    }

}
