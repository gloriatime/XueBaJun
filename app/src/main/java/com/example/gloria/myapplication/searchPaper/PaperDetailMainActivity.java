package com.example.gloria.myapplication.searchPaper;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.adapter.CommentAdapter;
import com.example.gloria.myapplication.adapter.ReplyAdapter;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Comment;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.Reply;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * by moos on 2018/04/20
 */
public class PaperDetailMainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mName;
    private TextView mLabel1;
    private TextView mLabel2;
    private TextView mLabel3;
    private TextView mScore;
    private TextView mUploader;
    private TextView mComment;

    User user;
    int id;
    RequestQueue mQueue;
    Document document;

    private TextView mDownload;
    private TextView mDownloadPic;
    private TextView mFavorite;
    private TextView mFavoritePic;
    private TextView mShare;
    private TextView mSharePic;
    private Button BScore;
    private TextListenerDown textListenerDown;
    private TextListenerFavo textListenerFavo;
    private TextListenerShare textListenerShare;

    private static final String TAG = "MainActivity";
    private ListView listView;
    private List<Comment> commentList = new ArrayList<Comment>();
    private BottomSheetDialog dialog;
    private TextView comment_bt;
    CommentAdapter adapter;
    ReplyAdapter Radapter;
    List<Reply> replyList = new ArrayList<Reply>();
    View replyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paperdetail_main);

        //获取资料的相关信息
        //初始化相关控件
        mName = (TextView) findViewById(R.id.textViewTitle);
        mLabel1 = (TextView) findViewById(R.id.textViewLabel1);
        mLabel2 = (TextView) findViewById(R.id.textViewLabel2);
        mLabel3 = (TextView) findViewById(R.id.textViewLabel3);
        mScore = (TextView) findViewById(R.id.textViewScore);
        mUploader = (TextView) findViewById(R.id.textViewUploader);
        mComment = (TextView) findViewById(R.id.textViewComment);
        user = new User();
        id = 0;
        getUserAId();

        mQueue = Volley.newRequestQueue(PaperDetailMainActivity.this);
        document = new Document();

        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetDocument";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                document = gson.fromJson(jsonObject.toString(), Document.class);
                //成功获取资料并显示
                if (document != null) {
                    Log.e("##getSuccess##", "Id"+document.getName()+ "\n");
                    mName.setText(document.getName());
                    mLabel1.setText(document.getAuthor());
                    mLabel2.setText(document.getAuthor());
                    mLabel3.setText(document.getAuthor());
                    String str = "当前评分："+document.getScore()+"分";
                    mScore.setText(str);
                    String src = "上传者："+document.getUp_user();
                    mUploader.setText(src);
                    String scc = "评论"+document.getComment();
                    mComment.setText(scc);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);

        //下载、收藏、分享
        mDownload = (TextView)findViewById(R.id.textViewDownload);
        mDownloadPic = (TextView)findViewById(R.id.textViewDownloadPic);
        mFavorite = (TextView)findViewById(R.id.textViewFavorite);
        mFavoritePic = (TextView)findViewById(R.id.textViewFavoritePic);
        mShare = (TextView)findViewById(R.id.textViewShare);
        mSharePic = (TextView)findViewById(R.id.textViewSharePic);
        BScore = (Button)findViewById(R.id.ButtonScore);
        textListenerDown = new TextListenerDown();
        textListenerFavo = new TextListenerFavo();
        textListenerShare = new TextListenerShare();
        //下载
        mDownload.setOnClickListener(textListenerDown);
        mDownloadPic.setOnClickListener(textListenerDown);
        //收藏
        mFavorite.setOnClickListener(textListenerFavo);
        mFavoritePic.setOnClickListener(textListenerFavo);
        //分享
        mShare.setOnClickListener(textListenerShare);
        mSharePic.setOnClickListener(textListenerShare);
        BScore.setOnClickListener(this);
        /**
         * 评论
         */
        //初始化
        listView = (ListView)findViewById(R.id.comment_detail);
        comment_bt = (TextView)findViewById(R.id.textViewSay);
        //解析回复界面
        replyView = LayoutInflater.from(this).inflate(R.layout.comment_show_reply, null);
        /***
         * 评论数据
         */
        if(document.getComment()!=0) {
            adapter = new CommentAdapter(PaperDetailMainActivity.this, commentList);
            listView.setAdapter(adapter);
        }
        /**写评论*/
        comment_bt.setOnClickListener(this);
        //查看回复
        ShowReply();
        //更新显示的评论数
        UpdateCommentNumber();
    }

    private void getUserAId(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        String ID = intent.getStringExtra("document_id");
        id = Integer.parseInt(ID);
    }

    //下载
    class TextListenerDown implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final String downloadUrl = document.getUrl();
            final Context mContext = PaperDetailMainActivity.this;
            mDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //创建下载任务
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                    //指定下载路径
                    request.setDestinationInExternalPublicDir("/download/", mName.toString());
                    //获取下载管理器
                    DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    //将下载任务加载到下载队列
                    downloadManager.enqueue(request);
                }
            });
        }
    }
    //收藏
    class TextListenerFavo implements View.OnClickListener{
        @Override
        public void onClick(View view){
            String path = "http://47.100.226.176:8080/XueBaJun/CollectDocument";
            HashMap<String,String> u = new HashMap<>();
            HashMap<String,String> d = new HashMap<>();
            u.put("phone",document.getUp_user());
            d.put("id", String.valueOf(document.getId()));
            Log.e("##","发送id "+String.valueOf(document.getId())+document.getUp_user());
            Map<String, Object> map = new HashMap<>();
            map.put("user",u);
            map.put("document",d);

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
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(document.getUrl())));
            shareIntent.setType("*/*");
            startActivity(Intent.createChooser(shareIntent, "分享到："));
        }
    }
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
                        if(document.getCommentList().get(position).getReplyList() == null) {
                            Log.e("##","firstreply");
                            addFirstReply(position);
                        }
                        else {
                            showReplyDetail(position);
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewSay:
                showCommentDialog();
                ShowReply();
                break;
            case R.id.ButtonScore:
                final EditText et = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入您的评分")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                mScore.setText(et.getText().toString()+"分");
                            }
                        }).setNegativeButton("取消",null).show();
                break;
            default:
                break;
        }
    }

    //回复界面
    private void showReplyDetail(int position) {
        dialog = new BottomSheetDialog(this);
        /*****
         * 通过position获得当前评论的回复
         */
        replyList = document.getCommentList().get(position).getReplyList();
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
                    if(document.getComment()==0){
                        detailBean.setReplyList(null);
                        commentList.add(detailBean);
                        adapter = new CommentAdapter(PaperDetailMainActivity.this, commentList);
                        listView.setAdapter(adapter);
                        //添加到该文档评论中
                        document.setCommentList(commentList);
                        document.setNumber(document.getComment()+1);
                    }
                    else {
                        adapter.addTheCommentData(detailBean);
                    }
                    mComment.setText("评论"+document.getComment());
                    /*上传到服务器*/
                    SendToServer();
                    Toast.makeText(PaperDetailMainActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PaperDetailMainActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
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
                    Reply detailBean = new Reply(user,replyList.get(p).getCritic(), commentContent);
                    //Reply detailBean = new Reply(user, "回复 " + document.getUp_user() + "：" + commentContent);
                    Radapter.addTheCommentData(detailBean);
                    SendToServer();
                    Toast.makeText(PaperDetailMainActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaperDetailMainActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
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
                    //Log.e("##","第一条回复加入list");
                    replyList.add(detailBean);
                    //Log.e("##","第一条回复加入成功");
                    Radapter = new ReplyAdapter(replyView.getContext(),replyList);
                    //Log.e("##","设置第一条评论的回复第一条回复");
                    commentList.get(p).setReplyList(replyList);
                    Toast.makeText(PaperDetailMainActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                    SendToServer();
                }
                else{
                    Toast.makeText(PaperDetailMainActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void SendToServer(){
        String url = "http://47.100.226.176:8080/XueBaJun/Document";
        //发送数据
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        try {
            jsonObject.put("id", document.getId());
            jsonObject.put("name", document.getName());
            jsonObject.put("author", document.getAuthor());
            jsonObject.put("up_user", document.getUp_user());
            jsonObject.put("up_time", document.getUp_time());
            jsonObject.put("score", document.getScore());
            jsonObject.put("number",document.getNumber());
            jsonObject.put("comment", document.getComment());
            jsonObject.put("download", document.getDownload());
            jsonObject.put("url", document.getUrl());
            jsonObject.put("commentList", document.getCommentList());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("##","发送 "+jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void UpdateCommentNumber(){
        String url = "http://47.100.226.176:8080/XueBaJun/GetBook";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                document = gson.fromJson(jsonObject.toString(), Document.class);
                if (document != null) {
                    String scc = "评论"+document.getComment();
                    mComment.setText(scc);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
        Log.e("##","最后的最后"+document.getComment());
    }
}