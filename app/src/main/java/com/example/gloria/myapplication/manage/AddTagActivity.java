package com.example.gloria.myapplication.manage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.Tag;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AddTagActivity extends AppCompatActivity {

    EditText tag_name_edit;
    Button add_tag_button;
    Spinner spinner;
    List<Tag> tagList;
    List<String> data_list;
    ArrayAdapter<String> arr_adapter;
    RequestQueue mQueue;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        init();

        getUser();

        listenEditor();
    }

    private void listenEditor() {
        tag_name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchTag(s.toString());
            }
        });
    }

    private void searchTag(String s) {
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("name", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到标签可选列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetTagListByLike";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Tag temp = gson.fromJson(jsonObject.toString(), Tag.class);
                tagList = temp.getTagList();

                // 设置选择列表
                showTagList();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void showTagList() {
        //数据
        data_list = new ArrayList<String>();
        for(Tag t:tagList){
            data_list.add(t.getName());
        }

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        // 监听
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                tag_name_edit.setText(tagList.get(arg2).getId());
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void getUser() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    private void init() {
        tag_name_edit = (EditText) findViewById(R.id.tag_name_edit);
        add_tag_button = (Button) findViewById(R.id.add_tag_button);
        spinner = (Spinner) findViewById(R.id.spinner);
        mQueue = Volley.newRequestQueue(this);
    }
}
