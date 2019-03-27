package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.adapter.CommentAdapter;
import com.example.gloria.myapplication.adapter.ReplyAdapter;
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Comment;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.News;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;
import com.example.model.myapplication.Reply;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener{

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

    //评论回复定义
    private ListView listView;
    private TextView mComment;
    private List<Comment> commentList = new ArrayList<>();
    private BottomSheetDialog dialog;
    private TextView comment_bt;
    CommentAdapter adapter;
    ReplyAdapter Radapter;
    List<Reply> replyList = new ArrayList<Reply>();
    View replyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.course_detail);

        listView = (ListView)findViewById(R.id.comment_detail);
        comment_bt = (TextView)findViewById(R.id.textViewSay);
        mComment = (TextView)findViewById(R.id.textViewComment);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.course_title);
        getPassInfo();
        getCourse();
        init();
        SetButton();

        //评论回复
        //解析回复界面
        replyView = LayoutInflater.from(this).inflate(R.layout.comment_show_reply, null);

        /**写评论*/
        comment_bt.setOnClickListener(this);
        //查看回复
        ShowReply();
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
                Log.e("##","comment_id"+course.getComment());
                //Log.e("##","长度11 "+book.getCommentList().size());
                if(course.getCommentList() != null) {
                    Log.e("##","长度22 "+course.getCommentList().size());
                    commentList = course.getCommentList();
                    // Log.e("##", "commentList" + commentList.get(0).getCritic().getPhone());
                    adapter = new CommentAdapter(CourseDetailActivity.this, commentList);
                    listView.setAdapter(adapter);
                }

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
        intent.putExtra("user",user);
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

    //评论回复
    //评论界面的监听
    private void ShowReply(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView view_reply = (TextView)findViewById(R.id.comment_item_reply);
                view_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //展示回复界面
                        //Log.e("##","展示回复界面");
                        //Log.e("##","position="+position);
                        //获得该评论的回复
                        String url = "http://47.100.226.176:8080/XueBaJun/GetComment";

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", commentList.get(position).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

                            public void onResponse(JSONObject jsonObject) {
                                Gson gson = new DateGson().getGson();
                                Comment ct = gson.fromJson(jsonObject.toString(), Comment.class);
                                Log.e("##","评论是否有回复"+ct.getReplyList().size());
                                if(ct.getReplyList().size() == 0) {
                                    Log.e("##","firstreply");
                                    addFirstReply(position);
                                }
                                else {
                                    replyList = ct.getReplyList();
                                    showReplyDetail(position);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }
                });
            }
        });
    }
    @Override
    public void onClick(View view) {
        showCommentDialog();
        ShowReply();
    }

    //回复界面
    private void showReplyDetail(int position) {
        dialog = new BottomSheetDialog(this);
        /*****
         * 通过position获得当前评论的回复
         */
        Radapter = new ReplyAdapter(replyView.getContext(), replyList);
        ListView replyListView = (ListView)replyView.findViewById(R.id.reply_list);
        replyListView.setAdapter(Radapter);
        dialog.setContentView(replyView);
        /***********
         * 监听回复
         */
        replyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int p, long id) {
                showReplyDialog(p);
            }
        });
        dialog.dismiss();
        dialog.show();
    }

    //弹出评论框并添加评论
    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this);
        //解析编辑发送界面
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText)commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        View parent = (View)commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){
                    dialog.dismiss();
                    Date now = new Date();
                    /**************
                     * 从其他页传来的当前用户的姓名
                     * ******************/
                    Comment detailBean = new Comment(user, commentContent, now);
                    Log.e("##","评论内容"+detailBean.getContent());
                    detailBean.setBelong(course.getId());
                    if(course.getCommentList()==null){
                        detailBean.setReplyList(null);
                        commentList.add(detailBean);
                        adapter = new CommentAdapter(CourseDetailActivity.this, commentList);
                        listView.setAdapter(adapter);
                        //添加到该文档评论中
                        course.setCommentList(commentList);
                        course.setNumber(course.getComment()+1);
                    }
                    else {
                        adapter.addTheCommentData(detailBean);
                    }
                    mComment.setText("评论"+course.getComment());
                    /*上传到服务器*/
                    SendCommentToServer(detailBean);
                    Toast.makeText(CourseDetailActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CourseDetailActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    //弹出回复框并添加回复
    private void showReplyDialog(final int p) {
        dialog = new BottomSheetDialog(this);
        //解析编辑发送界面
        Log.e("##","replydialog");
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText)commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        View parent = (View)commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {
                    dialog.dismiss();
                    /**************
                     * 从其他页传来的当前用户的姓名
                     * ******************/
                    Reply detailBean = new Reply(user,  replyList.get(p).getCritic(), commentContent);
                    detailBean.setBelong(commentList.get(p).getId());
                    detailBean.setAt(replyList.get(p).getCritic());
                    Radapter.addTheCommentData(detailBean);
                    SendReplyToSever(detailBean);
                    Toast.makeText(CourseDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    //弹出回复框并添加回复
    private void addFirstReply(final int p) {
        dialog = new BottomSheetDialog(this);
        //解析编辑发送界面
        Log.e("##","11replydialog");
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText)commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        commentText.setHint("请输入您的回复.....");
        View parent = (View)commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());
        bt_comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){
                    dialog.dismiss();
                    /**************
                     * 从其他页传来的当前用户的姓名
                     * ******************/
                    //Log.e("##","第一条回复");
                    Reply detailBean = new Reply(user, commentContent);
                    detailBean.setBelong(commentList.get(p).getId());
                    detailBean.setAt(commentList.get(p).getCritic());
                    //Log.e("##","第一条回复加入list");
                    replyList.add(detailBean);
                    //Log.e("##","第一条回复加入成功");
                    Radapter = new ReplyAdapter(replyView.getContext(),replyList);
                    //Log.e("##","设置第一条评论的回复第一条回复");
                    commentList.get(p).setReplyList(replyList);
                    SendReplyToSever(detailBean);
                    Toast.makeText(CourseDetailActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CourseDetailActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void SendCommentToServer(Comment comment){
        Log.e("##", "Content="+comment.getContent());
        Log.e("##","critic="+comment.getCritic().getName());
        String url = "http://47.100.226.176:8080/XueBaJun/AddComment";
        //发送数据
        org.json.JSONObject jsonObject ;

        HashMap<String,String> u = new HashMap<>();
        u.put("phone",user.getPhone());
        Map<String, Object> map = new HashMap<>();
        map.put("critic",u);
        map.put("type", "book");
        map.put("content", comment.getContent());
        map.put("belong",comment.getBelong());
        jsonObject = new JSONObject(map);

        Log.e("##","发送 "+jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.e("##","评论返回 ");
                UpdateCommentNumber();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("##","评论返回cuowu ");
                UpdateCommentNumber();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void SendReplyToSever(Reply reply){
        String url = "http://47.100.226.176:8080/XueBaJun/AddReply";
        //发送数据
        //发送数据
        org.json.JSONObject jsonObject ;

        HashMap<String,String> u = new HashMap<>();
        u.put("phone",user.getPhone());
        HashMap<String, String> a = new HashMap<>();
        a.put("phone", reply.getAt().getPhone());
        Map<String, Object> map = new HashMap<>();
        map.put("critic",u);
        map.put("at", a);
        map.put("content", reply.getContent());
        map.put("belong",reply.getBelong());
        jsonObject = new JSONObject(map);
        Log.e("##","发送 "+jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.e("##","回复上传成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("##","回复上传失败");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void UpdateCommentNumber(){
        String url = "http://47.100.226.176:8080/XueBaJun/GetCourse";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                course = gson.fromJson(jsonObject.toString(), Course.class);
                if (course != null) {
                    String scc = "评论"+course.getComment();
                    mComment.setText(scc);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

}
