package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Teacher;

import java.io.Serializable;
import java.util.List;

public class CourseDetailActivity extends Activity{

    private Course course;
    private TextView course_name_text;
    private TextView course_wcollege;
    private TextView course_prefermajor;
    private TextView course_prefersemester;
    private TextView course_ctime;
    private TextView course_examiningmode;
    private TextView setscore;
    private TextView textbbook;
    private TextView introducebook;
    private Button teacher_one;
    private Button teacher_two;
    private Button teacher_three;
    private Button givescore;
    private RequestQueue mQueue;
    private ImageView bookimage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.course_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.course_title);
        getCourse();
        init();
        SetButton();
    }

    private void SetButton() {
        List<Teacher> teacherList = course.getTeacherList();
        int tnum = teacherList.size();
        Teacher teachero, teachert, teacherth;
        if(tnum>=3)
        {
            teacher_one.setVisibility(Button.VISIBLE);
            teachero = teacherList.get(0);
            teacher_one.setText(teachero.getTname());
            teacher_two.setVisibility(Button.VISIBLE);
            teachert = teacherList.get(1);
            teacher_two.setText(teachert.getTname());
            teacher_three.setVisibility(Button.VISIBLE);
            teacherth = teacherList.get(2);
            teacher_three.setText(teacherth.getTname());
        }
        else if(tnum==2)
        {
            teacher_one.setVisibility(Button.VISIBLE);
            teachero = teacherList.get(0);
            teacher_one.setText(teachero.getTname());
            teacher_two.setVisibility(Button.VISIBLE);
            teachert = teacherList.get(1);
            teacher_two.setText(teachert.getTname());
            teacher_three.setVisibility(Button.INVISIBLE);
        }
        else if(tnum==1)
        {
            teacher_one.setVisibility(Button.VISIBLE);
            teachero = teacherList.get(0);
            teacher_one.setText(teachero.getTname());
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
        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("course");
    }
    private void init() {
        course_name_text = (TextView)findViewById(R.id.course_name_text);
        course_name_text.setText(course.getName());
        course_wcollege = (TextView)findViewById(R.id.course_wcollege);
        course_wcollege .setText(course.getWcollege());
        course_prefermajor = (TextView)findViewById(R.id.course_prefermajor);
        course_prefermajor.setText(course.getprefermajor());
        course_prefersemester = (TextView)findViewById(R.id.course_prefersemester);
        course_prefersemester.setText(course.getTerm());
        course_ctime = (TextView)findViewById(R.id.course_ctime);
        course_ctime.setText(course.getCtime());
        course_examiningmode = (TextView)findViewById(R.id.course_examiningmode);
        course_examiningmode.setText(course.getExaminingmode());
        setscore = (TextView)findViewById(R.id.setscore);
        setscore.setText(course.getScore());
        textbbook = (TextView)findViewById(R.id.textbbook);
        Book book = course.getTextbbook();
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
            List<Teacher> teacherList = course.getTeacherList();
            Teacher teacherone = teacherList.get(0);
            bundle.putString("teacherone",teacherone.getTname());
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
            List<Teacher> teacherList = course.getTeacherList();
            Teacher teachertwo = teacherList.get(1);
            bundle.putString("teachertwo",teachertwo.getTname());
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
            List<Teacher> teacherList = course.getTeacherList();
            Teacher teacherthree = teacherList.get(2);
            bundle.putString("teacherthree",teacherthree.getTname());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private void SetBookImg(){
        // 请求图书对应头像，如果没有，就使用默认图片
        Book book = course.getTextbbook();
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
