package com.example.gloria.myapplication.searchPaper;

import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
import com.example.base.myapplication.UnfinishDialog;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.adapter.CommentAdapter;
import com.example.gloria.myapplication.adapter.ReplyAdapter;
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.gloria.myapplication.showInfo.UserInfoActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Comment;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.Reply;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Float.valueOf;

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
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paperdetail_main);
        setBackJump();
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
        document = new Document();
        id = 0;

        //下载、收藏、分享
        mDownload = (TextView)findViewById(R.id.textViewDownload);
        mDownloadPic = (TextView)findViewById(R.id.textViewDownloadPic);
        mFavorite = (TextView)findViewById(R.id.textViewFavorite);
        mFavoritePic = (TextView)findViewById(R.id.textViewFavoritePic);
        mShare = (TextView)findViewById(R.id.textViewShare);
        mSharePic = (TextView)findViewById(R.id.textViewSharePic);
        BScore = (Button)findViewById(R.id.ButtonScore);
        //评论，初始化
        listView = (ListView)findViewById(R.id.comment_detail);
        comment_bt = (TextView)findViewById(R.id.textViewSay);
        //解析回复界面
        replyView = LayoutInflater.from(this).inflate(R.layout.comment_show_reply, null);

        mQueue = Volley.newRequestQueue(PaperDetailMainActivity.this);

        //获得用户和id
        getUserAId();
        //获得和展示文档
        Log.e("##","开始获取document");
        get_show_Document();

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

        /**写评论*/
        comment_bt.setOnClickListener(this);
        //查看回复
        ShowReply();
        //更新显示的评论数
        //UpdateCommentNumber();
    }

    private void getUserAId(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        //String ID = intent.getStringExtra("document_id");
        //id = Integer.parseInt(ID);
        id = intent.getIntExtra("document_id",0);
       // Log.e("##","跳转后ID："+ID);
        Log.e("##","跳转后Id："+intent.getIntExtra("document_id",0));
    }

    private void get_show_Document(){
        Log.e("##","3,2,1");
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetDocument";

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
                document = gson.fromJson(jsonObject.toString(), Document.class);
                //成功获取资料并显示
                if (document != null) {
                    Log.e("##getSuccess##", "Id"+document.getName()+ "\n");
                    mName.setText(document.getName());
                    //获得书籍标签
                    Log.e("##","显示标签");
                    Log.e("##","taglist长度"+document.getTagList().size());
                    if(document.getTagList().size()!=0) {
                        String tag = "";
                        int n = document.getTagList().size();
                        if(n == 1) {
                            mLabel1.setText(document.getTagList().get(0).getName());
                            mLabel2.setVisibility(View.GONE);
                            mLabel3.setVisibility(View.GONE);
                        }
                        else if(n == 2){
                            mLabel1.setText(document.getTagList().get(0).getName());
                            mLabel2.setText(document.getTagList().get(1).getName());
                            mLabel3.setVisibility(View.GONE);
                        }
                        else {
                            mLabel1.setText(document.getTagList().get(0).getName());
                            mLabel2.setText(document.getTagList().get(1).getName());
                            mLabel3.setText(document.getTagList().get(2).getName());
                        }
                    }
                    /***测试*/
                    else {
                        mLabel1.setText("暂无标签");
                        mLabel2.setVisibility(View.GONE);
                        mLabel3.setVisibility(View.GONE);
                    }
                    String str = "当前评分："+document.getScore()+"分";
                    mScore.setText(str);
                    setUpUserName();
                    String scc = "评论"+document.getComment();
                    mComment.setText(scc);
                    Log.e("##","document"+document.getComment());
                    Log.e("##","document"+document.getCommentList().size());

                    if(document.getCommentList() != null) {
                        commentList = document.getCommentList();
                        //Log.e("##", "commentList" + commentList.get(0).getCritic().getPhone());
                        adapter = new CommentAdapter(PaperDetailMainActivity.this, commentList);
                        listView.setAdapter(adapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                 Log.e("##","不会吧！！！");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void setUpUserName(){
        String url = "http://47.100.226.176:8080/XueBaJun/GetUser";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", document.getUp_user());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                User upuser = gson.fromJson(jsonObject.toString(), User.class);
                name = upuser.getName();
                mUploader.setText("上传者："+ name);
                Log.e("##","name"+name + "#"+upuser.getName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    //下载
    class TextListenerDown implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e("##url", document.getUrl());
            int n = document.getUrl().length();
            final String downloadUrl = "http://47.100.226.176:8080/XueBaJun/"+document.getUrl();
            Log.e("download", downloadUrl);
            final Context mContext = PaperDetailMainActivity.this;
//            mDownload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
                    Log.e("##", "开始下载");
                    //创建下载任务
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                    //指定下载路径
                    request.setDestinationInExternalPublicDir("/XueBaJun/download/", document.getName());
                    //获取下载管理器
                    DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    //将下载任务加载到下载队列
                    downloadManager.enqueue(request);
                    Toast.makeText(PaperDetailMainActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
//                }
//            });
            //setUnfinishDialog();
        }
    }
    private void setUnfinishDialog(){
        UnfinishDialog u = new UnfinishDialog(this);
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
            d.put("applicant",user.getPhone());
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
            View dView = getWindow().getDecorView();//获得程序显示的区域
            dView.setDrawingCacheEnabled(true);//开启cache,使getDrawingCache()可以得到图像
            dView.buildDrawingCache();//预防缓存失败
            Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bitmap, document.getName()));//分享内容
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
                                Log.e("##","评分前"+document.getNumber());
                                int sum = document.getNumber()+1;
                                float sc = (document.getScore() * document.getNumber()+  Float.valueOf(et.getText().toString()).floatValue())/sum;
                                BigDecimal bg = new BigDecimal(sc);
                                float f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                                String res = "当前评分"+f1+"分";
                                mScore.setText(res);
                                /****
                                 * 提交分数
                                 */
                                Log.e("##","最后计算的分数"+sc);
                                String url = "http://47.100.226.176:8080/XueBaJun/ScoreDocument";
                                org.json.JSONObject jsonObject = new org.json.JSONObject();
                                try {
                                    jsonObject.put("score", sc);
                                    jsonObject.put("id", document.getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("##", "score document_id"+jsonObject);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                                    public void onResponse(JSONObject jsonObject) {
                                        Gson gson = new DateGson().getGson();
                                        Document d = gson.fromJson(jsonObject.toString(), Document.class);
                                        Log.e("##","上传数据库之后的分数"+d.getScore()+" "+d.getId());
                                        Log.e("##","评分后"+document.getNumber());
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                    }
                                });
                                mQueue.add(jsonObjectRequest);
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
        Radapter = new ReplyAdapter(replyView.getContext(), replyList);
        ListView replyListView = (ListView)replyView.findViewById(R.id.reply_list);
        replyListView.setAdapter(Radapter);
        ViewGroup parent = (ViewGroup)replyView.getParent();
        if(parent!=null){
            parent.removeAllViews();
            Log.e("####","有父布局");
        }
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
                    detailBean.setBelong(document.getId());
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
                    SendCommentToServer(detailBean);
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
                    detailBean.setBelong(commentList.get(p).getId());
                    detailBean.setAt(replyList.get(p).getCritic());
                    //Reply detailBean = new Reply(user, "回复 " + document.getUp_user() + "：" + commentContent);
                    Radapter.addTheCommentData(detailBean);
                    SendReplyToSever(detailBean);
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
                    detailBean.setBelong(commentList.get(p).getId());
                    detailBean.setAt(commentList.get(p).getCritic());
                    //Log.e("##","第一条回复加入list");
                    replyList.add(detailBean);
                    //Log.e("##","第一条回复加入成功");
                    Radapter = new ReplyAdapter(replyView.getContext(),replyList);
                    //Log.e("##","设置第一条评论的回复第一条回复");
                    commentList.get(p).setReplyList(replyList);
                    SendReplyToSever(detailBean);
                    Toast.makeText(PaperDetailMainActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PaperDetailMainActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
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
        map.put("type", "document");
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
        Log.e("##","最后的最后1"+document.getComment());
        String url = "http://47.100.226.176:8080/XueBaJun/GetDocument";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("applicant",user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Log.e("##","最后的最后2"+document.getComment());
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
        adapter = new CommentAdapter(PaperDetailMainActivity.this, document.getCommentList());
        listView.setAdapter(adapter);
        Log.e("##","最后的最后"+document.getComment());
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
                Intent intent = new Intent(PaperDetailMainActivity.this, UserInfoActivity.class);
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
