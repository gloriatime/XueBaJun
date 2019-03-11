package com.example.gloria.myapplication.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.MyJsonArrayRequest;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.TempActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {

    ListView list;
    User user;
    String url,type,search_content;
    List<Document> document_list;
    List<Course> course_list;
    List<Book> book_list;
    List<String> str;
    RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        init();
        getExtra();
        getData();
    }

    private void init() {
        list = (ListView) findViewById(R.id.search_result_list);
        user = new User();
        url = "";
        type = "";
        search_content = "";
        str = new ArrayList<>();
        mQueue = Volley.newRequestQueue(SearchResultActivity.this);
    }

    private void getExtra() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        type = (String) intent.getSerializableExtra("type");
        search_content = (String) intent.getSerializableExtra("search_content");
        Log.e("##", "搜索"+type+"  "+search_content);
    }

    private void getData() {

        // 通过名字的模糊查询
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",search_content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(type.compareTo("课程") == 0){

            url = "http://47.100.226.176:8080/XueBaJun/SearchCourse";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {

                    Gson gson = new DateGson().getGson();

                    Course temp = gson.fromJson(jsonObject.toString(), Course.class);
                    Log.e("##", jsonObject.toString());
                    course_list = temp.getCourseList();
                    // 更改str的值
                    str.clear();
                    for(Course c:course_list){
                        str.add(c.getName());
                    }
                    setList();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(jsonObjectRequest);

        }else if(type.compareTo("资料") == 0){

            url = "http://47.100.226.176:8080/XueBaJun/SearchDocument";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {

                    Gson gson = new DateGson().getGson();

                    Document temp = gson.fromJson(jsonObject.toString(), Document.class);
                    Log.e("##", jsonObject.toString());
                    document_list = temp.getDocumentList();
                    // 更改str的值
                    str.clear();
                    for(Document d:document_list){
                        str.add(d.getName());
                    }
                    setList();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(jsonObjectRequest);

        }else{
            url = "http://47.100.226.176:8080/XueBaJun/SearchBook";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {

                    Gson gson = new DateGson().getGson();

                    Book temp = gson.fromJson(jsonObject.toString(), Book.class);
                    Log.e("##", jsonObject.toString());
                    book_list = temp.getBookList();
                    // 更改str的值
                    str.clear();
                    for(Book b:book_list){
                        str.add(b.getName());
                    }
                    setList();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(jsonObjectRequest);
        }

    }


    private void setList() {
        if(str.isEmpty()){
            Toast.makeText(getApplicationContext(), "什么都没找到╥﹏╥...", Toast.LENGTH_SHORT).show();

        }else {
            list.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, str));
        }
    }

}
