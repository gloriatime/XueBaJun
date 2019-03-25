package com.example.gloria.myapplication.Recommend;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.showInfo.Activity_Top20_course;
import com.example.gloria.myapplication.showInfo.CourseDetailActivity;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.Professor;
import com.example.model.myapplication.ProfessorCourse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reco_course extends Activity {
    private ListView list_re_C;
    private RecommandAdapter mAdapter;
   // private final ArrayList<Course> data = new ArrayList<>();
    //以上为列表内容
    private TextView morecourse;
    private TextView T_C_one;
    private TextView T_C_two;
    private TextView T_C_three;
    private Course course;
    RequestQueue mQueue;
    //搜索框
    private SearchView searchView;
    private ListView listView;
    private final String[] strings = {"数据结构","C++","JAVA"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_course);
        getCourse();
       //设置可能喜欢列表
        init();
        setListView();
        //setOnclick();
        //设置搜索框
        initsearch();
    }

    private void initsearch() {
        listView = (ListView)findViewById(R.id.lv);
        searchView = (SearchView)findViewById(R.id.searchEdit);
       //设置适配器
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strings));
        //listView启动过滤
        listView.setTextFilterEnabled(true);
        //设置一开始不显示listview
        listView.setVisibility(View.GONE);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入课程名或教师名");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                Toast.makeText(Reco_course.this,"数据结构",Toast.LENGTH_SHORT).show();
            return false;}
            @Override
            public boolean onQueryTextChange(String newText){
                if(TextUtils.isEmpty(newText)){
                   listView.setVisibility(View.GONE);
                   listView.clearTextFilter();
                }
                else{
                    listView.setVisibility(View.VISIBLE);
                    listView.setFilterText(newText);
                }
                return true;
            }
        });
    }

    private void init() {
        morecourse = (TextView) findViewById(R.id.morecourse);
        morecourse.setOnClickListener(new morec());
        List<Course> Clist = course.getRecommendList();
        Course cone = Clist.get(0);
        Course ctwo = Clist.get(1);
        Course cthree = Clist.get(2);
        List<ProfessorCourse> Plist = course.getProfessorCourseList();
        Professor pone = Plist.get(0).getProfessor();
        Professor ptwo = Plist.get(1).getProfessor();
        Professor pthree = Plist.get(2).getProfessor();
        T_C_one = (TextView)findViewById(R.id.T_C_one);
        String a = "1."+cone.getName()+"------"+pone.getName();
        T_C_one.setText(a);
        T_C_two = (TextView)findViewById(R.id.T_C_two);
        String b = "2."+ctwo.getName()+"------"+ptwo.getName();
        T_C_two.setText(b);
        T_C_three = (TextView)findViewById(R.id.T_C_three);
        String c = "3."+cthree.getName()+"------"+pthree.getName();
        T_C_three.setText(c);
        list_re_C = (ListView)findViewById(R.id.ListView_like);
        mQueue = Volley.newRequestQueue(Reco_course.this);
        //开始设置搜索框
      //  searchView = (SearchView) findViewById(R.id.searchEdit);
    }

    private void getCourse() {
            // 与服务器交互
            String url = "http://47.100.226.176:8080/XueBaJun/GetCourse";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id",1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

                public void onResponse(JSONObject jsonObject) {
                    Gson gson = new DateGson().getGson();
                    Course tempuser = gson.fromJson(jsonObject.toString(), Course.class);
                    // 签到成功，更新user的积分值并修改UI显示
                    course = tempuser;//从后台请求第一个course成功

                    if(tempuser != null) {
                        Log.e("##getSuccess##", "Id"+tempuser.getName()+ "\n");
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
            Intent intent = new Intent(Reco_course.this, Activity_Top20_course.class);
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
                Intent intent = new Intent();
                intent.setClass(Reco_course.this, CourseDetailActivity.class);
                Bundle bundle = new Bundle();
                List<Course> courseList = course.getRecommendList();
                Course showcourse = courseList.get(i);
                bundle.putString("courseone",showcourse.getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }
    //设置数据的显示显示数据
    private ArrayList<HashMap<String ,Object>> getData(){
        ArrayList<HashMap<String ,Object>> listItem = new ArrayList<>();
        List<Course> Clist = course.getRecommendList();
        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
        for(int i = 0; i < Clist.size(); i++){
            HashMap<String, Object> map = new HashMap<>();
            Book book = Clist.get(i).getBook();
           map.put("coursecover",book.getCover());
           map.put("cname",Clist.get(i).getName());
           List<ProfessorCourse> professorlist = course.getProfessorCourseList();
           ProfessorCourse pcourse = professorlist.get(0);
           Professor teacher = pcourse.getProfessor();
           map.put("tname",teacher.getName());
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
            return course.getRecommendList().size();
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
            holder.tname.setText("教师："+getData().get(position).get("tname").toString());

            NetImage netImage = new NetImage();
            netImage.setCoverImage(mQueue,holder.coursecover,"http://47.100.226.176:8080/"+getData().get(position).get("coursecover").toString());

            return convertView;
        }

    }

}