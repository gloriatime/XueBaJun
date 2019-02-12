package com.example.gloria.myapplication.manage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.ListItemViewHolder;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.CollectBook;
import com.example.model.myapplication.CollectCourse;
import com.example.model.myapplication.CollectDocument;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCollectActivity extends AppCompatActivity {

    private Button documents_button,books_button,courses_button;
    private ListView list;
    private ArrayList<Document> documents = new ArrayList<>();
    private ArrayList<Book> books= new ArrayList<>();;
    private ArrayList<Course> courses= new ArrayList<>();;
    private int DOCUMENT=1,BOOK=2,COURSE=3;
    RequestQueue mQueue;
    CollectionAdapter mAdapter;// 本页面list的适配器
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        init();
        getUser();
        setClickFun();
        // 默认显示收藏的资料列表
        documents_button.performClick();
    }

    private void init(){
        documents_button = (Button) findViewById(R.id.documents_button);
        books_button  = (Button) findViewById(R.id.books_button);
        courses_button= (Button) findViewById(R.id.courses_button);
        list= (ListView) findViewById(R.id.list);
        mQueue  = Volley.newRequestQueue(MyCollectActivity.this);
    }

    private void getUser(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    private void setClickFun(){
        documents_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonUI(documents_button,books_button,courses_button);
                updateDocuments();
                setList(DOCUMENT);
            }
        });

        books_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonUI(books_button,documents_button,courses_button);
                updateBooks();
                setList(BOOK);
            }
        });

        courses_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonUI(courses_button,documents_button,books_button);
                updateCourses();
                setList(COURSE);
            }
        });
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

    private void setList(int listContent) {
        mAdapter = new CollectionAdapter(this,listContent);//得到一个自定义的ListAdapter对象
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

    private void updateDocuments() {

        // 清理原来的值
        documents.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyCollectedDocuments";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);
                Log.e("##", jsonObject.toString());
                if (tempuser != null) {
                    Log.e("##", "我收藏的资料已返回");
                    List<CollectDocument> MyCollectedDocuments = tempuser.getCollected_documents();

                    for (CollectDocument c : MyCollectedDocuments) {
                        Document d = c.getDocument();
                        documents.add(d);
                        Log.e("##", "Document's name" + d.getName()+ "Id: "+d.getId()+"Author: "+c.getDocument().getAuthor());
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

    private void updateCourses() {

        // 清理原来的值
        courses.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
            //jsonObject.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyCollectedCourses";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);

                if (tempuser != null) {
                    Log.e("##", "我收藏的课程已返回");
                    List<CollectCourse> MyCollectedCourses = tempuser.getCollected_courses();

                    for (CollectCourse c : MyCollectedCourses) {
                        Course d = c.getCourse();
                        courses.add(d);
                        Log.e("##", "Course's name" + d.getName());
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

    private void updateBooks() {

        // 清理原来的值
        books.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
            //jsonObject.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyCollectedBooks";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);

                if (tempuser != null) {
                    Log.e("##", "我收藏的课程已返回");
                    List<CollectBook> MyCollectedBooks = tempuser.getCollected_books();

                    for (CollectBook c : MyCollectedBooks) {
                        Book d = c.getBook();
                        books.add(d);
                        Log.e("##", "Book's name" + d.getName());
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

    // 准备展示的数据
    private ArrayList<HashMap<String, Object>> getData(int listContent){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        /* 关注的人，关注我的人，最多100人， */

        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
        //Log.e("##", "此时User列表为" );//在LogCat中输出信息

        if(listContent == DOCUMENT){
            Log.e("##", "此时doc列表为" +documents.size());//在LogCat中输出信息
            for (int i = 0; i < documents.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id",documents.get(i).getId());
                map.put("name", documents.get(i).getName());
                listItem.add(map);

            }
        }else if(listContent == BOOK){
            Log.e("##", "此时book列表为" +books.size());//在LogCat中输出信息
            for (int i = 0; i < books.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Id",books.get(i).getId());
                map.put("name", books.get(i).getName());
                listItem.add(map);

            }
        }else if(listContent == COURSE){
            Log.e("##", "此时course列表为"+courses.size() );//在LogCat中输出信息
            for (int i = 0; i < courses.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Id",courses.get(i).getId());
                map.put("name", courses.get(i).getName());
                listItem.add(map);

            }
        }
        return listItem;
    }

    // 点击List中的按钮会弹出的对话框
    private void showAlertDialog(final int position, final int listContent){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("确认要取消收藏"+getData(listContent).get(position).get("name").toString()+"么？")//设置对话框的标题
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "";
                        org.json.JSONObject jsonObject = null;

                        // 根据listContent变换内容
                        if(listContent == DOCUMENT){
                            url = "http://47.100.226.176:8080/XueBaJun/DeleteCollectedDocument";
                            HashMap<String,String> u = new HashMap<>();
                            HashMap<String,String> d = new HashMap<>();
                            u.put("phone",user.getPhone());
                            d.put("id", String.valueOf(documents.get(position).getId()));
                            Log.e("##","发送id "+String.valueOf(documents.get(position).getId())+documents.get(position).getName());
                            Map<String, Object> map = new HashMap<>();
                            map.put("user",u);
                            map.put("document",d);

                            jsonObject = new org.json.JSONObject(map);
                            Log.e("##","发送 "+jsonObject.toString());

                        }else if(listContent == BOOK){
                            url = "http://47.100.226.176:8080/XueBaJun/DeleteCollectedBook";
                            HashMap<String,String> u = new HashMap<>();
                            HashMap<String,String> d = new HashMap<>();
                            u.put("phone",user.getPhone());
                            d.put("id", String.valueOf(books.get(position).getId()));
                            Map<String, Object> map = new HashMap<>();
                            map.put("user",u);
                            map.put("book",d);
                            jsonObject = new org.json.JSONObject(map);
                        }else if(listContent == COURSE){
                            url = "http://47.100.226.176:8080/XueBaJun/DeleteCollectedCourse";
                            HashMap<String,String> u = new HashMap<>();
                            HashMap<String,String> d = new HashMap<>();
                            u.put("phone",user.getPhone());
                            d.put("id", String.valueOf(courses.get(position).getId()));
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
                        if(listContent == DOCUMENT){
                           updateDocuments();
                        }else if(listContent == BOOK){
                            updateBooks();
                        }else if(listContent == COURSE){
                           updateCourses();
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
    private class CollectionAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int listContent;


        CollectionAdapter(Context context, int listContent) {
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
                convertView = mInflater.inflate(R.layout.list_item_my_collection,null);
                holder = new ListItemViewHolder();

                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.button = (Button) convertView.findViewById(R.id.button);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.name.setText(getData(listContent).get(position).get("name").toString());

            // 为Button添加点击事件
            holder.button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.e("##", "你点击了按钮" + position);
                    // 弹出确认取消关注的对话框
                    showAlertDialog(position,listContent);
                }
            });

            return convertView;
        }

    }
}
