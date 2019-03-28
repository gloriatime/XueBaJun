package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.ListItemViewHolderCAndB;
import com.example.base.myapplication.NetImage;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.Recommend.Reco_course;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_Top20_course extends AppCompatActivity {
    private ListView list_re_C;
    List<String> str; // 存放listView数据
    //以上为列表内容
    private Course course;
    RequestQueue mQueue;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top20);

        init();
        
        getUser();

        getCourse();


    }

    private void getUser(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    private void init() {
        list_re_C = (ListView)findViewById(R.id.ListView_like);
        str = new ArrayList<>();
        mQueue = Volley.newRequestQueue(Activity_Top20_course.this);
    }
    private void getCourse() {
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetTopTwentyCourse";

        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Course tempuser = gson.fromJson(jsonObject.toString(), Course.class);

                course = tempuser;//从后台请求第一个course成功

                for(Course c:course.getTopTwentyList()){
                    str.add(c.getName());
                }

                setListView();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void setListView()//设置列表的点击事件
    {
        list_re_C.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, str));
        /*为lsit添加点击事件*/
        list_re_C.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("##", "你点击了ListView条目" + i);//在LogCat中输出信息
                // ---显示点击之后的页面
                Intent intent = new Intent(Activity_Top20_course.this,CourseDetailActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("course_id",course.getTopTwentyList().get(i).getId());
                startActivity(intent);
            }
        });


    }

}