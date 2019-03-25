package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.News;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private Course course;
    private TextView course_name_text;
    private TextView course_prefersemester;
    private TextView setscore;
    private TextView textbbook;
    private TextView introducebook;
    private Button teacher_one;
    private Button teacher_two;
    private Button teacher_three;
    private Button givescore;
    private RequestQueue mQueue;
    private ImageView bookimage;
    User user;
    int id; // 详情界面展示的课程Id
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.course_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.course_title);
        getPassInfo();
        getCourse();
        init();
        SetButton();
    }

    private void getPassInfo() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        id = intent.getIntExtra("course_id",0);
    }

    private void SetButton() {
        List<ProfessorCourse> professorCourseList = course.getProfessorCourseList();
        int tnum = professorCourseList.size();
       // Professor teachero, teachert, teacherth;
        if(tnum>=3)
        {
            teacher_one.setVisibility(Button.VISIBLE);
            ProfessorCourse one = professorCourseList.get(0);
            Professor professorO = one.getProfessor();
            teacher_one.setText(professorO.getName());
            teacher_two.setVisibility(Button.VISIBLE);
            ProfessorCourse two = professorCourseList.get(1);
            Professor professorT = two.getProfessor();
            teacher_two.setText(professorT.getName());
            teacher_three.setVisibility(Button.VISIBLE);
            ProfessorCourse three = professorCourseList.get(2);
            Professor professorTh = three.getProfessor();
            teacher_three.setText(professorTh.getName());
        }
        else if(tnum==2)
        {
            teacher_one.setVisibility(Button.VISIBLE);
            ProfessorCourse one = professorCourseList.get(0);
            Professor professorO = one.getProfessor();
            teacher_one.setText(professorO.getName());
            teacher_two.setVisibility(Button.VISIBLE);
            ProfessorCourse two = professorCourseList.get(1);
            Professor professorT = two.getProfessor();
            teacher_two.setText(professorT.getName());
            teacher_three.setVisibility(Button.INVISIBLE);
        }
        else if(tnum==1)
        {
            teacher_one.setVisibility(Button.VISIBLE);
            ProfessorCourse one = professorCourseList.get(0);
            Professor professorO = one.getProfessor();
            teacher_one.setText(professorO.getName());
            teacher_two.setVisibility(Button.INVISIBLE);
            teacher_three.setVisibility(Button.INVISIBLE);
        }
        else
        {
            teacher_one.setVisibility(Button.INVISIBLE);
            teacher_two.setVisibility(Button.INVISIBLE);
            teacher_three.setVisibility(Button.INVISIBLE);
        }

    }

    private void getCourse(){

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("applicant", user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetCoures";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                 course = new Gson().fromJson(jsonObject.toString(), Course.class);
                Log.e("##", jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }
    private void init() {
        course_name_text = (TextView)findViewById(R.id.course_name_text);
        course_name_text.setText(course.getName());
        course_prefersemester = (TextView)findViewById(R.id.course_prefersemester);
        course_prefersemester.setText(course.getTerm());
        //course_ctime = (TextView)findViewById(R.id.course_ctime);
       // course_ctime.setText(course.getCtime());
        setscore = (TextView)findViewById(R.id.setscore);
        setscore.setText(String.valueOf(course.getScore()));
        textbbook = (TextView)findViewById(R.id.textbbook);
        Book book = course.getBook();
        bookimage = (ImageView) findViewById(R.id.bookimage);
        SetBookImg();
        textbbook.setText(book.getName());
        introducebook = (TextView)findViewById(R.id.introducebook);
        introducebook.setText(course.getIntro());
        teacher_one = (Button)findViewById(R.id.teacher_one);
        teacher_two = (Button)findViewById(R.id.teacher_two);
        teacher_three = (Button)findViewById(R.id.teacher_three);
        givescore = (Button)findViewById(R.id.givescore);
        teacher_one.setOnClickListener(new tone());
        teacher_two.setOnClickListener(new ttwo());
        teacher_three.setOnClickListener(new tthree());
        givescore.setOnClickListener(new gscore());
    }
    class gscore implements OnClickListener
    {
        @Override
        public void onClick(View V) {
            Intent intent = new Intent(CourseDetailActivity.this,PingFenActivity.class);
            intent.putExtra("course", (Serializable) course);
            startActivity(intent);
        }
    }
    class tone implements OnClickListener
    {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(CourseDetailActivity.this,TeacherDetailActivity.class);
        Bundle bundle = new Bundle();
            List<ProfessorCourse> teacherList = course.getProfessorCourseList();
            Professor teacherone = teacherList.get(0).getProfessor();
            bundle.putString("teacherone",teacherone.getName());
        intent.putExtras(bundle);
        startActivity(intent);
        }
    }
    class ttwo implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(CourseDetailActivity.this,TeacherDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> teacherList = course.getProfessorCourseList();
            Professor teachertwo = teacherList.get(1).getProfessor();
            bundle.putString("teachertwo",teachertwo.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    class tthree implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(CourseDetailActivity.this,TeacherDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> teacherList = course.getProfessorCourseList();
            Professor teacherthree = teacherList.get(2).getProfessor();
            bundle.putString("teacherthree",teacherthree.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private void SetBookImg(){
        // 请求图书对应头像，如果没有，就使用默认图片
        Book book = course.getBook();
        ImageRequest imageRequest = new ImageRequest(
                "http://47.100.226.176:8080/XueBaJun/book_image/"+book.getCover()+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        bookimage.setBackground(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bookimage.setBackgroundResource(R.drawable.bookimgsample);
            }
        });
        mQueue.add(imageRequest);
    }

}
