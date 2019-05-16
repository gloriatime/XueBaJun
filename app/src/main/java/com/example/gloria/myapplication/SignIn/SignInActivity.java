package com.example.gloria.myapplication.SignIn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.MainActivity;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

public class SignInActivity extends AppCompatActivity {
    private CheckBox cbRemember;//定义记住密码
    private EditText phone, passwd;
    private TextView log_in, sign_up;
    private String sphone, spasswd;
    private SharedPreferences mSpSettings=null;//声明一个sharedPreferences用于保存数据
    private static final String PREPS_NAME="NamePwd";
    String TAG = "SignInActivity";
    User user;

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phone = (EditText) findViewById(R.id.editText2);
        passwd = (EditText) findViewById(R.id.editText4);
        log_in = (TextView) findViewById(R.id.button);
        sign_up = (TextView) findViewById(R.id.button2);
        cbRemember=(CheckBox) findViewById(R.id.cbRemember);
        cbRemember.setChecked(true);
        passwd.setTransformationMethod(new PasswordTransformationMethod());

        user = new User();
        mQueue = Volley.newRequestQueue(SignInActivity.this);

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText().length() < 0) {
                    Toast.makeText(SignInActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.getText().length() < 0) {
                    Toast.makeText(SignInActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String url = "http://47.100.226.176:8080/XueBaJun/SignUp";

                final org.json.JSONObject jsonObject = new org.json.JSONObject();
                try {
                    jsonObject.put("phone", phone.getText().toString());
                    jsonObject.put("pwd", passwd.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("####",""+jsonObject.toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new DateGson().getGson();
                        user = gson.fromJson(response.toString(), User.class);
                        Log.e("##", "name" + user.getName());
                        Log.e("##", "pwd" + user.getPwd());
                        Log.e("##", "COLLEGE" + user.getCollege());
                        if (user != null) {
                            if (cbRemember.isChecked()) {
                                mSpSettings = getSharedPreferences(PREPS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor edit = mSpSettings.edit();//得到Editor对象
                                edit.putBoolean("isKeep", true);//记录保存标记
                                edit.putString("username", phone.getText().toString());//记录用户名
                                edit.putString("password", passwd.getText().toString());//记录密码
                                edit.apply();//**提交

                            } else {
                                mSpSettings = getSharedPreferences(PREPS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor edit = mSpSettings.edit();
                                edit.putBoolean("isKeep", true);//保存的文件名isKeep
                                edit.putString("username", "");
                                edit.putString("password", "");
                                edit.apply();
                            }
                            //***********t跳转到主页************
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("##", "error" + error);
                        Toast.makeText(SignInActivity.this, "输入有误，请检查并重新输入", Toast.LENGTH_SHORT).show();
                        Log.e("##", "COLLEGE" + user.getCollege());
                    }
                });
                mQueue.add(jsonObjectRequest);

            }
        });
        getData();
        sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignInActivity.this, SignUp1Activity.class);
                startActivity(intent);
            }
        });
}
    @Override
   protected void onResume() {
       super.onResume();
       getData();//在界面显示数据之前得到之前存储的数据
 }
        private void getData() {
        mSpSettings=getSharedPreferences(PREPS_NAME, MODE_PRIVATE);
        if(mSpSettings.getBoolean("isKeep", false)){
          //如果之前存储过,则显示在相应文本框内
        phone.setText(mSpSettings.getString("username", ""));
        passwd.setText(mSpSettings.getString("password", ""));
         }else{//否则显示为空
        phone.setText("");
        passwd.setText("");
      }
 }
}

