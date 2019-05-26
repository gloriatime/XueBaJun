package com.example.gloria.myapplication.SignIn;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.MainActivity;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SignUp5Activity extends AppCompatActivity {

    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    TextView t6;
    TextView t7;
    Button button;
    private TextListener1 textListener1;
    private TextListener2 textListener2;
    private TextListener3 textListener3;
    private TextListener4 textListener4;
    private TextListener5 textListener5;
    private TextListener6 textListener6;
    private TextListener7 textListener7;

    boolean []flag = new boolean[7];

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up5);

        t1 = (TextView) findViewById(R.id.textView1);
        textListener1 = new TextListener1();
        t1.setOnClickListener(textListener1);

        t2 = (TextView) findViewById(R.id.textView2);
        textListener2 = new TextListener2();
        t2.setOnClickListener(textListener2);

        t3 = (TextView) findViewById(R.id.textView3);
        textListener3 = new TextListener3();
        t3.setOnClickListener(textListener3);

        t4 = (TextView) findViewById(R.id.textView4);
        textListener4 = new TextListener4();
        t4.setOnClickListener(textListener4);

        t5 = (TextView) findViewById(R.id.textView5);
        textListener5 = new TextListener5();
        t5.setOnClickListener(textListener5);

        t6 = (TextView) findViewById(R.id.textView6);
        textListener6 = new TextListener6();
        t6.setOnClickListener(textListener6);

        t7 = (TextView) findViewById(R.id.textView7);
        textListener7 = new TextListener7();
        t7.setOnClickListener(textListener7);

        Intent gintent = getIntent();
        final String mail = gintent.getStringExtra("mail");
        final String passwd = gintent.getStringExtra("passwd");
        final String name = gintent.getStringExtra("name");
        final String univers = gintent.getStringExtra("university");
        final String major = gintent.getStringExtra("major");
        final String grade = gintent.getStringExtra("grade");

        user.setName(name);
        user.setPwd(passwd);
        user.setPhone(mail);
        user.setCollege(univers);
        user.setGrade(grade);
        user.setHead(major);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 与服务器交互
                RequestQueue mQueue = Volley.newRequestQueue(SignUp5Activity.this);
                String url = "http://47.100.226.176:8080/XueBaJun/SignIn";
                //发送数据
                org.json.JSONObject jsonObject = new org.json.JSONObject();

                try {
                    jsonObject.put("phone", user.getPhone());
                    jsonObject.put("name", user.getName());
                    jsonObject.put("pwd", user.getPwd());
                    jsonObject.put("grade", user.getGrade());
                    jsonObject.put("college", user.getCollege());
                    //jsonObject.put("major", user.getHead());
                    jsonObject.put("medicine" ,String.valueOf(user.isMedicine()));
                    jsonObject.put("technology" ,String.valueOf(user.isTechnology()));
                    jsonObject.put("art" ,String.valueOf(user.isArt()));
                    jsonObject.put("agriculture" ,String.valueOf(user.isAgriculture()));
                    jsonObject.put("management" ,String.valueOf(user.isManagement()));
                    jsonObject.put("humanity" ,String.valueOf(user.isHumanity()));
                    jsonObject.put("play" ,String.valueOf(user.isPlay()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("##","发送 "+jsonObject.toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new DateGson().getGson();
                        User ur = gson.fromJson(jsonObject.toString(), User.class);
                        Log.e("##","phone zhuce"+ur.getPhone());
                        if(ur != null){
                            Intent intent = new Intent(SignUp5Activity.this, MainActivity.class);
                            intent.putExtra("user", (Serializable) user);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(SignUp5Activity.this,"注册失败", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
                mQueue.add(jsonObjectRequest);

            }
        });
    }

    class TextListener1 implements View.OnClickListener
    {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            Log.e("##flag[0]",flag[0]+"");
            if(!flag[0]) {
                t1.setTextColor(Color.rgb(0,0,0));
                t1.setBackground(getResources().getDrawable(R.drawable.textview_border_changed));
                flag[0] = true;
            }
            else{
                t1.setBackground(getResources().getDrawable(R.drawable.textview_border1));
                t1.setTextColor(Color.rgb(51,255,231));
                flag[0] = false;
            }
            user.setArt(flag[0]);
        }
    }

    class TextListener2 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(!flag[1]) {
                t2.setTextColor(Color.rgb(0,0,0));
                t2.setBackground(getResources().getDrawable(R.drawable.textview_border_changed));
                flag[1] = true;
            }
            else{
                t2.setBackground(getResources().getDrawable(R.drawable.textview_border2));
                t2.setTextColor(Color.rgb(255,51,78));
                flag[1] = false;
            }
            user.setMedicine(flag[1]);
        }
    }

    class TextListener3 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(!flag[2]) {
                t3.setTextColor(Color.rgb(0,0,0));
                t3.setBackground(getResources().getDrawable(R.drawable.textview_border_changed));
                flag[2] = true;
            }
            else{
                t3.setBackground(getResources().getDrawable(R.drawable.textview_border3));
                t3.setTextColor(Color.rgb(255,218,51));
                flag[2] = false;
            }
            user.setManagement(flag[2]);
        }
    }

    class TextListener4 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(!flag[3]) {
                t4.setTextColor(Color.rgb(0,0,0));
                t4.setBackground(getResources().getDrawable(R.drawable.textview_border_changed));
                flag[3] = true;
            }
            else{
                t4.setBackground(getResources().getDrawable(R.drawable.textview_border4));
                t4.setTextColor(Color.rgb(146,255,51));
                flag[3] = false;
            }
            user.setHumanity(flag[3]);
        }
    }

    class TextListener5 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(!flag[4]) {
                t5.setTextColor(Color.rgb(0,0,0));
                t5.setBackground(getResources().getDrawable(R.drawable.textview_border_changed));
                flag[4] = true;
            }
            else{
                t5.setBackground(getResources().getDrawable(R.drawable.textview_border5));
                t5.setTextColor(Color.rgb(252,51,255));
                flag[4] = false;
            }
            user.setTechnology(flag[4]);
        }
    }

    class TextListener6 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(!flag[5]) {
                t6.setTextColor(Color.rgb(0,0,0));
                t6.setBackground(getResources().getDrawable(R.drawable.textview_border_changed));
                flag[5] = true;
            }
            else{
                t6.setBackground(getResources().getDrawable(R.drawable.textview_border6));
                t6.setTextColor(Color.rgb(255,136,51));
                flag[5] = false;
            }
            user.setAgriculture(flag[5]);
        }
    }

    class TextListener7 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(!flag[6]) {
                t7.setTextColor(Color.rgb(0,0,0));
                t7.setBackground(getResources().getDrawable(R.drawable.textview_border_changed));
                flag[6] = true;
            }
            else{
                t7.setBackground(getResources().getDrawable(R.drawable.textview_border7));
                t7.setTextColor(Color.rgb(51,54,255));
                flag[6] = false;
            }
            user.setPlay(flag[6]);
        }
    }
}

