package com.example.gloria.myapplication.bookDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.gloria.myapplication.adapter.ReplyAdapter;
import com.example.gloria.myapplication.showInfo.CourseDetailActivity;
import com.example.gloria.myapplication.showInfo.UserInfoActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Comment;
import com.example.model.myapplication.Reply;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookMainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mName;
    private TextView mAuthor;
    private TextView mPress;
    private TextView mCourse;
    private TextView mSynopsis;
    private TextView mBookPic;
    private TextView mScore;
    private TextView mComment;
    private TextView mLabel;

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

    private ListView listView;
    private List<Comment> commentList = new ArrayList<>();
    private BottomSheetDialog dialog;
    private TextView comment_bt;
    CommentAdapter adapter;
    ReplyAdapter Radapter;
    List<Reply> replyList = new ArrayList<Reply>();
    View replyView;

    RequestQueue mQueue;
    Book book;
    User user;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_main);

        //获取书籍相关信息
        //初始化
        mName = (TextView)findViewById(R.id.textViewName);
        mAuthor = (TextView)findViewById(R.id.textViewAuthor);
        mPress = (TextView)findViewById(R.id.textViewPress);
        mCourse = (TextView)findViewById(R.id.textViewCourse);
        mBookPic = (TextView)findViewById(R.id.textViewBookPic);
        mSynopsis = (TextView)findViewById(R.id.textViewSynopsis);
        mScore = (TextView)findViewById(R.id.textViewScore);
        mComment = (TextView)findViewById(R.id.textViewComment);
        mLabel = (TextView)findViewById(R.id.textViewLabel);

        mQueue = Volley.newRequestQueue(BookMainActivity.this);
        book = new Book();
        user = new User();
        id = 0;

        listView = (ListView)findViewById(R.id.comment_detail);
        comment_bt = (TextView)findViewById(R.id.textViewSay);

        setBackJump();
        getUserAId();

        Log.e("##","id0="+id);
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetBook";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("applicant", user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("##","id="+id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                book = gson.fromJson(jsonObject.toString(), Book.class);
                if (book != null) {
                    Log.e("##getSuccess##", ""+book.getAuthor());
                    mName.setText(book.getName());
                    if(book.getAuthor() != null)
                       mAuthor.setText(book.getAuthor());
                    else mAuthor.setText("佚名");
                    if(book.getPress().equals("没简介。"))
                         mPress.setText("佚名出版社");
                    else mPress.setText(book.getPress());
                    //获得书籍标签
                    Log.e("##","显示标签");
                    if(book.getTagList()!=null) {
                        String tag = "";
                        for (int i = 0; i < 3 && i < book.getTagList().size(); i++) {
                            if(i != 0) tag += "/";
                            tag += book.getTagList().get(i).getName();
                        }
                        mLabel.setText(tag);
                    }
                    if(book.getCourse() != null) {
                        mCourse.setText(book.getCourse().getName());
                    }
                    else{
                        mCourse.setText("暂无相关课程");
                    }
                    if(book.getIntro().length() == 0)
                    mSynopsis.setText("简介：暂无");
                    else mSynopsis.setText("简介："+book.getIntro());
                    //获得书籍图片
                    Log.e("##","picture");
                    RequestQueue mQueue = Volley.newRequestQueue(BookMainActivity.this);
                    ImageRequest imageRequest = new ImageRequest(
                            "http://47.100.226.176:8080/"+book.getCover(),
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    BitmapDrawable temp = new BitmapDrawable(response);
                                    mBookPic.setBackground(temp);
                                }
                            }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mBookPic.setText("暂无封面");
                            mBookPic.setBackgroundColor(Color.GRAY);
                        }
                    });
                    mQueue.add(imageRequest);

                    String str = "当前评分："+book.getScore()+"分";
                    mScore.setText(str);
                    String scc = "评论"+book.getComment();
                    mComment.setText(scc);
                    Log.e("##","comment_id"+book.getComment());
                    //Log.e("##","长度11 "+book.getCommentList().size());
                    if(book.getCommentList() != null) {
                        Log.e("##","长度22 "+book.getCommentList().size());
                        commentList = book.getCommentList();
                       // Log.e("##", "commentList" + commentList.get(0).getCritic().getPhone());
                        adapter = new CommentAdapter(BookMainActivity.this, commentList);
                        listView.setAdapter(adapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("##","error enter");
                Log.e("##","detail_error: "+volleyError);
            }
        });
        mQueue.add(jsonObjectRequest);
        mSynopsis.setMovementMethod(ScrollingMovementMethod.getInstance());

        mCourse.setOnClickListener(this);

        //下载、收藏、分享
        //mDownload = (TextView)findViewById(R.id.textViewDownload);
       // mDownloadPic = (TextView)findViewById(R.id.textViewDownloadPic);
        mFavorite = (TextView)findViewById(R.id.textViewFavorite1);
        mFavoritePic = (TextView)findViewById(R.id.textViewFavoritePic);
        mShare = (TextView)findViewById(R.id.textViewShare1);
        mSharePic = (TextView)findViewById(R.id.textViewSharePic);
        BScore = (Button)findViewById(R.id.ButtonScore);
        textListenerDown = new TextListenerDown();
        textListenerFavo = new TextListenerFavo();
        textListenerShare = new TextListenerShare();
        //下载
       // mDownload.setOnClickListener(textListenerDown);
       // mDownloadPic.setOnClickListener(textListenerDown);
        //收藏
       // mFavorite.setOnClickListener(textListenerFavo);
        mFavoritePic.setOnClickListener(textListenerFavo);
        //分享
       // mShare.setOnClickListener(textListenerShare);
        mSharePic.setOnClickListener(textListenerShare);
        BScore.setOnClickListener(this);

        //解析回复界面
        replyView = LayoutInflater.from(this).inflate(R.layout.comment_show_reply, null);

        /**写评论*/
        comment_bt.setOnClickListener(this);
        //查看回复
        ShowReply();
        //**更新显示的“评论数”*/
        //UpdateCommentNumber();
    }

    private void getUserAId(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        id = intent.getIntExtra("book_id",0);
        Log.e("##","跳转后Id："+intent.getIntExtra("book_id", 0));
    }
    //下载
    class TextListenerDown implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            /*final String downloadUrl = book.getCover();
            final Context mContext = BookMainActivity.this;
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
            });*/
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
            String path = "http://47.100.226.176:8080/XueBaJun/CollectBook";
            HashMap<String,String> u = new HashMap<>();
            HashMap<String,String> d = new HashMap<>();
            u.put("phone",user.getPhone());
            d.put("id", String.valueOf(book.getId()));
            d.put("applicant",user.getPhone());
            Log.e("##","发送id "+String.valueOf(book.getId())+book.getName());
            Map<String, Object> map = new HashMap<>();
            map.put("user",u);
            map.put("book",d);

            org.json.JSONObject jsonObject = new org.json.JSONObject(map);
            Log.e("##","发送 "+jsonObject.toString());

           /* collectBook.setBook(book);
            collectBook.setUser(user);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", collectBook.getUser().getPhone());
                jsonObject.put("id", collectBook.getBook().getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(path, jsonObject, new Response.Listener<org.json.JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("##","shoucang");
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
                shareIntent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bitmap, book.getName()));//分享内容
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
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
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
                                int sum = book.getNumber()+1;
                                float sc = (book.getScore() * book.getNumber()+  Float.valueOf(et.getText().toString()).floatValue())/sum;
                                BigDecimal bg = new BigDecimal(sc);
                                float f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                                String res = "当前评分"+f1+"分";
                                mScore.setText(res);
                                /****
                                 * 提交分数
                                 */
                                Log.e("##","最后计算的分数"+sc);
                                String url = "http://47.100.226.176:8080/XueBaJun/ScoreBook";
                                org.json.JSONObject jsonObject = new org.json.JSONObject();
                                try {
                                    jsonObject.put("score", sc);
                                    jsonObject.put("id", book.getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("##", "score book_id"+jsonObject);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                                    public void onResponse(JSONObject jsonObject) {
                                        Gson gson = new DateGson().getGson();
                                        Book d = gson.fromJson(jsonObject.toString(), Book.class);
                                        Log.e("##","上传数据库之后的分数"+d.getScore()+" "+d.getId());
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
            case R.id.textViewCourse:
                    Intent intent = new Intent(BookMainActivity.this, CourseDetailActivity.class);
                    intent.putExtra("user",user);
                    intent.putExtra("course_id", book.getCourse().getId());
                    startActivity(intent);
                    break;
            default:
                break;
        }
    }

    //回复界面
    private void showReplyDetail(final int position) {
        Log.e("####","detail_1111");
        dialog = new BottomSheetDialog(this);
        /*****
         * 通过position获得当前评论的回复
         */
        Radapter = new ReplyAdapter(replyView.getContext(), replyList);
        ListView replyListView = (ListView)replyView.findViewById(R.id.reply_list);
        replyListView.setAdapter(Radapter);
        Log.e("####","detail_2222");

        if(dialog!=null)
            Log.e("###","回复界面不为空"+dialog.getContext());

        ViewGroup parent = (ViewGroup)replyView.getParent();
        if(parent!=null){
            parent.removeAllViews();
            Log.e("####","有父布局");
        }
        dialog.setContentView(replyView);
        Log.e("####","detail_3333");
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
        Log.e("####","detail_dismiss");
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
                    detailBean.setBelong(book.getId());
                    if(book.getCommentList()==null){
                        detailBean.setReplyList(null);
                        commentList.add(detailBean);
                        adapter = new CommentAdapter(BookMainActivity.this, commentList);
                        listView.setAdapter(adapter);
                        //添加到该文档评论中
                        book.setCommentList(commentList);
                        book.setNumber(book.getComment()+1);
                    }
                    else {
                        adapter.addTheCommentData(detailBean);
                    }
                    mComment.setText("评论"+book.getComment());
                    /*上传到服务器*/
                    SendCommentToServer(detailBean);
                    Toast.makeText(BookMainActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(BookMainActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    //弹出回复框并添加回复
    private void showReplyDialog(int position) {
        final int p = position;
        Log.e("##","进入replydialog");
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
                    Toast.makeText(BookMainActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookMainActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BookMainActivity.this, "评论成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(BookMainActivity.this, "评论不能为空",Toast.LENGTH_SHORT).show();
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
        Log.e("##","最后的最后p"+book.getComment());
        String url = "http://47.100.226.176:8080/XueBaJun/GetBook";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("applicant",user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Log.e("##","最后的最后2"+book.getComment());
                Gson gson = new DateGson().getGson();
                book = gson.fromJson(jsonObject.toString(), Book.class);
                if (book != null) {
                    String scc = "评论"+book.getComment();
                    mComment.setText(scc);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
        adapter = new CommentAdapter(BookMainActivity.this, book.getCommentList());
        listView.setAdapter(adapter);
        Log.e("##","最后的最后"+book.getComment());
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
                Intent intent = new Intent(BookMainActivity.this, UserInfoActivity.class);
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
