package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;

import java.io.Serializable;
import java.util.List;

public class TeacherDetailActivity extends AppCompatActivity {
    private Professor professor;
    private ImageView professorimage;
    private RequestQueue mQueue;
    private TextView peopleintro;
    private TextView researchin;
    private Button courseone;
    private Button coursetwo;
    private Button coursethree;
    private Button coursefour;
    private Button score;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_detail);
        getProfessor();
        SetProfessorImg();
        init();
        SetButtonAndText();
    }
    private void  init(){
        professorimage = (ImageView)findViewById(R.id.teacherimage)   ;
     courseone = (Button)findViewById(R.id.courseone);
     coursetwo = (Button)findViewById(R.id.coursetwo);
     coursethree = (Button)findViewById(R.id.coursethree);
     coursefour = (Button)findViewById(R.id.coursefour);
     peopleintro = (TextView)findViewById(R.id.peopleintro);
     peopleintro.setText(professor.getIntro());
     researchin = (TextView)findViewById(R.id.researchin);
     researchin.setText(professor.getField());
     score = (Button)findViewById(R.id.score);
     score.setOnClickListener(new gscore());
    }
    class gscore implements OnClickListener
    {
        @Override
        public void onClick(View V) {
            Intent intent = new Intent(TeacherDetailActivity.this,activity_T_pingfen.class);
            intent.putExtra("professor", (Serializable) professor);
            startActivity(intent);
        }
    }
    private void SetButtonAndText() {
        List<ProfessorCourse> courseList = professor.getProfessorCourseList();
        int tnum = courseList.size();
        if(tnum>=4)
        {
         courseone.setVisibility(Button.VISIBLE);
         coursetwo.setVisibility(Button.VISIBLE);
         coursethree.setVisibility(Button.VISIBLE);
         coursefour.setVisibility(Button.VISIBLE);
        }
        else if(tnum == 3)
        {
            courseone.setVisibility(Button.VISIBLE);
            coursetwo.setVisibility(Button.VISIBLE);
            coursethree.setVisibility(Button.VISIBLE);
            coursefour.setVisibility(Button.GONE);
        }
        else if(tnum ==2)
        {
            courseone.setVisibility(Button.VISIBLE);
            coursetwo.setVisibility(Button.VISIBLE);
            coursethree.setVisibility(Button.GONE);
            coursefour.setVisibility(Button.GONE);
        }
        else if(tnum == 1)
        {
            courseone.setVisibility(Button.VISIBLE);
            coursetwo.setVisibility(Button.GONE);
            coursethree.setVisibility(Button.GONE);
            coursefour.setVisibility(Button.GONE);
        }
        else if(tnum==0)
        {
            courseone.setVisibility(Button.GONE);
            coursetwo.setVisibility(Button.GONE);
            coursethree.setVisibility(Button.GONE);
            coursefour.setVisibility(Button.GONE);
        }
    }

    private void getProfessor(){
        Intent intent = getIntent();
        professor = (Professor) intent.getSerializableExtra("Professor");
    }
    private void   SetProfessorImg(){
        // 请求教师对应头像，如果没有，就使用默认图片
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
    }
    class courseone implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(TeacherDetailActivity.this,CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            List<ProfessorCourse> courseList = professor.getProfessorCourseList();
            ProfessorCourse courseone = courseList.get(0);
            Course one = courseone.getCourse();
            bundle.putString("courseone",one.getName());
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
            ProfessorCourse coursetwo = courseList.get(1);
            Course two = coursetwo.getCourse();
            bundle.putString("coursetwo",two.getName());
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
            ProfessorCourse coursethree = courseList.get(2);
            Course three = coursethree.getCourse();
            bundle.putString("coursethree",three.getName());
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
            ProfessorCourse coursefour = courseList.get(3);
            Course four = coursefour.getCourse();
            bundle.putString("coursefour",four.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
