package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.example.gloria.myapplication.search.SearchResultActivity;
import com.example.gloria.myapplication.searchPaper.PaperDetailMainActivity;
import com.example.model.myapplication.Comment;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;
import com.example.model.myapplication.Reply;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Professor professor;
    private ImageView professorimage;
    private RequestQueue mQueue;
    private TextView peopleintro;
    private TextView researchin;
    private Button courseone;
    private Button coursetwo;
    private Button coursethree;
    private Button coursefour;
    private Button course5,course6;
    private TextView score;
    TextView seescore;

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
    User user = new User();
    int id = 0;
    String  src;
    List<ProfessorCourse> professorCourse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_detail);
        mQueue = Volley.newRequestQueue(TeacherDetailActivity.this);
        listView = (ListView)findViewById(R.id.comment_detail);
        comment_bt = (TextView)findViewById(R.id.textViewSay);
        mComment = (TextView)findViewById(R.id.textViewComment);
        setBackJump();
        getPassInfo();
        init();
        getProfessor();
        //评论回复
        //解析回复界面
        replyView = LayoutInflater.from(this).inflate(R.layout.comment_show_reply, null);

        /**写评论*/
        comment_bt.setOnClickListener(this);
        //查看回复
        ShowReply();
        IntentFilter filter = new IntentFilter(PingFenActivity.action);
        registerReceiver(mRefreshBroadcastReceiver, filter);

    }
    // broadcast receiver
