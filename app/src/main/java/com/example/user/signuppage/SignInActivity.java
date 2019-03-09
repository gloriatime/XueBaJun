package com.example.user.signuppage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class SignInActivity extends AppCompatActivity {

    private EditText phone, passwd;
    private Button log_in, sign_up;
    private String sphone, spasswd;
    String TAG = "SignInActivity";

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phone = (EditText)findViewById(R.id.editText2);
        passwd = (EditText)findViewById(R.id.editText4);
        log_in = (Button)findViewById(R.id.button);
        sign_up = (Button)findViewById(R.id.button2);

        mQueue = Volley.newRequestQueue(SignInActivity.this);

        log_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (phone.getText().length()<0){
                    Toast.makeText(SignInActivity.this,"用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.getText().length()<0){
                    Toast.makeText(SignInActivity.this,"密码不能为空", Toast.LENGTH_SHORT).show();
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

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new DateGson().getGson();
                        User user = gson.fromJson(jsonObject.toString(), User.class);
                        if(user != null ){
                            /**************
                             * ***********t跳转到主页************
                             * ******************/
                        }
                    }
                },  new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignInActivity.this,"用户不存在，请先注册", Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignInActivity.this, SignUp1Activity.class);
                startActivity(intent);
            }
        });

    }
}

