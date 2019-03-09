package com.example.gloria.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.GlideImageLoader;
import com.example.gloria.myapplication.manage.AboutMeActivity;
import com.example.gloria.myapplication.manage.ChangeInfoActivity;
import com.example.gloria.myapplication.manage.MyCollectActivity;
import com.example.gloria.myapplication.manage.MyConcernActivity;
import com.example.gloria.myapplication.manage.UploadFileActivity;
import com.example.gloria.myapplication.search.SearchResultActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView title;// 学吧君的标题文字
    TextView search;
    Spinner spinner;
    List<String> data_list;
    ArrayAdapter<String> arr_adapter;
    ImageButton user_management_button,head_image_button;
    DrawerLayout main_drawable;
    LinearLayout uploade_doc_linear,about_me_linear,my_collect_linear,change_info_linear,my_concern_linear,left_drawable;
    Button check_in_button;
    TextView name_text,point_text,college_text,grade_text,interest_text;
    TextView app_name;

    // 搜索功能
    TextView search_box;
    ImageButton search_button;
    // ListView user_management_list;

    // 设置本周热门TOP
    TextView course_top,book_top,document_top;
    Banner banner;

    User user;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setMainPage();

        getUser();

        setLeftDrawable();

        // 使用文件选择器之后标题会被选择器的名称覆盖
        app_name.setText("学吧君");

        //testFun();

    }


    private void testFun() {
        Intent intent = new Intent(MainActivity.this,TempActivity.class);

        startActivity(intent);
    }

    private void init(){
        user = new User();
        spinner = (Spinner) findViewById(R.id.spinner);
        user_management_button = (ImageButton) findViewById(R.id.user_management_button);
        main_drawable = (DrawerLayout) findViewById(R.id.main_layout);
        uploade_doc_linear = (LinearLayout) findViewById(R.id.upload_doc_linear);
        about_me_linear = (LinearLayout) findViewById(R.id.about_me_linear);
        my_collect_linear = (LinearLayout) findViewById(R.id.my_collect_linear);
        change_info_linear = (LinearLayout) findViewById(R.id.change_info_linear);
        my_concern_linear = (LinearLayout) findViewById(R.id.my_concern_linear);
        head_image_button = (ImageButton) findViewById(R.id.head_image_button);
        left_drawable = (LinearLayout) findViewById(R.id.left_drawable);
        check_in_button = (Button) findViewById(R.id.check_in_button);
        name_text = (TextView) findViewById(R.id.name_text);
        point_text = (TextView) findViewById(R.id.point_text);
        grade_text = (TextView) findViewById(R.id.grade_text);
        college_text = (TextView) findViewById(R.id.college_text);
        interest_text = (TextView) findViewById(R.id.interest_text);
        app_name = (TextView) findViewById(R.id.app_name);
        search_box = (TextView) findViewById(R.id.search_box);
        search_button = (ImageButton) findViewById(R.id.do_search_button);
        //course_top = (TextView) findViewById(R.id.course_top);
        //book_top = (TextView) findViewById(R.id.book_top);
        //document_top = (TextView) findViewById(R.id.document_top);
        banner = (Banner) findViewById(R.id.banner);
        // user_management_list = (ListView) findViewById(R.id.user_management_list);

        mQueue = Volley.newRequestQueue(MainActivity.this);

    }

    private void setMainPage(){
        // ------------设置搜索栏前面的下拉菜单------------------
        //数据
        data_list = new ArrayList<String>();
        data_list.add("课程");
        data_list.add("书籍");
        data_list.add("资料");

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        // 监听
        search = (TextView) findViewById(R.id.search) ;
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                search.setText( data_list.get(arg2) );//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                search.setText("Nothing");
            }
        });

        // ------------------设置主页的搜索功能--------------
        setSearchFun();
        //-------------------设置热门-----------
        setHot();
    }

    private void getUser(){
        // ---------------得到user-----------------
        user.setName("我最帅");
        user.setPoint(200);
        user.setPhone("13061765432");
    }

    // ------------------设置主页的搜索功能--------------
    private void setSearchFun() {

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到当前搜索类别
                String type = search.getText().toString();
                // 跳转到结果界面进行搜索
                Intent intent = new Intent(MainActivity.this,SearchResultActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("type",type);
                intent.putExtra("search_content",search_box.getText().toString());
                Log.e("##", "传递"+type+"  "+search_box.getText().toString());
                startActivity(intent);
            }
        });

    }

    //-------------------设置热门-----------
    private void setHot() {
        // 设置主页热门top
        // 与服务器交互
        String url1 = "http://47.100.226.176:8080/XueBaJun/GetTopOneDocument";

        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url1,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Document temp = gson.fromJson(jsonObject.toString(), Document.class);
                // 更新UI
                if(temp != null) {
                    Log.d("##getSuccess##", "name"+temp.getName()+ "\n");
                    //document_top.setText(temp.getName());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);

        String url2 = "http://47.100.226.176:8080/XueBaJun/GetTopOneCourse";

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(url2,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Course temp = gson.fromJson(jsonObject.toString(), Course.class);
                // 更新UI
                if(temp != null) {
                    Log.d("##getSuccess##", "name"+temp.getName()+ "\n");
                    //course_top.setText(temp.getName());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest2);

        String url3 = "http://47.100.226.176:8080/XueBaJun/GetTopOneBook";

        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(url3,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Book temp = gson.fromJson(jsonObject.toString(), Book.class);
                // 更新UI
                if(temp != null) {
                    Log.d("##getSuccess##", "name"+temp.getName()+ "\n");
                    //book_top.setText(temp.getName());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest3);


        // 设置图片
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<String> images = new ArrayList<>();
        images.add("http://47.100.226.176:8080/top_image/top_book.jpg");
        images.add("http://47.100.226.176:8080/top_image/top_course.jpg");
        images.add("http://47.100.226.176:8080/top_image/top_document.jpg");
        banner.setImages(images);
        // 轮播间隔8秒
        banner.setDelayTime(8000);
        // 设置点击监听
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if(position == 0){
                    // 展示TOP1的书籍
                    Log.e("##","你点击了图片0");
                }else if(position == 1){
                    // 展示TOP1的课程
                    Log.e("##","你点击了图片1");
                }else {
                    // 展示TOP1的资料
                    Log.e("##","你点击了图片2");
                }
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    // ---------------设置用户信息边栏------------------
    private void setLeftDrawable(){

        // 如果没有登陆，不能打开个人信息边栏
        if(user == null){
            main_drawable.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        // 设置边栏弹出效果，没有登陆会提示对话框进行跳转
        user_management_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user == null){
                    showSignInDialog();
                }else{

                    // 设置用户信息栏内容和跳转
                    setHeadImage();

                    setLeftDrawableInfo();

                    setLeftDrawableJump();

                    main_drawable.openDrawer(GravityCompat.START);
                }
            }
        });

        // 设置签到功能
        check_in_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                // 设置点击后UI
                check_in_button.setText("已签到");
                check_in_button.setEnabled(false);
                // 与服务器交互
                String url = "http://47.100.226.176:8080/XueBaJun/CheckIn";

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone",user.getPhone());
                    jsonObject.put("point" ,String.valueOf(user.getPoint()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject jsonObject) {
                        User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);
                        // 签到成功，更新user的积分值并修改UI显示
                        if(tempuser != null) {
                            Log.d("##getSuccess##", "name"+tempuser.getPhone()+ "\n");
                            user.setPoint(tempuser.getPoint());
                            point_text.setText(String.valueOf(user.getPoint()));
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

    // 设置边栏中的选项跳转
    private void setLeftDrawableJump(){

        change_info_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChangeInfoActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
        uploade_doc_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UploadFileActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
        my_concern_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyConcernActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
        my_collect_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyCollectActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
        about_me_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AboutMeActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
    }

    // 设置用户的其他信息
    private void setLeftDrawableInfo(){
        // 设置用户的其他信息
        name_text.setText(user.getName());
        point_text.setText(String.valueOf(user.getPoint()));
        grade_text.setText(user.getGrade());
        college_text.setText(user.getCollege());
        StringBuffer interest_string = new StringBuffer();
        if(user.isArt()){
            interest_string.insert(interest_string.length(),"艺 ");
        }
        if(user.isAgriculture()){
            interest_string.insert(interest_string.length(),"农 ");
        }
        if(user.isManagement()){
            interest_string.insert(interest_string.length(),"管 ");
        }
        if(user.isHumanity()){
            interest_string.insert(interest_string.length(),"文 ");
        }
        if(user.isTechnology()){
            interest_string.insert(interest_string.length(),"理");
        }
        if(user.isPlay()){
            interest_string.insert(interest_string.length(),"乐 ");
        }
        if(user.isMedicine()){
            interest_string.insert(interest_string.length(),"医 ");
        }
        interest_text.setText(interest_string);
    }

    // 请先登录对话框
    private void showSignInDialog(){
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.icon_personal_message)//设置标题的图片
                .setTitle("未登录")// 设置对话框的标题
                .setMessage("请先登录")//设置对话框的内容 //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); } })
                .setPositiveButton("好的，去登陆", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,ChangeInfoActivity.class);
                        startActivity(intent);
                    } })
                .create();
        dialog.show();
    }

    // 设置头像
    private void setHeadImage(){
        // 请求用户对应头像，如果没有头像就使用默认头像
        ImageRequest imageRequest = new ImageRequest(
                "http://47.100.226.176:8080/XueBaJun/head_image/"+user.getPhone()+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        head_image_button.setBackground(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                head_image_button.setBackgroundResource(R.drawable.ic_head_image);
            }
        });
        mQueue.add(imageRequest);
    }

    // 再返回时刷新页面边栏的信息
    public void onResume() {
        super.onResume();
        setHeadImage();
        setLeftDrawableInfo();

    }
}
