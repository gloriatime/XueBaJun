package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.BackJump;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class activity_T_pingfen extends AppCompatActivity {
    Button btn;
    EditText editText;
    User user;
    Course course;
    // Course course;
    //private Course course;
    RequestQueue mQueue;
    int idc;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingfen_detail);
        setBackJump();
        getPassInfo();//professor
        //getCourse();//course
        btn = (Button)findViewById(R.id.okbtn);
        editText = (EditText)findViewById(R.id.editText) ;
        btn.setOnClickListener(new btnok());
        mQueue  = Volley.newRequestQueue(activity_T_pingfen.this);
    }
    private void getPassInfo() {
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        course = (Course) intent.getSerializableExtra("course");
        idc =  course.getId();
        Log.e("##", "评分详情id："+idc);
    }
    class btnok implements OnClickListener{
        @Override
        public void onClick(View v) {
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
            String score = editText.getText().toString();
            if(course != null) {
                int sum = course.getNumber()+1;
                float sc = (course.getScore()*course.getNumber()+  Float.valueOf(score))/sum;
                BigDecimal bg = new BigDecimal(sc);
                float f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                /****
                 * 提交分数
                 */
                Log.e("##","最后计算的分数"+sc);
                String url = "http://47.100.226.176:8080/XueBaJun/ScoreCourse";
                org.json.JSONObject jsonObject = new org.json.JSONObject();
                try {
                    jsonObject.put("score", sc);
                    jsonObject.put("id", course.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("##", "score document_id" + jsonObject);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new DateGson().getGson();
                        Course d = gson.fromJson(jsonObject.toString(), Course.class);
                        Log.e("##", "上传数据库之后的分数" + d.getScore() + " " + d.getId());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                mQueue.add(jsonObjectRequest);
                showDialog();
            }
        }
    }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.happy);
        builder.setTitle("温馨提示");
        builder.setMessage("您的分数已经提交！");
        builder.setPositiveButton("我知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction("action.refreshCourse");
                        sendBroadcast(intent);
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

    ImageButton back_button;
    public void setBackJump(){
        back_button= (ImageButton) findViewById(R.id.back_button);
        BackJump bj = new BackJump();
        bj.setBack(back_button);
    }

}
