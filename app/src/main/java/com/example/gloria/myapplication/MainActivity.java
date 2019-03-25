package com.example.gloria.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.base.myapplication.ListItemViewHolder;
import com.example.base.myapplication.ListViewForMainPage;
import com.example.base.myapplication.UnfinishDialog;
import com.example.gloria.myapplication.Recommend.Reco_book;
import com.example.gloria.myapplication.SignIn.SignInActivity;
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.gloria.myapplication.manage.AboutMeActivity;
import com.example.gloria.myapplication.manage.ChangeInfoActivity;
import com.example.gloria.myapplication.manage.MyCollectActivity;
import com.example.gloria.myapplication.manage.MyConcernActivity;
import com.example.gloria.myapplication.manage.UploadFileActivity;
import com.example.gloria.myapplication.paper.DataMainActivity;
import com.example.gloria.myapplication.search.SearchResultActivity;
import com.example.gloria.myapplication.searchPaper.PaperDetailMainActivity;
import com.example.gloria.myapplication.showInfo.CourseDetailActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.example.model.myapplication.UserTag;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

    // 中心模块跳转
    ImageButton paper_button,course_button,book_button,square_button;

    // 推荐列表
    List<Document> documents;
    List<Course> courses;
    List<Book> books;
    ListView document_list_view,course_list_view,book_list_view;
    RecommendAdapter mAdapter_document,mAdapter_course,mAdapter_book;// 本页面list的适配器
    private int DOCUMENT=1,BOOK=2,COURSE=3;

    User user;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        getUser();

        setMainPage();

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
        document_list_view = (ListView) findViewById(R.id.document_list_view);
        course_list_view = (ListView) findViewById(R.id.course_list_view);
        book_list_view = (ListView) findViewById(R.id.book_list_view);
        // user_management_list = (ListView) findViewById(R.id.user_management_list);
        paper_button = (ImageButton)findViewById(R.id.paper);
        course_button = (ImageButton)findViewById(R.id.course);
        book_button = (ImageButton)findViewById(R.id.book);
        square_button = (ImageButton)findViewById(R.id.square);

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
        // ------------------设置推荐列表------------------
        setRecommendList();
        //--------------设置模块跳转---------
        setModelJump();
    }


    private void getUser(){
        // ---------------得到user-----------------
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.e("##","phone"+user.getPhone());
        //Log.e("##","college"+user.getCollege());
        Log.e("##","pwd"+user.getName());
    }

    // ------------------设置主页的搜索功能--------------
    private void setSearchFun() {

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到当前搜索类别
                String type = search.getText().toString();
                // 跳转到结果界面进行搜索
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("type", type);
                intent.putExtra("search_content", search_box.getText().toString());
                Log.e("##", "传递" + type + "  " + search_box.getText().toString());
                startActivity(intent);
            }
        });
    }
    //---------设置模块跳转----------
    private void setModelJump(){
        paper_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DataMainActivity.class);
                intent.putExtra("user",(Serializable) user);
                startActivity(intent);
            }
        });
        course_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Reco_book.class);
                intent.putExtra("user",(Serializable) user);
                startActivity(intent);
            }
        });
        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Reco_book.class);
                intent.putExtra("user",(Serializable) user);
                startActivity(intent);
            }
        });
        square_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹出警告对话框
                showUnfinishDialog();
            }
        });
    }
    private void showUnfinishDialog(){
        UnfinishDialog u = new UnfinishDialog(this);
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

    // ---------------设置推荐列表------------------
    private void setRecommendList() {
        // 先和后台交互得到推荐内容
        String url = "http://47.100.226.176:8080/XueBaJun/GetRecommendListOfMainPage";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",user.getPhone());
            jsonObject.put("medicine" ,String.valueOf(user.isMedicine()));
            //Log.e("##","String.valueOf(user.isTech())"+String.valueOf(user.isTechnology()));
            jsonObject.put("technology" ,String.valueOf(user.isTechnology()));
            jsonObject.put("art" ,String.valueOf(user.isArt()));
            jsonObject.put("agriculture" ,String.valueOf(user.isAgriculture()));
            jsonObject.put("management" ,String.valueOf(user.isManagement()));
            jsonObject.put("humanity" ,String.valueOf(user.isHumanity()));
            jsonObject.put("play" ,String.valueOf(user.isPlay()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                UserTag temp = new Gson().fromJson(jsonObject.toString(), UserTag.class);
                // 签到成功，更新user的积分值并修改UI显示
                if(temp != null) {
                    Log.e("##getSuccess##", "推荐列表返回"+jsonObject.toString());
                    // 设置列表
                    documents = temp.getRecommendDocumentList();
                    courses = temp.getRecommendCourseList();
                    books = temp.getRecommendBookList();
                    // 更新UI
                    showRecommendList();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void showRecommendList() {
        mAdapter_document = new RecommendAdapter(this,DOCUMENT);
        document_list_view.setAdapter(mAdapter_document);
        setListViewHeightBasedOnChildren(document_list_view);
        document_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("##", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                // ---------------------------跳转到对应的信息展示界面，目前先空着--------------------------------------
                Intent intent = new Intent(MainActivity.this, PaperDetailMainActivity.class);
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("document_id", documents.get(arg2).getId());
                startActivity(intent);
            }
        });

        mAdapter_course = new RecommendAdapter(this,COURSE);
        course_list_view.setAdapter(mAdapter_course);
        setListViewHeightBasedOnChildren(course_list_view);
        course_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("##", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                // ---------------------------跳转到对应的信息展示界面，目前先空着--------------------------------------
                Intent intent = new Intent(MainActivity.this, CourseDetailActivity.class);
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("course_id", courses.get(arg2).getId());
                startActivity(intent);
            }
        });

        mAdapter_book = new RecommendAdapter(this,BOOK);
        book_list_view.setAdapter(mAdapter_book);
        setListViewHeightBasedOnChildren(book_list_view);
        book_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("##", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                // ---------------------------跳转到对应的信息展示界面，目前先空着--------------------------------------
                Intent intent = new Intent(MainActivity.this, BookMainActivity.class);
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("book_id", books.get(arg2).getId());
                startActivity(intent);
            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        RecommendAdapter listAdapter = (RecommendAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    // ---------------设置用户信息边栏------------------
    private void setLeftDrawable(){

        //Log.e("###","user存不存在"+user);
        // 如果没有登陆，不能打开个人信息边栏
        if(user == null){
            main_drawable.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        // 设置边栏弹出效果，没有登陆会提示对话框进行跳转
        user_management_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(user == null){
                    //showSignInDialog();
               // }else{

                    // 设置用户信息栏内容和跳转
                    setHeadImage();

                    setLeftDrawableInfo();

                    setLeftDrawableJump();

                    main_drawable.openDrawer(GravityCompat.START);
               // }
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
/*
 */ // 未登录用户可见主页修改为必须先登陆
    // 请先登录对话框
    /*
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
                        /**8
                         * 登录注册
                         *
                         *

                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent);*/
                  /* } })
                .create();
        dialog.show();
    }*/

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

    private ArrayList<HashMap<String, Object>> getData(int listContent){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息

        if(listContent == DOCUMENT){
            Log.e("##", "此时doc列表为" +documents.size());//在LogCat中输出信息
            for (int i = 0; i < documents.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("text_1", documents.get(i).getName());
                map.put("text_2", "上传by："+documents.get(i).getUp_user());
                map.put("text_3", "上传时间："+documents.get(i).getUp_time());
                listItem.add(map);
            }
        }else if(listContent == BOOK){
            Log.e("##", "此时book列表为" +books.size());//在LogCat中输出信息
            for (int i = 0; i < books.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("text_1", books.get(i).getName());
                map.put("text_3", "作者："+books.get(i).getAuthor());
                map.put("text_2", "");

                listItem.add(map);

            }
        }else if(listContent == COURSE){
            Log.e("##", "此时course列表为"+courses.size() );//在LogCat中输出信息
            for (int i = 0; i < courses.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("text_1", courses.get(i).getName());
                map.put("text_2", "");
                map.put("text_3", "");
                listItem.add(map);

            }
        }
        return listItem;
    }

    // list的适配器
    private class RecommendAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int listContent;


        RecommendAdapter(Context context, int listContent) {
            this.mInflater = LayoutInflater.from(context);
            this.listContent = listContent;
        }

        @Override
        public int getCount() {
            return getData(listContent).size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ListItemViewHolder holder;

            Log.e("##", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_recommend_document_for_main_page,null);
                holder = new ListItemViewHolder();

                holder.name = (TextView) convertView.findViewById(R.id.text_1);
                holder.content = (TextView) convertView.findViewById(R.id.text_2);
                holder.author = (TextView) convertView.findViewById(R.id.text_3);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.name.setText(getData(listContent).get(position).get("text_1").toString());
            holder.content.setText(getData(listContent).get(position).get("text_2").toString());
            holder.author.setText(getData(listContent).get(position).get("text_3").toString());

            return convertView;
        }

    }
}
