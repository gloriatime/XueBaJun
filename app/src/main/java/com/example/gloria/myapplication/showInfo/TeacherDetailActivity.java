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
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Teacher;

import java.util.List;

public class TeacherDetailActivity extends Activity{
    private Teacher teacher;
    private ImageView teacherimage;
    private RequestQueue mQueue;
    private TextView student1;
    private TextView student2;
    private TextView student3;
    private TextView student4;
    private TextView peopleintro;
    private TextView researchin;
    private Button courseone;
    private Button coursetwo;
    private Button coursethree;
    private Button coursefour;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_detail);
        getTeacher();
        SetTeacherImg();
        init();
        SetButtonAndText();
    }
    private void  init(){
     teacherimage = (ImageView)findViewById(R.id.teacherimage)   ;
     student1 = (TextView)findViewById(R.id.student1);
     student2 = (TextView)findViewById(R.id.student2);
     student3 = (TextView)findViewById(R.id.student3);
     student4 = (TextView)findViewById(R.id.student4);
     courseone = (Button)findViewById(R.id.courseone);
     coursetwo = (Button)findViewById(R.id.coursetwo);
     coursethree = (Button)findViewById(R.id.coursethree);
     coursefour = (Button)findViewById(R.id.coursefour);
     peopleintro = (TextView)findViewById(R.id.peopleintro);
     peopleintro.setText(teacher.getintrpeople());
     researchin = (TextView)findViewById(R.id.researchin);
     researchin.setText(teacher.getResearch());
    }
    private void SetButtonAndText() {
        List<Course> courseList = teacher.getCourseList();
        int tnum = courseList.size();
        if(tnum>=4)
        {
         student1.setVisibility(TextView.VISIBLE);
         courseone.setVisibility(Button.VISIBLE);
         student2.setVisibility(TextView.VISIBLE);
         coursetwo.setVisibility(Button.VISIBLE);
         student3.setVisibility(TextView.VISIBLE);
         coursethree.setVisibility(Button.VISIBLE);
         student4.setVisibility(TextView.VISIBLE);
         coursefour.setVisibility(Button.VISIBLE);
        }
        else if(tnum == 3)
        {
            student1.setVisibility(TextView.VISIBLE);
            courseone.setVisibility(Button.VISIBLE);
            student2.setVisibility(TextView.VISIBLE);
            coursetwo.setVisibility(Button.VISIBLE);
            student3.setVisibility(TextView.VISIBLE);
            coursethree.setVisibility(Button.VISIBLE);
            student4.setVisibility(TextView.GONE);
            coursefour.setVisibility(Button.GONE);
        }
        else if(tnum ==2)
        {
            student1.setVisibility(TextView.VISIBLE);
            courseone.setVisibility(Button.VISIBLE);
            student2.setVisibility(TextView.VISIBLE);
            coursetwo.setVisibility(Button.VISIBLE);
            student3.setVisibility(TextView.GONE);
            coursethree.setVisibility(Button.GONE);
            student4.setVisibility(TextView.GONE);
            coursefour.setVisibility(Button.GONE);
        }
        else if(tnum == 1)
        {
            student1.setVisibility(TextView.VISIBLE);
            courseone.setVisibility(Button.VISIBLE);
            student2.setVisibility(TextView.GONE);
            coursetwo.setVisibility(Button.GONE);
            student3.setVisibility(TextView.GONE);
            coursethree.setVisibility(Button.GONE);
            student4.setVisibility(TextView.GONE);
            coursefour.setVisibility(Button.GONE);
        }
        else if(tnum==0)
        {
            student1.setText("暂无教授课程");
            student1.setVisibility(TextView.VISIBLE);
            courseone.setVisibility(Button.GONE);
            student2.setVisibility(TextView.GONE);
            coursetwo.setVisibility(Button.GONE);
            student3.setVisibility(TextView.GONE);
            coursethree.setVisibility(Button.GONE);
            student4.setVisibility(TextView.GONE);
            coursefour.setVisibility(Button.GONE);
        }
    }

    private void getTeacher(){
        Intent intent = getIntent();
        teacher = (Teacher) intent.getSerializableExtra("Teacher");
    }
    private void SetTeacherImg(){
        // 请求教师对应头像，如果没有，就使用默认图片
        ImageRequest imageRequest = new ImageRequest(
                "http://47.100.226.176:8080/XueBaJun/teacher_image/"+teacher.getCover()+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        teacherimage.setBackground(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                teacherimage.setBackgroundResource(R.drawable.bookimgsample);
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
            List<Course> courseList = teacher.getCourseList();
            Course courseone = courseList.get(0);
            bundle.putString("courseone",courseone.getName());
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
            List<Course> courseList = teacher.getCourseList();
            Course coursetwo = courseList.get(1);
            bundle.putString("coursetwo",coursetwo.getName());
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
            List<Course> courseList = teacher.getCourseList();
            Course coursethree = courseList.get(2);
            bundle.putString("coursethree",coursethree.getName());
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
            List<Course> courseList = teacher.getCourseList();
            Course coursefour = courseList.get(3);
            bundle.putString("coursefour",coursefour.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
