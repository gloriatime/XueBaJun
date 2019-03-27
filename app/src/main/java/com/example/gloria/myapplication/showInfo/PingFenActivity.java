package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.manage.MyCollectActivity;
import com.example.model.myapplication.CollectDocument;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.List;

public class PingFenActivity extends AppCompatActivity {
    Button btn;
    EditText editText;
    //private Course course;
    RequestQueue mQueue;
    int id;
    int idc;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingfen_detail);
        getPassInfo();
        btn = (Button)findViewById(R.id.okbtn);
        editText = (EditText)findViewById(R.id.editText) ;
        btn.setOnClickListener(new btnok());
        Intent intent = getIntent();
        idc = (int) intent.getIntExtra("course",0);
        mQueue  = Volley.newRequestQueue(PingFenActivity.this);
    }
    private void getPassInfo() {
        Intent intent = getIntent();
        Log.e("##", "课程id：");
        id = intent.getIntExtra("pingfen_id",0);
        //id = 6;
        Log.e("##", "评分详情id："+id);
    }
    class btnok implements OnClickListener{
        @Override
        public void onClick(View v) {
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
        String score = editText.toString();
       // course.changeScore(score);
        // 与服务器交互

            org.json.JSONObject jsonObject = new org.json.JSONObject();
            try {
                jsonObject.put("score", score);
                jsonObject.put("Id", idc);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 与服务器交互得到我收藏的资料列表
            String url = "http://47.100.226.176:8080/XueBaJun/Score";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {

                    Gson gson = new DateGson().getGson();

                    Course temp = gson.fromJson(jsonObject.toString(), Course.class);

                    temp.getScore();
                }

            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(jsonObjectRequest);

        }
    }

}
