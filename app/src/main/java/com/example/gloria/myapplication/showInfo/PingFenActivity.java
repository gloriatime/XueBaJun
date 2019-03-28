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
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PingFenActivity extends AppCompatActivity {
    public static String action ="jason.broadcast.action";
    Button btn;
    EditText editText;
    User user;
    Professor professor;
   // Course course;
    //private Course course;
    RequestQueue mQueue;
    int idp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingfen_detail);
        getPassInfo();//professor
        //getCourse();//course
        btn = (Button)findViewById(R.id.okbtn);
        editText = (EditText)findViewById(R.id.editText) ;
        btn.setOnClickListener(new btnok());
        mQueue  = Volley.newRequestQueue(PingFenActivity.this);
    }
    private void getPassInfo() {
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        professor = (Professor) intent.getSerializableExtra("professor");
        idp =  professor.getId();
        Log.e("##", "评分详情id："+idp);
    }
    class btnok implements OnClickListener{
        @Override
        public void onClick(View v) {
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
          String score = editText.getText().toString();
            if(professor != null) {
                int sum = professor.getNumber()+1;
                float sc = (professor.getScore()*professor.getNumber()+  Float.valueOf(score))/sum;
                BigDecimal bg = new BigDecimal(sc);
                float f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                /****
                 * 提交分数
                 */
                Log.e("##","最后计算的分数"+sc);
                String url = "http://47.100.226.176:8080/XueBaJun/ScoreProfessor";
                org.json.JSONObject jsonObject = new org.json.JSONObject();
                try {
                    jsonObject.put("score", sc);
                    jsonObject.put("id", professor.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("##", "score document_id" + jsonObject);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new DateGson().getGson();
                        Professor d = gson.fromJson(jsonObject.toString(), Professor.class);
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
                        Intent intent = new Intent(action);
                        //Log.e("##","professor的分数sssssssssssss"+Float.valueOf(professor.getScore()));
                        intent.putExtra("score", Float.valueOf(professor.getScore()));
                        sendBroadcast(intent);
                        //finish();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();

    }


}
/*if(course != null) {
                int sum = course.getNumber()+1;
                float sc = (course.getScore()+  Float.valueOf(score).floatValue())/sum;
                BigDecimal bg = new BigDecimal(sc);
                float f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                String res = "当前评分"+f1+"分";
                //mScore.setText(res);
                /****
                 * 提交分数
                 */
              /*  Log.e("##","最后计算的分数"+sc);
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
                        Intent intent = new Intent(PingFenActivity.this, TeacherDetailActivity.class);
                        intent.putExtra("user", (Serializable) user);
                        intent.putExtra("course", d);
                        startActivity(intent);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                mQueue.add(jsonObjectRequest);
            }*/