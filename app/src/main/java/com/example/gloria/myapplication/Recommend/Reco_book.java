package com.example.gloria.myapplication.Recommend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.ListItemViewHolder;
import com.example.base.myapplication.ListItemViewHolderCAndB;
import com.example.base.myapplication.NetImage;
import com.example.base.myapplication.UnfinishDialog;
import com.example.gloria.myapplication.MainActivity;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.gloria.myapplication.search.SearchResultActivity;
import com.example.gloria.myapplication.showInfo.Activity_Top20_book;
import com.example.gloria.myapplication.showInfo.Activity_Top20_course;
import com.example.gloria.myapplication.showInfo.CourseDetailActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reco_book extends AppCompatActivity {
    private ListView list_re_C;
    private RecommandAdapter mAdapter;
    //以上为列表内容
    private TextView morecourse;
    private TextView T_C_one;
    private TextView T_C_two;
    private TextView T_C_three;
    private Book course;
    RequestQueue mQueue;
    User user;
    //搜索框
    private SearchView searchView;
    private ListView listView;
    private final String[] strings = {"数据结构","C++","JAVA","操作系统"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_course);

        init();
        getUser();
        getCourse();
        getTop3();
        //setOnclick();
        //设置搜索框
        setSearch();
    }

    private void getUser() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    List<Book> courseTopList;
    private void getTop3() {
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetTopTwentyBook";

        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Book tempuser = gson.fromJson(jsonObject.toString(), Book.class);

                course = tempuser;//从后台请求第一个course成功

                courseTopList = course.getTopTwentyList();
                if(courseTopList == null){
                    T_C_one.setText("暂无");
                    T_C_two.setText("暂无");
                    T_C_three.setText("暂无");

                }else if(courseTopList.size() == 1){
                    T_C_one.setText(courseTopList.get(0).getName());
                    T_C_two.setText("暂无");
                    T_C_three.setText("暂无");

                }else if(courseTopList.size() == 2){
                    T_C_one.setText(courseTopList.get(0).getName());
                    T_C_two.setText(courseTopList.get(1).getName());
                    T_C_three.setText("暂无");

                }else if(courseTopList.size() == 3){
                    T_C_one.setText(courseTopList.get(0).getName());
                    T_C_two.setText(courseTopList.get(1).getName());
                    T_C_three.setText(courseTopList.get(2).getName());

                }

                T_C_one.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(courseTopList.size()>=1){
                            Intent intent = new Intent(Reco_book.this, BookMainActivity.class);
                            // 传递参数
                            intent.putExtra("user", (Serializable) user);
                            intent.putExtra("book_id",courseTopList.get(0).getId());
                            startActivity(intent);
                        }
                    }
                });

                T_C_two.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(courseTopList.size()>=2){
                            Intent intent = new Intent(Reco_book.this,BookMainActivity.class);
                            // 传递参数
                            intent.putExtra("user", (Serializable) user);
                            intent.putExtra("book_id",courseTopList.get(1).getId());
                            startActivity(intent);
                        }
                    }
                });

                T_C_three.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(courseTopList.size()>=3){
                            Intent intent = new Intent(Reco_book.this,BookMainActivity.class);
                            // 传递参数
                            intent.putExtra("user", (Serializable) user);
                            intent.putExtra("book_id",courseTopList.get(3).getId());
                            startActivity(intent);
                        }
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void setSearch() {
        //设置适配器
        //listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strings));
        //listView启动过滤
        //listView.setTextFilterEnabled(false);

        //设置一开始不显示listview
        //listView.setVisibility(View.GONE);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入书籍名");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(Reco_course.this,"数据结构",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Reco_book.this, SearchResultActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("type", "书籍");
                intent.putExtra("search_content", searchView.getQuery().toString());
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                if(TextUtils.isEmpty(newText)){
                    //listView.setVisibility(View.GONE);
                    //listView.clearTextFilter();
                }
                else{
                    //listView.setVisibility(View.GONE);
                    //listView.setFilterText(newText);
                }
                return true;
            }
        });
    }

    private void init() {
        morecourse = (TextView) findViewById(R.id.morecourse);

        T_C_one = (TextView)findViewById(R.id.T_C_one);

        T_C_two = (TextView)findViewById(R.id.T_C_two);

        T_C_three = (TextView)findViewById(R.id.T_C_three);

        list_re_C = (ListView)findViewById(R.id.ListView_like);
        //开始设置搜索框
        //listView = (ListView)findViewById(R.id.lv);
        searchView = (SearchView)findViewById(R.id.searchEdit);

        course = new Book();


        mQueue = Volley.newRequestQueue(Reco_book.this);

    }

    private void setPage(){
        morecourse.setOnClickListener(new morec());
    }

    private void setUnfinishDialog(){
        UnfinishDialog u = new UnfinishDialog(this);
    }

    private void getCourse() {
        // 与服务器交互

        String url = "http://47.100.226.176:8080/XueBaJun/GetRecommendListOfBookPage";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",user.getPhone());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Book tempuser = gson.fromJson(jsonObject.toString(), Book.class);
                // 签到成功，更新user的积分值并修改UI显示
                course = tempuser;//从后台请求第一个course成功

                setPage();
                //设置可能喜欢列表
                setListView();
                if(tempuser != null) {
                    Log.e("##getSuccess##", "课程推荐返回"+course.getRecommendList().size());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }
    //为更多课程添加点击事件
    class morec implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Reco_book.this, Activity_Top20_book.class);
            // 传递参数
            intent.putExtra("user", (Serializable) user);
            startActivity(intent);
        }
    }

    private void setListView()//设置列表的点击事件
    {
        mAdapter = new RecommandAdapter(this);//得到自定义的RecommandAdapter对象
        list_re_C.setAdapter(mAdapter);//为ListView绑定Adapter
        /*为lsit添加点击事件*/
        list_re_C.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("##", "你点击了ListView条目" + i);//在LogCat中输出信息
                // ---显示点击之后的页面
                Intent intent = new Intent(Reco_book.this,BookMainActivity.class);
                // 传递参数
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("book_id",course.getRecommendList().get(i).getId());
                startActivity(intent);
            }
        });
    }
    //设置数据的显示显示数据
    private ArrayList<HashMap<String ,Object>> getData(){
        if(course.getRecommendList()==null)
            Log.e("##","推荐列表是空的");
        ArrayList<HashMap<String ,Object>> listItem = new ArrayList<>();
        List<Book> Clist = new ArrayList<>();
        Clist = course.getRecommendList();
        Log.e("##", "书籍的推荐列表的长度"+String.valueOf(course.getRecommendList().size()));//在LogCat中输出信息
        if(Clist == null)
        {
            Log.e("##","推荐列表是空的，返回了");
            return listItem;}
        for(int i = 0; i < Clist.size(); i++){
            HashMap<String, Object> map = new HashMap<>();
            Log.e("##", "----------------添加内容**************************----------");
            Log.e("##","这是课程的名字"+Clist.get(i).getName());
            if(Clist.get(i).getCover()!=null)
                map.put("coursecover",Clist.get(i).getCover());
            else
            {
                @SuppressLint("ResourceType") ImageView cimage= (ImageView) findViewById(R.drawable.bookimgsample);
                map.put("coursecover",cimage);}
            map.put("cname",Clist.get(i).getName());
            if(Clist.get(i).getIntro()!=null){
                map.put("tname",Clist.get(i).getIntro().substring(0,20)+"...");
            }else {
                map.put("tname","暂无简介。");
            }

            listItem.add(map);
        }
        return listItem;
    }
    //定义一个可能喜欢的adapter
    private class RecommandAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        RecommandAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount()
        {
            return getData().size();
        }
        @Override
        public Object getItem(int position)
        {
            return null;
        }
        @Override
        public long getItemId(int position)
        {
            return 0;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ListItemViewHolderCAndB holder;
            Log.e("##", "getView " + position + " " + convertView);
            if(convertView == null)
            {
                Log.e("##","空的空的空的空的空的空的空的空的空的空的");
                convertView = mInflater.inflate(R.layout.list_maylike_course,null);
                holder = new ListItemViewHolderCAndB();
                holder.coursecover = (ImageView) convertView.findViewById(R.id.coursecover);
                holder.cname = (TextView) convertView.findViewById(R.id.cname);
                // holder.category = (TextView) convertView.findViewById(R.id.category);
                holder.tname = (TextView) convertView.findViewById(R.id.tname);
                convertView.setTag(holder);
            }
            else{
                holder = (ListItemViewHolderCAndB) convertView.getTag();
            }

            holder.cname.setText("课程："+getData().get(position).get("cname").toString());
            holder.tname.setText("简介："+getData().get(position).get("tname").toString());

            NetImage netImage = new NetImage();
            if(getData().get(position).get("coursecover")!=null) {
                netImage.setCoverImage(mQueue, holder.coursecover, "http://47.100.226.176:8080/" + getData().get(position).get("coursecover").toString());
            }else {
                holder.coursecover.setBackgroundResource(R.drawable.pic_no_data);
            }

            return convertView;
        }

    }

}