private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
          @Override
   public void onReceive(Context context, Intent intent) {
             // seescore.setText( intent.getExtras().getString("data"));
              src = intent.getExtras().getString("data");
              Log.e("##","professor的分数"+Float.valueOf(professor.getScore()));
              seescore.setText( src);
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
        professor = (Professor) intent.getSerializableExtra("professor");
        id = professor.getId();
        Log.e("##","professor 得到Id "+id);
    }
    private void sec_init() {
        if(professor.getIntro()!=null)
        peopleintro.setText(professor.getIntro());
        else
            peopleintro.setText("此项信息暂无");
        if(professor.getField()!=null)
        researchin.setText(professor.getField());
        else
            researchin.setText("研究领域尚不明确");
        score.setOnClickListener(new gscore());
        seescore.setText(" "+professor.getScore());
        setCourseButton();
        //courseone.setText(professor.getProfessorCourseList().get(0).getCourse().getName());
        SetProfessorImg();

    }

    private void jump(int i){
        Intent intent = new Intent(TeacherDetailActivity.this, CourseDetailActivity.class);
        intent.putExtra("user",(Serializable) user);
        Log.e("##","跳转前user"+user.getPhone());
        intent.putExtra("course_id",professorCourse.get(i).getCourse().getId());
        startActivity(intent);
    }

    private void setCourseButton() {
        courseone.setText(professorCourse.get(0).getCourse().getName());
        courseone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                jump(0);
            }
        });
        int i = professorCourse.size();
        if((--i)>0){
            coursetwo.setText(professorCourse.get(1).getCourse().getName());
            coursetwo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                   jump(1);
                }
            });
        }
        if((--i)>0){
            coursethree.setText(professorCourse.get(2).getCourse().getName());
            coursethree.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    jump(2);
                }
            });
        }
        if((--i)>0){
            coursefour.setText(professorCourse.get(3).getCourse().getName());
            coursefour.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    jump(3);
                }
            });
        }
        if((--i)>0){
            course5.setText(professorCourse.get(4).getCourse().getName());
            course5.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    jump(4);
                }
            });
        }
        if((--i)>0){
            course6.setText(professorCourse.get(5).getCourse().getName());
            course6.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    jump(5);
                }
            });
        }
    }


    private void  init() {
        professorimage = (ImageView) findViewById(R.id.teacherimage);
        courseone = (Button) findViewById(R.id.courseone);
        coursetwo = (Button) findViewById(R.id.coursetwo);
        coursethree = (Button) findViewById(R.id.coursethree);
        coursefour = (Button) findViewById(R.id.coursefour);
        peopleintro = (TextView) findViewById(R.id.peopleintro);
        researchin = (TextView) findViewById(R.id.researchin);
        score = (TextView) findViewById(R.id.score);
        course5 = (Button) findViewById(R.id.coursefive);
        course6 = (Button) findViewById(R.id.coursesix);
        seescore = (TextView) findViewById(R.id.seescore);
    }
    class gscore implements OnClickListener
    {
        @Override
        public void onClick(View V) {
            Intent intent = new Intent(TeacherDetailActivity.this,PingFenActivity.class);
            intent.putExtra("user",user);
            intent.putExtra("professor",(Serializable) professor);
            startActivity(intent);
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
                professor = new DateGson().getGson().fromJson(jsonObject.toString(), Professor.class);
                Log.e("##","professo返回 "+jsonObject.toString());
                professorCourse = professor.getProfessorCourseList();
                mComment.setText("评论"+professor.getCommentList().size());
                if(professor.getCommentList() != null) {
                    commentList = professor.getCommentList();
                    adapter = new CommentAdapter(TeacherDetailActivity.this, commentList);
                    listView.setAdapter(adapter);
                }
                sec_init();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void   SetProfessorImg(){
        // 请求教师对应头像，如果没有，就使用默认图片
        if(professor.getPic()==null)
            professorimage.setBackgroundResource(R.drawable.bookimgsample);
        else {
            ImageRequest imageRequest = new ImageRequest(
                    "http://47.100.226.176:8080/" + professor.getPic(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            BitmapDrawable temp = new BitmapDrawable(response);
                            professorimage.setBackground(temp);
                        }
                    }, 300, 500, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    professorimage.setBackgroundResource(R.drawable.bookimgsample);
                }
            });
            mQueue.add(imageRequest);
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
                    detailBean.setBelong(professor.getId());
                    if(professor.getCommentList()==null){
                        detailBean.setReplyList(null);
                        commentList.add(detailBean);
                        adapter = new CommentAdapter(TeacherDetailActivity.this, commentList);
                        listView.setAdapter(adapter);
                        //添加到该文档评论中
                        professor.setCommentList(commentList);
                        professor.setNumber(professor.getComment()+1);
                    }
                    else {
                        adapter.addTheCommentData(detailBean);
                    }
                    mComment.setText("评论"+professor.getComment());
                    /*上传到服务器*/
                    SendCommentToServer(detailBean);
                    Toast.makeText(TeacherDetailActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(TeacherDetailActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TeacherDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeacherDetailActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TeacherDetailActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(TeacherDetailActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
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
        map.put("type", "professor");
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
        String url = "http://47.100.226.176:8080/XueBaJun/GetProfessor";

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
                professor = gson.fromJson(jsonObject.toString(), Professor.class);
                if (professor != null) {
                    String scc = "评论"+professor.getComment();
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
        getProfessor();
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
            Log.e("##getView##","position"+position);
            Log.e("##请求头像用户的手机号0：", commentBeanList.get(position).getCritic().getPhone());

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, parent, false);
                commentHolder = new CommentHolder(convertView);
                convertView.setTag(commentHolder);
            } else {
                commentHolder = (CommentHolder) convertView.getTag();
            }
            final RequestQueue mQueue = Volley.newRequestQueue(convertView.getContext());
            ImageRequest imageRequest = new ImageRequest(
                    "http://47.100.226.176:8080/XueBaJun/head_image/"+commentBeanList.get(position).getCritic().getPhone()+".jpg",
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            BitmapDrawable temp = new BitmapDrawable(response);
                            commentHolder.logo.setImageDrawable(temp);
                        }
                    }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("##请求头像用户的手机号：", commentBeanList.get(position).getCritic().getPhone());
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
                            Log.e("##########","查看回复查看查看");
                            Log.e("####reply_pos",""+pos);
                            showReplyDetail(pos);
                            Log.e("###","准备进入回复界面啦啦啦啦");
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
                Intent intent = new Intent(TeacherDetailActivity.this, UserInfoActivity.class);
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
