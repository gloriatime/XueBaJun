package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.model.myapplication.Professor;
import com.google.gson.Gson;

import org.json.JSONException;

public class activity_T_pingfen extends Activity{
    Button btn;
    EditText editText;
    private Professor teacher;
    RequestQueue mQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingfen_detail);
        btn = (Button)findViewById(R.id.okbtn);
        editText = (EditText)findViewById(R.id.editText) ;
        btn.setOnClickListener(new btnok());
        Intent intent = getIntent();
        teacher = (Professor) intent.getSerializableExtra("Professor");
        mQueue  = Volley.newRequestQueue(activity_T_pingfen.this);
    }
    class btnok implements OnClickListener{
        @Override
        public void onClick(View v) {
            String score = editText.toString();
            // 与服务器交互

            org.json.JSONObject jsonObject = new org.json.JSONObject();
            try {
                jsonObject.put("score", score);
                jsonObject.put("Id", teacher.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 与服务器交互得到我收藏的资料列表
            String url = "http://47.100.226.176:8080/XueBaJun/Score";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {

                    Gson gson = new DateGson().getGson();

                    Professor temp = gson.fromJson(jsonObject.toString(), Professor.class);

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
