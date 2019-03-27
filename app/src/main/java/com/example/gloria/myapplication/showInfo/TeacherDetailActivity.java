package com.example.gloria.myapplication.showInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

public class TeacherDetailActivity extends AppCompatActivity {
    private Professor professor;
    private TextView peopleintro;
    private TextView researchin;
    private Button courseone;
    private Button coursetwo;
    private Button coursethree;
    private Button coursefour;
    private Button coursefive;
    private Button coursesix;
    private Button score;
    private RequestQueue mQueue;
    private ImageView professorimage;
    private ImageView notexist;
    User user;
    int id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_detail);
        getPassInfo();
        init();
        getProfessor();

    }
    private void getPassInfo() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        id = intent.getIntExtra("professor_id",0);
        //id = 6;
        Log.e("##", "教师详情id："+id);
    }
    private void SetButton() {
        List<ProfessorCourse> courseList = professor.getProfessorCourseList();
        if (courseList != null) {
            int tnum = courseList.size();
            if (tnum == 6) {
                courseone.setVisibility(Button.VISIBLE);
                courseone.setText(courseList.get(0).getCourse().getName());
                coursetwo.setVisibility(Button.VISIBLE);
                coursetwo.setText(courseList.get(1).getCourse().getName());
                coursethree.setVisibility(Button.VISIBLE);
                coursethree.setText(courseList.get(2).getCourse().getName());
                coursefour.setVisibility(Button.VISIBLE);
                coursefour.setText(courseList.get(3).getCourse().getName());
                coursefive.setVisibility(Button.VISIBLE);
                coursefive.setText(courseList.get(4).getCourse().getName());
                coursesix.setVisibility(Button.VISIBLE);
                coursesix.setText(courseList.get(5).getCourse().getName());
            } else if (tnum == 5) {
                courseone.setVisibility(Button.VISIBLE);
                courseone.setText(courseList.get(0).getCourse().getName());
                coursetwo.setVisibility(Button.VISIBLE);
                coursetwo.setText(courseList.get(1).getCourse().getName());
                coursethree.setVisibility(Button.VISIBLE);
                coursethree.setText(courseList.get(2).getCourse().getName());
                coursefour.setVisibility(Button.VISIBLE);
                coursefour.setText(courseList.get(3).getCourse().getName());
                coursefive.setVisibility(Button.VISIBLE);
                coursefive.setText(courseList.get(4).getCourse().getName());
                coursesix.setVisibility(Button.GONE);
            } else if (tnum == 4) {
                courseone.setVisibility(Button.VISIBLE);
                courseone.setText(courseList.get(0).getCourse().getName());
                coursetwo.setVisibility(Button.VISIBLE);
                coursetwo.setText(courseList.get(1).getCourse().getName());
                coursethree.setVisibility(Button.VISIBLE);
                coursethree.setText(courseList.get(2).getCourse().getName());
                coursefour.setVisibility(Button.VISIBLE);
                coursefour.setText(courseList.get(3).getCourse().getName());
                coursefive.setVisibility(Button.GONE);
                coursesix.setVisibility(Button.GONE);
            } else if (tnum == 3) {
                courseone.setVisibility(Button.VISIBLE);
                courseone.setText(courseList.get(0).getCourse().getName());
                coursetwo.setVisibility(Button.VISIBLE);
                coursetwo.setText(courseList.get(1).getCourse().getName());
                coursethree.setVisibility(Button.VISIBLE);
                coursethree.setText(courseList.get(2).getCourse().getName());
                coursefour.setVisibility(Button.GONE);
                coursefive.setVisibility(Button.GONE);
                coursesix.setVisibility(Button.GONE);
            } else if (tnum == 2) {
                courseone.setVisibility(Button.VISIBLE);
                courseone.setText(courseList.get(0).getCourse().getName());
                coursetwo.setVisibility(Button.VISIBLE);
                coursetwo.setText(courseList.get(1).getCourse().getName());
                coursethree.setVisibility(Button.GONE);
                coursefour.setVisibility(Button.GONE);
                coursefive.setVisibility(Button.GONE);
                coursesix.setVisibility(Button.GONE);
            } else if (tnum == 1) {
                courseone.setVisibility(Button.VISIBLE);
                courseone.setText(courseList.get(0).getCourse().getName());
                coursetwo.setVisibility(Button.GONE);
                coursethree.setVisibility(Button.GONE);
                coursefour.setVisibility(Button.GONE);
                coursefive.setVisibility(Button.GONE);
                coursesix.setVisibility(Button.GONE);
            }
        }
        else{
                courseone.setVisibility(Button.GONE);
                coursetwo.setVisibility(Button.GONE);
                coursethree.setVisibility(Button.GONE);
                coursefour.setVisibility(Button.GONE);
                coursefive.setVisibility(Button.GONE);
                coursesix.setVisibility(Button.GONE);
        }
    }
    private void getProfessor(){

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetProfessor";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                professor = new Gson().fromJson(jsonObject.toString(), Professor.class);
                Log.e("##", "教师详情："+jsonObject.toString());
                sec_init();
                SetButton();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }
    private void sec_init() {
       // if(professor.getIntro()==null)
        peopleintro.setText("暂无介绍");
      //  else
            //peopleintro.setText(professor.getIntro());
       // if(professor.getField()!=null)
     //   researchin.setText(professor.getField());
       // else
            researchin.setText("信息尚未完善，敬请期待");
        score.setOnClickListener(new gscore());
        courseone.setOnClickListener(new courseone());
        coursetwo.setOnClickListener(new coursetwo());
        coursethree.setOnClickListener(new coursethree());
        coursefour.setOnClickListener(new coursefour());
        coursefive.setOnClickListener(new coursefive());
        coursesix.setOnClickListener(new coursesix());
        //if(professor.getPic()==null)
        professorimage.setBackgroundResource(R.drawable.bookimgsample);
       // else
       // SetProfessorImg();
    }
    @SuppressLint("ResourceType")
    private void  init(){
        notexist = (ImageView)findViewById(R.drawable.bookimgsample) ;
        professorimage = (ImageView) findViewById(R.id.teacherimage);
        courseone = (Button)findViewById(R.id.courseone);
        coursetwo = (Button)findViewById(R.id.coursetwo);
        coursethree = (Button)findViewById(R.id.coursethree);
        coursefour = (Button)findViewById(R.id.coursefour);
        coursefive = (Button)findViewById(R.id.coursefive) ;
        coursesix = (Button)findViewById(R.id.coursesix);
        peopleintro = (TextView)findViewById(R.id.peopleintro);
        researchin = (TextView)findViewById(R.id.researchin);
        score = (Button)findViewById(R.id.score);
        mQueue = Volley.newRequestQueue(TeacherDetailActivity.this);
    }
    class gscore implements OnClickListener
    {
        @Override
        public void onClick(View V) {
            Intent intent = new Intent(TeacherDetailActivity.this,activity_T_pingfen.class);
            intent.putExtra("professor",  professor.getId());
            Log.e("##", "这是评分的呢的");
            startActivity(intent);
        }
    }

    class courseone implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(TeacherDetailActivity.this,CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> courseList = professor.getProfessorCourseList();
            bundle.putString("courseone",courseList.get(0).getCourse().getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    class coursetwo implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(TeacherDetailActivity.this,CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> courseList = professor.getProfessorCourseList();
            bundle.putString("coursetwo",courseList.get(1).getCourse().getName());
            startActivity(intent);
        }
    }
    class coursethree implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(TeacherDetailActivity.this,CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> courseList = professor.getProfessorCourseList();
            bundle.putString("coursethree",courseList.get(2).getCourse().getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    class coursefour implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(TeacherDetailActivity.this,CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> courseList = professor.getProfessorCourseList();
            bundle.putString("coursefour",courseList.get(3).getCourse().getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    class coursefive implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(TeacherDetailActivity.this,CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> courseList = professor.getProfessorCourseList();
            bundle.putString("coursefive",courseList.get(4).getCourse().getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    class coursesix implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(TeacherDetailActivity.this,CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> courseList = professor.getProfessorCourseList();
            bundle.putString("coursesix",courseList.get(5).getCourse().getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private ImageView  SetProfessorImg(){
        // 请求教师对应头像，如果没有，就使用默认图片
        if(professor.getPic()==null)
            return notexist;
        ImageRequest imageRequest = new ImageRequest(
                "http://47.100.226.176:8080/XueBaJun/teacher_image/"+professor.getPic()+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        professorimage.setBackground(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                professorimage.setBackgroundResource(R.drawable.bookimgsample);
            }
        });
        mQueue.add(imageRequest);
        return null;
    }
}
