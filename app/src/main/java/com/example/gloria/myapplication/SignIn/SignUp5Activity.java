package com.example.gloria.myapplication.SignIn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
                    jsonObject.put("major", user.getHead());
                    jsonObject.put("art",user.isArt());
                    jsonObject.put("medicine", user.isMedicine());
                    jsonObject.put("management", user.isManagement());
                    jsonObject.put("humanity", user.isHumanity());
                    jsonObject.put("technology", user.isTechnology());
                    jsonObject.put("agriculture", user.isAgriculture());
                    jsonObject.put("play", user.isPlay());
                    jsonObject.put("point",0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("##","发送 "+jsonObject.toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new DateGson().getGson();
                        User ur = gson.fromJson(jsonObject.toString(), User.class);
                        if(ur != null){
                            Intent intent = new Intent(SignUp5Activity.this, MainActivity.class);
                            intent.putExtra("user",ur);
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
        @Override
        public void onClick(View view) {
            t1.setTextColor(Color.rgb(0,0,0));
            t1.setBackgroundColor(Color.rgb(255,240,245));
            Boolean flag = true;
            user.setArt(flag);
        }
    }

    class TextListener2 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            t2.setTextColor(Color.rgb(0,0,0));
            t2.setBackgroundColor(Color.rgb(255,240,245));
            Boolean flag = true;
            user.setMedicine(flag);
        }
    }

    class TextListener3 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            t3.setTextColor(Color.rgb(0,0,0));
            t3.setBackgroundColor(Color.rgb(255,240,245));
            Boolean flag = true;
            user.setManagement(flag);
        }
    }

    class TextListener4 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            t4.setTextColor(Color.rgb(0,0,0));
            t4.setBackgroundColor(Color.rgb(255,240,245));
            Boolean flag = true;
            user.setHumanity(flag);
        }
    }

    class TextListener5 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            t5.setTextColor(Color.rgb(0,0,0));
            t5.setBackgroundColor(Color.rgb(255,240,245));
            Boolean flag = true;
            user.setTechnology(flag);
        }
    }

    class TextListener6 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            t6.setTextColor(Color.rgb(0,0,0));
            t6.setBackgroundColor(Color.rgb(255,240,245));
            Boolean flag = true;
            user.setAgriculture(flag);
        }
    }

    class TextListener7 implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            t7.setTextColor(Color.rgb(0,0,0));
            t7.setBackgroundColor(Color.rgb(255,240,245));
            Boolean flag = true;
            user.setPlay(flag);
        }
    }
}

