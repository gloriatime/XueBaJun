package com.example.gloria.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class TempActivity extends AppCompatActivity {
    RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        mQueue = Volley.newRequestQueue(TempActivity.this);
        testGetDocument();
    }

    private void testGetDocument() {
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetDocument";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",26);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Document tempuser = gson.fromJson(jsonObject.toString(), Document.class);
                // 签到成功，更新user的积分值并修改UI显示
                if(tempuser != null) {
                    Log.e("##getSuccess##", "Id"+tempuser.getName()+ "\n");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }
}
