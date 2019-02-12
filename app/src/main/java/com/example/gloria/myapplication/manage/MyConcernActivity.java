package com.example.gloria.myapplication.manage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.ListItemViewHolder;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Concern;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyConcernActivity extends AppCompatActivity {

    private ListView listView;
    private Button my_concern_button,concern_me_button;// 我的关注按钮，关注我的按钮
    private User user;// 当前登陆用户
    private  final ArrayList<User> ConcernUser = new ArrayList<>(); // 存放我的关注列表中的用户的信息
    private int MYCONCERN = 1;// 用来确定list的显示内容的常量
    private int CONCERNME = 2;// 用来确定list的显示内容的常量
    RequestQueue  mQueue;
    ListAdapter mAdapter;// 本页面list的适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_concern);

        init();
        getUser();

        setOnclickFun();

        // 开始的时候默认显示我关注的人
        // 不知道为什么第一次点击会显示不出来
        my_concern_button.performClick();
    }

    // 得到从主页传递的已登陆用户信息
    private void getUser(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    // 绑定界面控件
    private void init(){
        user = null;
        my_concern_button = (Button) findViewById(R.id.my_concern_button);
        concern_me_button = (Button) findViewById(R.id.concern_mw_button);
        listView = (ListView) findViewById(R.id.list);
        mQueue = Volley.newRequestQueue(MyConcernActivity.this);
    }

    // 添加点击事件
    // 点击不同的按钮调用getData与数据库交互得到不同的User数组
    // 形成不同的适配器，使用同一个ListView显示不同的内容
    private void setOnclickFun(){
        my_concern_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 被点击的UI效果
                my_concern_button.setTextColor(getResources().getColor(R.color.white));
                my_concern_button.setBackgroundColor(getResources().getColor(R.color.red));
                concern_me_button.setTextColor(getResources().getColor(R.color.red));
                concern_me_button.setBackground(getResources().getDrawable(R.drawable.bg_stroke));
                // 使用list展示我关注的人
                updateUserList(MYCONCERN);
                setListView(MYCONCERN);
                // 按钮不能连续点击
                my_concern_button.setEnabled(false);
                concern_me_button.setEnabled(true);
            }
        });

        concern_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 被点击的UI效果
                concern_me_button.setTextColor(getResources().getColor(R.color.white));
                concern_me_button.setBackgroundColor(getResources().getColor(R.color.red));
                my_concern_button.setTextColor(getResources().getColor(R.color.red));
                my_concern_button.setBackground(getResources().getDrawable(R.drawable.bg_stroke));
                // 使用list展示关注我的人
                updateUserList(CONCERNME);
                setListView(CONCERNME);
                // 按钮不能连续点击
                concern_me_button.setEnabled(false);
                my_concern_button.setEnabled(true);
            }
        });
    }

    private void setListView(int listContent){
        mAdapter = new ListAdapter(this,listContent);//得到一个自定义的ListAdapter对象
        //listView.setAdapter(mAdapter);//为ListView绑定Adapter
        /*为ListView添加点击事件*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("##", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                // ---------------------------跳转到人物介绍界面，目前先空着--------------------------------------
            }
        });
    }
    // 准备展示的数据
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        /* 关注的人，关注我的人，最多100人， */

        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
        Log.e("##", "此时User列表为" + ConcernUser.size());//在LogCat中输出信息

        for (int i = 0; i < ConcernUser.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", ConcernUser.get(i).getName());
            map.put("head", getResources().getDrawable(R.drawable.icon_search));
            listItem.add(map);
        }

        return listItem;

    }

    // 与数据库交互更新用户列表
    private void updateUserList(int listContent){

        // 清理原来的值
        ConcernUser.clear();

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("point", String.valueOf(user.getPoint()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 如果是我的关注列表
        if(listContent == MYCONCERN){
            // 与服务器交互得到我的关注用户列表
            String url = "http://47.100.226.176:8080/XueBaJun/GetMyConcern";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {
                    User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);
                    // 根据user的我的关注列表，将关注的人的信息放入MyConcernUser
                    // 因为列表的值可能会更改，user不做更新
                    if (tempuser != null) {
                        Log.e("##", "我的关注已返回");
                        List<Concern> MyConcernList = tempuser.getMyconcernlist();

                        for (Concern c : MyConcernList) {
                            User u = new User(c.getMy_concern());
                            ConcernUser.add(u);
                            Log.e("##", "U's name" + u.getName());
                        }
                        // 先执行内部类，后执行外部类
                        // setAdapter需要放在数据刷新成功之后。
                        listView.setAdapter(mAdapter);//为ListView绑定Adapter
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(jsonObjectRequest);
        }else if(listContent == CONCERNME){
            // 与服务器交互得到我的关注用户列表
            String url = "http://47.100.226.176:8080/XueBaJun/GetConcernMe";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                public void onResponse(org.json.JSONObject jsonObject) {
                    User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);

                    if (tempuser != null) {
                        Log.e("##", "关注我的已返回");
                        List<Concern> ConcernMeList = tempuser.getConcern_me_list();

                        for (Concern c : ConcernMeList) {
                            User u = new User(c.getUser());
                            ConcernUser.add(u);
                            Log.e("##", "U's name" + u.getName());
                        }
                        // 先执行内部类，后执行外部类
                        // setAdapter需要放在数据刷新成功之后。
                        listView.setAdapter(mAdapter);//为ListView绑定Adapter
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

    // 点击List中的按钮会弹出的对话框
    private void showAlertDialog(final int position){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("确认要取消关注"+getDate().get(position).get("name").toString()+"么？")//设置对话框的标题
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ---------------------------与数据库交互取消关注某人-------------------------
                        // 到时候用得到的User列表和position值一对照~完美
                        // 嵌套json数据
                        HashMap<String,String> u = new HashMap<>();
                        HashMap<String,String> c = new HashMap<>();
                        u.put("phone",user.getPhone());
                        c.put("phone",ConcernUser.get(position).getPhone());
                        Map<String, Object> map = new HashMap<>();
                        map.put("user",u);
                        map.put("my_concern",c);
                        org.json.JSONObject jsonObject = new org.json.JSONObject(map);

                        // 与服务器交互
                        String url = "http://47.100.226.176:8080/XueBaJun/DeleteConcern";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<org.json.JSONObject>() {

                            public void onResponse(org.json.JSONObject jsonObject) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.e("##","突然的404闪了腰"+volleyError.toString());
                                byte[] htmlBodyBytes = volleyError.networkResponse.data;
                                Log.e("VolleyError body---->", new String(htmlBodyBytes), volleyError);
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                        // 更新显示的列表
                        updateUserList(MYCONCERN);
                        setListView(MYCONCERN);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
    // 为我的关注list准备自定义的adapter
    private class ListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int listContent;


        ListAdapter(Context context,int listContent) {
            this.mInflater = LayoutInflater.from(context);
            this.listContent = listContent;
        }

        @Override
        public int getCount() {
            return getDate().size();//返回数组的长度
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

            // 如果是我的关注列表
            if(listContent == MYCONCERN){

                ListItemViewHolder holder;

                Log.e("##", "getView " + position + " " + convertView);
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_item_my_concern,null);
                    holder = new ListItemViewHolder();

                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    holder.head = (ImageView) convertView.findViewById(R.id.head_image);
                    holder.button = (Button) convertView.findViewById(R.id.button);
                    convertView.setTag(holder);//绑定ViewHolder对象
                }
                else{
                    holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
                }

                holder.name.setText(getDate().get(position).get("name").toString());
                holder.head.setBackground((Drawable) getDate().get(position).get("head"));

                // 为Button添加点击事件
                holder.button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.e("##", "你点击了按钮" + position);
                        // 弹出确认取消关注的对话框
                        showAlertDialog(position);
                    }
                });

            // 如果是关注我的列表
            }else if(listContent==CONCERNME){

                ListItemViewHolder holder;

                Log.e("##", "getView " + position + " " + convertView);
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_item_concern_me,null);
                    holder = new ListItemViewHolder();

                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    holder.head = (ImageView) convertView.findViewById(R.id.head_image);
                    convertView.setTag(holder);//绑定ViewHolder对象
                }
                else{
                    holder = (ListItemViewHolder)convertView.getTag();//取出ViewHolder对象
                }

                holder.name.setText(getDate().get(position).get("name").toString());
                holder.head.setBackground((Drawable) getDate().get(position).get("head"));

            }

            return convertView;
        }

    }
}
