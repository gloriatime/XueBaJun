package com.example.gloria.myapplication.manage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.asm.Type;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.ListItemViewHolder;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.CollectDocument;
import com.example.model.myapplication.Comment;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.News;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.leon.lfilepickerlibrary.utils.Constant;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutMeActivity extends AppCompatActivity {

    private Button uploaded_button,comment_button,news_button;
    private ListView list;
    private ArrayList<Document> uploaded = new ArrayList<>();
    private ArrayList<Comment> comments= new ArrayList<>();;
    private ArrayList<News> news= new ArrayList<>();;
    private static final int UPLOADED=1,COMMENT=2,NEWS=3;
    RequestQueue mQueue;
    DocumentAdapter mAdapter;// 本页面list的适配器
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        init();
        getUser();
        setClickFun();
        // 默认显示上传的资料列表
        uploaded_button.performClick();
    }

    private void init() {
        uploaded_button = (Button) findViewById(R.id.uploaded_button);
        comment_button = (Button) findViewById(R.id.comment_button);
        news_button= (Button) findViewById(R.id.news_button);
        list= (ListView) findViewById(R.id.list);
        mQueue  = Volley.newRequestQueue(AboutMeActivity.this);
    }

    private void getUser(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    private void setClickFun(){
        uploaded_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonUI(uploaded_button,comment_button,news_button);
                updateUploaded();
                setList(UPLOADED);
            }
        });

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonUI(comment_button,uploaded_button,news_button);
                updateComment();
                setList(COMMENT);
            }
        });

       news_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonUI(news_button,uploaded_button,comment_button);
                updateNews();
                setList(NEWS);
            }
        });
    }

    private void setList(int listContent) {
        mAdapter = new DocumentAdapter(this,listContent);//得到一个自定义的ListAdapter对象
        // list.setAdapter(mAdapter);

        /*为ListView添加点击事件*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("##", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                // ---------------------------跳转到对应的信息展示界面，目前先空着--------------------------------------
            }
        });
    }

    private void updateNews() {
        // 清理原来的值
        news.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyNews";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);
                Log.e("##", jsonObject.toString());
                if (tempuser != null) {
                    Log.e("##", "我的消息已返回");
                    List<News> MyNews = tempuser.getMyNews();

                    for (News n : MyNews) {
                        news.add(n);
                        Log.e("##", "new's content" + n.getContent()+ "Id: "+n.getId());
                    }
                    // 先执行内部类，后执行外部类
                    // setAdapter需要放在数据刷新成功之后。
                    list.setAdapter(mAdapter);//为ListView绑定Adapter
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void updateComment() {

        // 清理原来的值
        comments.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyComment";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                // Creates the json object which will manage the information received
                GsonBuilder builder = new GsonBuilder();

                // Register an adapter to manage the date types as long values
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();

                User tempuser = gson.fromJson(jsonObject.toString(), User.class);
                Log.e("##", jsonObject.toString());
                if (tempuser != null) {
                    Log.e("##", "我的评论已返回");
                    List<Comment> MyComment = tempuser.getMyComment();

                    for (Comment c : MyComment) {
                        comments.add(c);
                        //Log.e("##", "Comment's content: " + c.getContent()+ "  Id: "+c.getId()+"   time: "+c.getDocument().getUp_time());
                    }
                    // 先执行内部类，后执行外部类
                    // setAdapter需要放在数据刷新成功之后。
                    list.setAdapter(mAdapter);//为ListView绑定Adapter
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);

    }

    private void updateUploaded() {

        // 清理原来的值
        uploaded.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyDocument";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {

                Gson gson = new DateGson().getGson();

                User tempuser = gson.fromJson(jsonObject.toString(), User.class);
                Log.e("##", jsonObject.toString());
                if (tempuser != null) {
                    Log.e("##", "我上传的资料已返回");
                    List<Document> MyDocuments = tempuser.getMyDocument();

                    for (Document d : MyDocuments) {
                        uploaded.add(d);
                        Log.e("##", "Document's name" + d.getName()+ "Id: "+d.getId());
                    }
                    // 先执行内部类，后执行外部类
                    // setAdapter需要放在数据刷新成功之后。
                    list.setAdapter(mAdapter);//为ListView绑定Adapter
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);

    }

    private void changeButtonUI(Button be_clicked_button,Button normal_button_1,Button normal_button_2){
        be_clicked_button.setTextColor(getResources().getColor(R.color.white));
        be_clicked_button.setBackgroundColor(getResources().getColor(R.color.red));
        be_clicked_button.setEnabled(false);

        normal_button_1.setTextColor(getResources().getColor(R.color.red));
        normal_button_1.setBackground(getResources().getDrawable(R.drawable.bg_stroke));
        normal_button_1.setEnabled(true);

        normal_button_2.setTextColor(getResources().getColor(R.color.red));
        normal_button_2.setBackground(getResources().getDrawable(R.drawable.bg_stroke));
        normal_button_2.setEnabled(true);
    }

    // 准备展示的数据
    private ArrayList<HashMap<String, Object>> getData(int listContent){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        /* 关注的人，关注我的人，最多100人， */

        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
        //Log.e("##", "此时User列表为" );//在LogCat中输出信息

        if(listContent == UPLOADED){
            Log.e("##", "此时doc列表为" +uploaded.size());//在LogCat中输出信息
            for (int i = 0; i < uploaded.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id",uploaded.get(i).getId());
                map.put("name", uploaded.get(i).getName());
                listItem.add(map);

            }
        }else if(listContent == COMMENT){
            Log.e("##", "此时book列表为" +comments.size());//在LogCat中输出信息
            for (int i = 0; i < comments.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Id",comments.get(i).getId());
                map.put("content", comments.get(i).getContent());
               // Log.e("##", "Comment's content: " + comments.get(i).getContent()+ "  Id: "+comments.get(i).getId()+"   time: "+comments.get(i).getDocument().getUp_time());

                // 从comment的type值得到此条评论的评论对象
                if(comments.get(i).getDocument()!=null){
                    map.put("name",comments.get(i).getDocument().getName());
                    map.put("head",R.drawable.icon_paper);
                }else if(comments.get(i).getBook()!=null){
                    map.put("name",comments.get(i).getBook().getName());
                    map.put("head",R.drawable.icon_book);
                }else if(comments.get(i).getCourse()!=null){
                    map.put("name",comments.get(i).getCourse().getName());
                    map.put("head",R.drawable.icon_course);
                }

                listItem.add(map);

            }
        }else if(listContent == NEWS){
            Log.e("##", "此时course列表为"+news.size() );//在LogCat中输出信息
            for (int i = 0; i < news.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Id",news.get(i).getId());
                map.put("name", news.get(i).getContent());
                listItem.add(map);

            }
        }
        return listItem;
    }


    private void showDeleteNewsAlterDialog(int position) {
        // 消息直接删除
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("id",news.get(position).getId());
            // jsonObject.put("url",uploaded.get(position).getUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/DeleteNews";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
        // 返回重新加载数据
        updateNews();
        setList(NEWS);
    }

    private void showChangeCommentDialog(final int position) {

        // 弹出包含修改删除两种选项的对话框
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("对这条评论")//设置对话框的标题
                .setPositiveButton("直接删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        org.json.JSONObject jsonObject = new org.json.JSONObject();
                        try {
                            jsonObject.put("id",comments.get(position).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 与服务器交互得到我收藏的资料列表
                        String url = "http://47.100.226.176:8080/XueBaJun/DeleteComment";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

                            public void onResponse(org.json.JSONObject jsonObject) {
                                //Log.e("##", "document删除已返回");
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                        mQueue.add(jsonObjectRequest);
                        // 返回重新加载数据
                        updateUploaded();
                        setList(COMMENT);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("修改评论内容", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 弹出输入对话框
                        showInputDialog(position);
                    }
                })
                .create();
        dialog.show();

    }

    private void showInputDialog(final int position) {
        final EditText editText = new EditText(this);
        editText.setText(comments.get(position).getContent());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入修改后的评论内容")
                .setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        org.json.JSONObject jsonObject = new org.json.JSONObject();
                        try {
                            jsonObject.put("id",comments.get(position).getId());
                            jsonObject.put("content",editText.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 与服务器交互得到我收藏的资料列表
                        String url = "http://47.100.226.176:8080/XueBaJun/ChangeComment";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

                            public void onResponse(org.json.JSONObject jsonObject) {
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                        mQueue.add(jsonObjectRequest);
                        // 返回重新加载数据
                        updateUploaded();
                        setList(COMMENT);
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

    private void showDeleteDocumentAlterDialog(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("确认要删除"+getData(UPLOADED).get(position).get("name").toString()+"么？")//设置对话框的标题
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        org.json.JSONObject jsonObject = new org.json.JSONObject();
                        try {
                            jsonObject.put("id",uploaded.get(position).getId());
                           // jsonObject.put("url",uploaded.get(position).getUrl());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 与服务器交互得到我收藏的资料列表
                        String url = "http://47.100.226.176:8080/XueBaJun/DeleteDocument";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

                            public void onResponse(org.json.JSONObject jsonObject) {
                                //Log.e("##", "document删除已返回");
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                        mQueue.add(jsonObjectRequest);
                        // 返回重新加载数据
                        updateUploaded();
                        //setList(UPLOADED);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    // 点击List中的按钮会弹出的对话框
    private void showAlertDialog(final int position, final int listContent){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("确认要删除"+getData(listContent).get(position).get("name").toString()+"么？")//设置对话框的标题
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "";
                        org.json.JSONObject jsonObject = null;

                        // 根据listContent变换内容
                        if(listContent == UPLOADED){
                            url = "http://47.100.226.176:8080/XueBaJun/DeleteCollectedDocument";
                            HashMap<String,String> u = new HashMap<>();
                            HashMap<String,String> d = new HashMap<>();
                            u.put("phone",user.getPhone());
                            d.put("id", String.valueOf(uploaded.get(position).getId()));
                            Log.e("##","发送id "+String.valueOf(uploaded.get(position).getId())+uploaded.get(position).getName());
                            Map<String, Object> map = new HashMap<>();
                            map.put("user",u);
                            map.put("document",d);

                            jsonObject = new org.json.JSONObject(map);
                            Log.e("##","发送 "+jsonObject.toString());

                        }else if(listContent == COMMENT){
                            url = "http://47.100.226.176:8080/XueBaJun/DeleteCollectedCOMMENT";
                            HashMap<String,String> u = new HashMap<>();
                            HashMap<String,String> d = new HashMap<>();
                            u.put("phone",user.getPhone());
                            d.put("id", String.valueOf(comments.get(position).getId()));
                            Map<String, Object> map = new HashMap<>();
                            map.put("user",u);
                            map.put("book",d);
                            jsonObject = new org.json.JSONObject(map);
                        }else if(listContent == NEWS){
                            url = "http://47.100.226.176:8080/XueBaJun/DeleteCollectedCourse";
                            HashMap<String,String> u = new HashMap<>();
                            HashMap<String,String> d = new HashMap<>();
                            u.put("phone",user.getPhone());
                            d.put("id", String.valueOf(news.get(position).getId()));
                            Map<String, Object> map = new HashMap<>();
                            map.put("user",u);
                            map.put("course",d);
                            jsonObject = new org.json.JSONObject(map);
                        }

                        // ---------------------------与数据库交互取消收藏-------------------------

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                            public void onResponse(org.json.JSONObject jsonObject) {
                                Log.e("##","删除执行完毕");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.e("##","突然的404闪了腰"+volleyError.toString());
                                byte[] htmlBodyBytes = volleyError.networkResponse.data;
                                Log.e("VolleyError body---->", new String(htmlBodyBytes), volleyError);
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                        // 更新显示的列表
                        if(listContent == UPLOADED){
                            updateUploaded();
                        }else if(listContent == COMMENT){
                            updateComment();
                        }else if(listContent == NEWS){
                            updateNews();
                        }
                        setList(listContent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
    // 为我的关注list准备自定义的adapter
    private class DocumentAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int listContent;


        DocumentAdapter(Context context, int listContent) {
            this.mInflater = LayoutInflater.from(context);
            this.listContent = listContent;
        }

        @Override
        public int getCount() {
            return getData(listContent).size();//返回数组的长度
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

                holder = new ListItemViewHolder();

                if(listContent == UPLOADED){
                    convertView = mInflater.inflate(R.layout.list_item_my_documents,null);
                    // 对应资料名称
                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    // 对应删除资料的按钮
                    holder.button = (Button) convertView.findViewById(R.id.button);
                }else if(listContent == NEWS){
                    convertView = mInflater.inflate(R.layout.list_item_my_news,null);
                    // 对应消息内容
                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    // 对应删除消息按钮
                    holder.button = (Button) convertView.findViewById(R.id.button);
                }else if(listContent == COMMENT){
                    convertView = mInflater.inflate(R.layout.list_item_my_comment,null);
                    // 对应被评论资料等的名称
                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    // 对应被评论资料等的显示图标
                    holder.head = (ImageView) convertView.findViewById(R.id.head);
                    // 对应评论内容
                    holder.content = (TextView) convertView.findViewById(R.id.content);
                    // 对应修改评论的按钮
                    holder.button = (Button) convertView.findViewById(R.id.button);

                    // holder 绑定数据
                    holder.content.setText(getData(listContent).get(position).get("content").toString());
                }

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.name.setText(getData(listContent).get(position).get("name").toString());

            // 为Button添加点击事件
            if(holder.button!=null){
                holder.button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.e("##", "你点击了按钮" + position);
                        // 根据listContent的不同分别使用不同的dialog
                        switch (listContent){
                            case UPLOADED:{
                                showDeleteDocumentAlterDialog(position);
                                break;
                            }
                            case COMMENT:{
                                showChangeCommentDialog(position);
                                break;
                            }
                            case NEWS:{
                                showDeleteNewsAlterDialog(position);
                                break;
                            }
                        }
                    }
                });
            }


            return convertView;
        }

    }
}
