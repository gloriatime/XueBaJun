package com.example.gloria.myapplication.showInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.BackJump;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.adapter.CommentAdapter;
import com.example.gloria.myapplication.adapter.ReplyAdapter;
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.gloria.myapplication.searchPaper.PaperDetailMainActivity;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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

    private TextView textViewFavo;
    private TextView textViewShare;

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

    // 收藏、分享
    private TextView mFavoritePic;
    private TextView mSharePic;
    private TextListenerFavo textListenerFavo;
    private TextListenerShare textListenerShare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail);
        listView = (ListView)findViewById(R.id.comment_detail);
        comment_bt = (TextView)findViewById(R.id.textViewSay);
        mComment = (TextView)findViewById(R.id.textViewComment);
        mFavoritePic = (TextView)findViewById(R.id.textViewFavoritePic);
        mSharePic = (TextView)findViewById(R.id.textViewSharePic);
        getPassInfo();
        setBackJump();
        init();
        getCourse();

        textListenerFavo = new TextListenerFavo();
        textListenerShare = new TextListenerShare();
        mFavoritePic.setOnClickListener(textListenerFavo);
        mSharePic.setOnClickListener(textListenerShare);
        //评论回复
        //解析回复界面
        replyView = LayoutInflater.from(this).inflate(R.layout.comment_show_reply, null);

        /**写评论*/
        comment_bt.setOnClickListener(this);
        //查看回复
        ShowReply();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshTeacher");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

    }
    // broadcast receiver
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshTeacher")) {
                setscore.setText(" "+course.getScore());
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    private void getPassInfo() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.e("##","user长度"+user.getPhone());
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
            Log.e("##","course请求 "+id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetCourse";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Log.e("##","course返回了 "+jsonObject.toString());
                Gson gson = new DateGson().getGson();
                course = gson.fromJson(jsonObject.toString(), Course.class);
                Log.e("##","有评论吗?"+course.getComment());
                Log.e("##","评论"+course.getCommentList().size());
                mComment.setText("评论"+course.getCommentList().size());
                if(course.getCommentList() != null) {
                    commentList = course.getCommentList();
                    adapter = new CommentAdapter(CourseDetailActivity.this, commentList);
                    listView.setAdapter(adapter);
                }
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
        if(course.getTerm()!=null)
        course_prefersemester.setText(course.getTerm());
        else
            course_prefersemester.setText("选课时间尚未给出");
        setscore.setText(String.valueOf(course.getScore()));
        Book book = course.getBook();
        if(book!=null){
            SetBookImg();
            textbbook.setText(book.getName());
        }else {
            bookimage.setBackgroundResource(R.drawable.bookimgsample);
        }
        if(course.getIntro()!=null)
        introducebook.setText(course.getIntro());
        else
            introducebook.setText("暂无简介");
        teacher_one.setOnClickListener(new tone());
        teacher_two.setOnClickListener(new ttwo());
        teacher_three.setOnClickListener(new tthree());
        givescore.setOnClickListener(new gscore());
        textbbook.setOnClickListener(new textbbook());
    }

    private void init() {
        course_name_text = (TextView)findViewById(R.id.course_name_text);
        course_prefersemester = (TextView)findViewById(R.id.course_prefersemester);
        setscore = (TextView)findViewById(R.id.setscore);
        textbbook = (Button)findViewById(R.id.textbbook);
        bookimage = (ImageView) findViewById(R.id.bookimage);
        introducebook = (TextView)findViewById(R.id.introducebook);
        teacher_one = (Button)findViewById(R.id.teacher_one);
        teacher_two = (Button)findViewById(R.id.teacher_two);
        teacher_three = (Button)findViewById(R.id.teacher_three);
        givescore = (Button)findViewById(R.id.givescore);
        mQueue = Volley.newRequestQueue(CourseDetailActivity.this);
    }
    class gscore implements OnClickListener
    {
        @Override
        public void onClick(View V) {
            Intent intent = new Intent(CourseDetailActivity.this,activity_T_pingfen.class);
            intent.putExtra("user",user);
            intent.putExtra("course", (Serializable) course);
            startActivity(intent);
        }
    }
    class textbbook implements OnClickListener
    {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(CourseDetailActivity.this,BookMainActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("book_id",course.getBook().getId());
        startActivity(intent);
        }
    }
    class tone implements OnClickListener
    {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(CourseDetailActivity.this,TeacherDetailActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("professor",course.getProfessorCourseList().get(0).getProfessor());
        startActivity(intent);
        }
    }
    class ttwo implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(CourseDetailActivity.this,TeacherDetailActivity.class);
            intent.putExtra("user",user);
            intent.putExtra("professor",course.getProfessorCourseList().get(1).getProfessor());
            startActivity(intent);
        }
    }
    class tthree implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(CourseDetailActivity.this,TeacherDetailActivity.class);
            intent.putExtra("user",user);
            intent.putExtra("professor",course.getProfessorCourseList().get(2).getProfessor());
            startActivity(intent);
        }
    }
    private void SetBookImg(){
        // 请求图书对应头像，如果没有，就使用默认图片
        Book book = course.getBook();
        if(book.getCover()==null)
            bookimage.setBackgroundResource(R.drawable.bookimgsample);
        else {
            Log.e("##","课程图片请求");
            ImageRequest imageRequest = new ImageRequest(
                    "http://47.100.226.176:8080/" + book.getCover() ,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Log.e("##","课程图片返回");
                            BitmapDrawable temp = new BitmapDrawable(response);
                            bookimage.setBackground(temp);
                        }
                    }, 300, 400, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    bookimage.setBackgroundResource(R.drawable.bookimgsample);
                }
            });
            mQueue.add(imageRequest);
        }
    }

    class TextListenerFavo implements View.OnClickListener{
        @Override
        public void onClick(View view){
            String path = "http://47.100.226.176:8080/XueBaJun/CollectCourse";
            HashMap<String,String> u = new HashMap<>();
            HashMap<String,String> c = new HashMap<>();
            u.put("phone",user.getPhone());
            c.put("id", String.valueOf(course.getId()));
            c.put("applicant",user.getPhone());
            Log.e("##","发送id "+String.valueOf(course.getId())+user.getPhone());
            Map<String, Object> map = new HashMap<>();
            map.put("user",u);
            map.put("course",c);

            org.json.JSONObject jsonObject = new org.json.JSONObject(map);
            Log.e("##","发送 "+jsonObject.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(path, jsonObject, new Response.Listener<org.json.JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(jsonObjectRequest);
            Resources resource = getBaseContext().getResources();
            Drawable mDrawable = resource.getDrawable(R.drawable.favorite_after);
            mFavoritePic.setBackgroundDrawable(mDrawable);
        }
    }
    //分享
    class TextListenerShare implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            View dView = getWindow().getDecorView();//获得程序显示的区域
            dView.setDrawingCacheEnabled(true);//开启cache,使getDrawingCache()可以得到图像
            dView.buildDrawingCache();//预防缓存失败
            Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bitmap, course.getName()));//分享内容
            } catch (IOException e) {
                e.printStackTrace();
            }
            shareIntent.setType("*/*");//分享类型
            startActivity(Intent.createChooser(shareIntent, "分享到："));
        }
    }
    private static Uri saveBitmap(Bitmap bm, String picName) throws IOException {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/img/"+picName+".jpg";
        File f = new File(dir);
        if(!f.exists()){
            f.getParentFile().mkdir();//创建文件夹
            f.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(f);
        bm.compress(Bitmap.CompressFormat.PNG, 90, out);//压缩图片
        out.flush();
        out.close();
        Uri uri = Uri.fromFile(f);
        return uri;
    }

    //评论回复
    //评论界面的监听
    private void ShowReply(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                showReplyDialog(position);
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
        ViewGroup parent = (ViewGroup)replyView.getParent();
        if(parent!=null){
            parent.removeAllViews();
            Log.e("####","有父布局");
        }
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
        map.put("type", "course");
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
            jsonObject.put("applicant",user.getPhone());
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

    public void onResume(){
        super.onResume();
        getCourse();
    }

    ImageButton back_button;
    public void setBackJump(){
        back_button= (ImageButton) findViewById(R.id.back_button);
        BackJump bj = new BackJump();
        bj.setBack(back_button);
    }

    //adapter
    public class CommentAdapter extends BaseAdapter {
        private List<Comment> commentBeanList;
        private Context context;

        public CommentAdapter(Context context, List<Comment> commentBeanList) {
            this.context = context;
            this.commentBeanList = commentBeanList;
        }

        @Override
        public int getCount() {
            return commentBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return commentBeanList.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CommentHolder commentHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, parent, false);
                commentHolder = new CommentHolder(convertView);
                convertView.setTag(commentHolder);
            } else {
                commentHolder = (CommentHolder) convertView.getTag();
            }
            final RequestQueue mQueue = Volley.newRequestQueue(convertView.getContext());
            ImageRequest imageRequest = new ImageRequest(
                    "http://47.100.226.176:8080/XueBaJun/head_image/" + commentBeanList.get(position).getCritic().getPhone() + ".jpg",
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            BitmapDrawable temp = new BitmapDrawable(response);
                            commentHolder.logo.setImageDrawable(temp);
                        }
                    }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    commentHolder.logo.setBackgroundResource(R.drawable.ic_head_image);
                }
            });
            mQueue.add(imageRequest);
            commentHolder.logo.setOnClickListener(new ImageListener(position));
            commentHolder.tv_name.setText(commentBeanList.get(position).getCritic().getName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(commentBeanList.get(position).getDate());
            commentHolder.tv_time.setText(date);
            commentHolder.tv_content.setText(commentBeanList.get(position).getContent());
            commentHolder.tv_reply.setTag(position);
            commentHolder.tv_reply.setOnClickListener(new MyListener(position));

            return convertView;
        }

        private class CommentHolder {
            private CircleImageView logo;
            private TextView tv_name, tv_content, tv_time;
            private TextView tv_reply;

            public CommentHolder(View view) {
                logo = (CircleImageView) view.findViewById(R.id.comment_item_logo);
                tv_content = (TextView) view.findViewById(R.id.comment_item_content);
                tv_name = (TextView) view.findViewById(R.id.comment_item_userName);
                tv_time = (TextView) view.findViewById(R.id.comment_item_time);
                tv_reply = (TextView) view.findViewById(R.id.comment_item_reply);
            }
        }

        class MyListener implements View.OnClickListener {
            int pos;

            public MyListener(int pos) {
                this.pos = pos;
            }

            @Override
            public void onClick(final View arg0) {
                String url = "http://47.100.226.176:8080/XueBaJun/GetComment";

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", commentList.get(pos).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new DateGson().getGson();
                        Comment ct = gson.fromJson(jsonObject.toString(), Comment.class);
                        Log.e("##", "评论是否有回复" + ct.getReplyList().size());
                        if (ct.getReplyList().size() == 0) {
                            Log.e("##", "firstreply");
                            addFirstReply(pos);
                        } else {
                            replyList = ct.getReplyList();
                            Log.e("##########", "查看回复查看查看");
                            Log.e("####reply_pos", "" + pos);
                            showReplyDetail(pos);
                            Log.e("###", "准备进入回复界面啦啦啦啦");
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

        class   ImageListener implements View.OnClickListener {
            int pos;

            public ImageListener(int pos) {
                this.pos = pos;
            }

            @Override
            public void onClick(final View arg0) {
                Intent intent = new Intent(CourseDetailActivity.this, UserInfoActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("user_info", commentList.get(pos).getCritic());
                startActivity(intent);
            }
        }

        /********
         * 插入一条新的评论
         * @param commentDetailBean
         */
        public void addTheCommentData(Comment commentDetailBean) {
            if (commentDetailBean != null) {
                commentDetailBean.setReplyList(null);
                commentBeanList.add(commentDetailBean);
                notifyDataSetChanged();
            }
        }
    }
}
