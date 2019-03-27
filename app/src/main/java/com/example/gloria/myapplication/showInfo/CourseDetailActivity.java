package com.example.gloria.myapplication.showInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
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
    private Button textbbook;
    private TextView introducebook;
    private Button teacher_one;
    private Button teacher_two;
    private Button givescore;
    private RequestQueue mQueue;
    private ImageView bookimage;
    private ImageView notexist;
    User user;
    int id; // 详情界面展示的课程Id
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail);
        getPassInfo();
        init();
        getCourse();
    }

    private void getPassInfo() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        id = intent.getIntExtra("course_id",0);
        //id = 6;
        Log.e("##", "课程详情id："+id);
    }

    private void SetButton() {
        List<ProfessorCourse> professorCourseList = course.getProfessorCourseList();
        if(professorCourseList != null) {
            int tnum = professorCourseList.size();
            // Professor teachero, teachert, teacherth;
            if (tnum == 2) {
                teacher_one.setVisibility(Button.VISIBLE);
                ProfessorCourse one = professorCourseList.get(0);
                Professor professorO = one.getProfessor();
                teacher_one.setText(professorO.getName());
                teacher_two.setVisibility(Button.VISIBLE);
                ProfessorCourse two = professorCourseList.get(1);
                Professor professorT = two.getProfessor();
                teacher_two.setText(professorT.getName());
            }
            else if (tnum == 1) {
                teacher_one.setVisibility(Button.VISIBLE);
                ProfessorCourse one = professorCourseList.get(0);
                Professor professorO = one.getProfessor();
                teacher_one.setText(professorO.getName());
                teacher_two.setVisibility(Button.INVISIBLE);
            }
        }
        else {
            teacher_one.setVisibility(Button.INVISIBLE);
            teacher_two.setVisibility(Button.INVISIBLE);
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
        String url = "http://47.100.226.176:8080/XueBaJun/GetCourse";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                 course = new Gson().fromJson(jsonObject.toString(), Course.class);
                Log.e("##", "课程详情："+jsonObject.toString());
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
        course_name_text.setText(course.getName());
        //if(course.getTerm()!=null)
       // course_prefersemester.setText(course.getTerm());
       // else
            course_prefersemester.setText("暂无推荐优选学期");
        setscore.setText(String.valueOf(course.getScore()));
        Book book = course.getBook();
        SetBookImg();
        if(book == null)
            textbbook.setText("暂无教材");
        else
            textbbook.setText(book.getName());
        introducebook.setText(course.getIntro());
        teacher_one.setOnClickListener(new tone());
        teacher_two.setOnClickListener(new ttwo());
        givescore.setOnClickListener(new gscore());
        textbbook.setOnClickListener(new textbbook());
    }
    @SuppressLint("ResourceType")
    private void init() {
        notexist = (ImageView)findViewById(R.drawable.bookimgsample);
        course_name_text = (TextView)findViewById(R.id.course_name_text);
        course_prefersemester = (TextView)findViewById(R.id.course_prefersemester);
        setscore = (TextView)findViewById(R.id.setscore);
        textbbook = (Button)findViewById(R.id.textbbook);
        bookimage = (ImageView) findViewById(R.id.bookimage);
        introducebook = (TextView)findViewById(R.id.introducebook);
        teacher_one = (Button)findViewById(R.id.teacher_one);
        teacher_two = (Button)findViewById(R.id.teacher_two);
        givescore = (Button)findViewById(R.id.givescore);
        course = new Course();
        mQueue = Volley.newRequestQueue(CourseDetailActivity.this);
    }
    class textbbook implements OnClickListener
    {
        @Override
        public void onClick(View v) {

                Intent intent = new Intent(CourseDetailActivity.this, BookMainActivity.class);
                intent.putExtra("user",(Serializable) user);
                Log.e("##", "不是详情："+course.getBook().getId());
                intent.putExtra("professor_id",course.getBook().getId());
                startActivity(intent);
        }
    }
    class gscore implements OnClickListener
    {
        @Override
        public void onClick(View V) {
            Intent intent = new Intent(CourseDetailActivity.this,PingFenActivity.class);
            intent.putExtra("course",  course.getId());
            Log.e("##", "这是评分的呢的invsdnvsdnVue");
            startActivity(intent);
        }
    }
    class tone implements OnClickListener
    {
        @Override
        public void onClick(View v) {

        Intent intent = new Intent(CourseDetailActivity.this, TeacherDetailActivity.class);
                        intent.putExtra("user",(Serializable) user);
                        List<ProfessorCourse> PC = course.getProfessorCourseList();
                        Log.e("##", String.valueOf(PC.size()));
                        Log.e("##", "不是详情："+PC.get(0).getProfessor().getId());
                        intent.putExtra("professor_id",PC.get(0).getProfessor().getId());
        startActivity(intent);

    }}
    class ttwo implements OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(CourseDetailActivity.this, TeacherDetailActivity.class);
            intent.putExtra("user", (Serializable) user);
            List<ProfessorCourse> PC = course.getProfessorCourseList();
            Log.e("##", String.valueOf(PC.size()));
            Log.e("##", "不是详情：" + PC.get(1).getProfessor().getId());
            intent.putExtra("professor_id", PC.get(1).getProfessor().getId());
            startActivity(intent);
        }
    }
    private ImageView SetBookImg(){
        // 请求图书对应头像，如果没有，就使用默认图片
        Book book = course.getBook();
        if(book == null)
            return notexist;
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
        return null;
    }

}
