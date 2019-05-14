package com.example.gloria.myapplication.showInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.BackJump;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.ListItemViewHolder;
import com.example.base.myapplication.NetImage;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.manage.MyConcernActivity;
import com.example.gloria.myapplication.searchPaper.PaperDetailMainActivity;
import com.example.model.myapplication.CollectCourse;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gloria.myapplication.R;

public class UserInfoActivity extends AppCompatActivity {

    // 上方导航栏
    private ImageButton back_button;

    // 用户信息部分
    private ImageView head_image;
    private TextView name_text,college_text;
    private Button concern_button;

    // 用户动态、上传部分
    private Button document_button,event_button;
    private ListView list;

    private User user;// 登陆的用户
    private User userInfo;// 显示的用户
    private ArrayList<Course> courses= new ArrayList<>();
    private ArrayList<Document> uploaded = new ArrayList<>();
    private RequestQueue mQueue;
    ListAdapter mAdapter;// 本页面list的适配器

    private int EVENT = 1;// 用来确定list的显示内容的常量
    private int DOCUMENT = 2;// 用来确定list的显示内容的常量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
        getUser();
        getUserInfo();

    }

    private void init() {
        back_button = (ImageButton) findViewById(R.id.back_button);

        head_image = (ImageView) findViewById(R.id.head_image);
        name_text = (TextView) findViewById(R.id.name_text);
        college_text = (TextView) findViewById(R.id.college_text);
        concern_button = (Button) findViewById(R.id.concern_button);

        document_button = (Button) findViewById(R.id.document_button);
        event_button = (Button) findViewById(R.id.event_button);
        list = (ListView) findViewById(R.id.list);

        mQueue  = Volley.newRequestQueue(UserInfoActivity.this);
    }

    private void getUser(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.e("##跳转到的用户：", user.getPhone());
    }

    private void getUserInfo() {

        Intent intent = getIntent();
        userInfo = (User) intent.getSerializableExtra("user_info");
        Log.e("##user_info", userInfo.getPhone());
        Log.e("##user", userInfo.getPhone());
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone",userInfo.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetUser";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                userInfo = new Gson().fromJson(jsonObject.toString(), User.class);
                setPage();
                // 预先显示ta的动态
                event_button.performClick();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void setPage() {
        setBackJump();
        setUserInfo();
        setButtonFun();
    }

    public void setBackJump(){
        back_button= (ImageButton) findViewById(R.id.back_button);
        BackJump bj = new BackJump();
        bj.setBack(back_button);
    }

    private void setUserInfo() {
        name_text.setText(userInfo.getName());
        college_text.setText(userInfo.getCollege());
        NetImage image = new NetImage();
        String url = "http://47.100.226.176:8080/XueBaJun/head_image/"+userInfo.getPhone()+".jpg";
        image.setHeadImage(mQueue, head_image,url);
    }

    private void setButtonFun() {
        concern_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                concern_button.setText("已关注");

                HashMap<String,String> u = new HashMap<>();
                HashMap<String,String> c = new HashMap<>();
                u.put("phone",user.getPhone());
                c.put("phone",userInfo.getPhone());
                Map<String, Object> map = new HashMap<>();
                map.put("user",u);
                map.put("my_concern",c);
                org.json.JSONObject jsonObject = new org.json.JSONObject(map);

                // 与服务器交互得到我收藏的资料列表
                String url = "http://47.100.226.176:8080/XueBaJun/AddConcern";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

                    public void onResponse(org.json.JSONObject jsonObject) {
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        });

        event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_button.setBackground(getResources().getDrawable(R.drawable.bg_fillet_left_corner));
                document_button.setBackground(getResources().getDrawable(R.drawable.bg_fillet_right_corner));
                updateUserDynamic();
                setListView(EVENT);
            }
        });

        document_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_button.setBackground(getResources().getDrawable(R.drawable.bg_fillet_left_corner_white));
                document_button.setBackground(getResources().getDrawable(R.drawable.bg_fillet_right_corner_blue));
                updateUserUpload();
                setListView(DOCUMENT);
            }
        });
    }

    private void updateUserUpload() {
        // 清理原来的值
        uploaded.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", userInfo.getPhone());
            jsonObject.put("name", userInfo.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyDocument";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {

                Gson gson = new DateGson().getGson();

                User tempuser = gson.fromJson(jsonObject.toString(), User.class);
                Log.e("##", jsonObject.toString());
                if (tempuser != null) {
                    Log.e("##", "我上传的资料已返回");
                    List<Document> MyDocuments = tempuser.getMyDocument();

                    for (Document d : MyDocuments) {
                        uploaded.add(d);
                        Log.e("##", "Document's name" + d.getName()+ "Id: "+d.getId());
                    }
                    // 先执行内部类，后执行外部类
                    // setAdapter需要放在数据刷新成功之后。
                    list.setAdapter(mAdapter);//为ListView绑定Adapter
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void updateUserDynamic(){

        // 清理原来的值
        courses.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", userInfo.getPhone());
            //jsonObject.put("name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到我收藏的资料列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetMyCollectedCourses";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);

                if (tempuser != null) {
                    Log.e("##", "我收藏的课程已返回");
                    List<CollectCourse> MyCollectedCourses = tempuser.getCollected_courses();

                    for (CollectCourse c : MyCollectedCourses) {
                        Course d = c.getCourse();
                        courses.add(d);
                        Log.e("##", "Course's score" + d.getScore());
                    }
                    // 先执行内部类，后执行外部类
                    // setAdapter需要放在数据刷新成功之后。
                    list.setAdapter(mAdapter);//为ListView绑定Adapter
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);

    }

    private void setListView(final int listContent){
        mAdapter = new ListAdapter(this,listContent);//得到一个自定义的ListAdapter对象
        //listView.setAdapter(mAdapter);//为ListView绑定Adapter
        /*为ListView添加点击事件*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("##", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                if(listContent == EVENT){
                    // ---------------------------跳转到课程介绍界面-------------------------------------
                    Intent intent = new Intent(UserInfoActivity.this, CourseDetailActivity.class);
                    intent.putExtra("user",(Serializable) user);
                    intent.putExtra("course_id",courses.get(arg2).getId());
                    startActivity(intent);
                }else if(listContent == DOCUMENT){
                    // ---------------------------跳转到资料介绍界面-------------------------------------
                    Intent intent = new Intent(UserInfoActivity.this, PaperDetailMainActivity.class);
                    intent.putExtra("user",(Serializable) user);
                    intent.putExtra("document_id",uploaded.get(arg2).getId());
                    startActivity(intent);
                }

            }
        });
    }

    private ArrayList<HashMap<String, Object>> getDate(int listContent){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
       // Log.e("##", "此时列表为" + courses.size());//在LogCat中输出信息
        if(listContent == EVENT){
            for (int i = 0; i < courses.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", courses.get(i).getName());
                map.put("intro", courses.get(i).getIntro());
                map.put("score", courses.get(i).getScore());
                map.put("comment", courses.get(i).getComment());
                listItem.add(map);
            }
        }else if(listContent == DOCUMENT){
            for (int i = 0; i < uploaded.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", uploaded.get(i).getName());
                map.put("time", uploaded.get(i).getUp_time());
                map.put("score", uploaded.get(i).getScore());
                map.put("comment", uploaded.get(i).getComment());
                listItem.add(map);
            }
        }


        return listItem;

    }

    private class ListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int listContent;


        ListAdapter(Context context, int listContent) {
            this.mInflater = LayoutInflater.from(context);
            this.listContent = listContent;
        }

        @Override
        public int getCount() {
            return getDate(listContent).size();//返回数组的长度
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

            // 如果是他的动态
            if(listContent == EVENT){

                ListItemViewHolder holder;

                Log.e("##", "getView " + position + " " + convertView);
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_item_user_dynamic,null);
                    holder = new ListItemViewHolder();

                    holder.name = (TextView) convertView.findViewById(R.id.dynamic_text);
                    holder.head = (ImageView) convertView.findViewById(R.id.head_image);
                    holder.course_name = (TextView) convertView.findViewById(R.id.course_name_text);
                    holder.content = (TextView) convertView.findViewById(R.id.course_intro_text);
                    holder.comment_num = (TextView) convertView.findViewById(R.id.comment_number);
                    holder.score = (TextView) convertView.findViewById(R.id.score_number);

                    convertView.setTag(holder);//绑定ViewHolder对象

                    holder.name.setText(userInfo.getName()+"收藏了课程");
                    NetImage image = new NetImage();
                    String url = "http://47.100.226.176:8080/XueBaJun/head_image/"+userInfo.getPhone()+".jpg";
                    image.setHeadImage(mQueue, holder.head ,url);
                    holder.course_name.setText(getDate(listContent).get(position).get("name").toString());

                    if(getDate(listContent).get(position).get("intro")!=null){
                        holder.content.setText("简介："+getDate(listContent).get(position).get("intro").toString());
                    }else{
                        holder.content.setText("暂无简介");
                    }

                    holder.comment_num.setText(getDate(listContent).get(position).get("comment").toString());
                    holder.score.setText(getDate(listContent).get(position).get("score").toString());
                }
                else{
                    holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
                }

                // 为Button添加点击事件
                /*holder.button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.e("##", "你点击了按钮" + position);
                        // 弹出确认取消关注的对话框
                        //showAlertDialog(position);
                    }
                });*/

            }else if(listContent==DOCUMENT){

                ListItemViewHolder holder;

                Log.e("##", "getView " + position + " " + convertView);
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_item_user_dynamic,null);
                    holder = new ListItemViewHolder();

                    holder.name = (TextView) convertView.findViewById(R.id.dynamic_text);
                    holder.head = (ImageView) convertView.findViewById(R.id.head_image);
                    holder.course_name = (TextView) convertView.findViewById(R.id.course_name_text);
                    holder.content = (TextView) convertView.findViewById(R.id.course_intro_text);
                    holder.comment_num = (TextView) convertView.findViewById(R.id.comment_number);
                    holder.score = (TextView) convertView.findViewById(R.id.score_number);

                    convertView.setTag(holder);//绑定ViewHolder对象

                    holder.name.setText(userInfo.getName()+"上传了资料");
                    NetImage image = new NetImage();
                    String url = "http://47.100.226.176:8080/XueBaJun/head_image/"+userInfo.getPhone()+".jpg";
                    image.setHeadImage(mQueue, holder.head ,url);
                    holder.course_name.setText(getDate(listContent).get(position).get("name").toString());


                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
                    String date =format.format(getDate(listContent).get(position).get("time"));
                    holder.content.setText("上传时间："+date);

                    holder.comment_num.setText(getDate(listContent).get(position).get("comment").toString());
                    holder.score.setText(getDate(listContent).get(position).get("score").toString());
                }
                else{
                    holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
                }

            }

            return convertView;
        }

    }
}


