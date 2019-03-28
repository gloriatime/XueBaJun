package com.example.gloria.myapplication.Recommend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.ListItemViewHolderCAndB;
import com.example.base.myapplication.NetImage;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.gloria.myapplication.showInfo.Activity_Top20_book;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reco_book extends AppCompatActivity {
    private ListView list_re_B;
    private RecommandAdapter mAdapter;
    //以上为列表内容
    private TextView morebook;
    private TextView B_one;
    private TextView B_two;
    private TextView B_three;
    private Book book;
    RequestQueue mQueue;
    //列表按钮
    private Button latest, maylike;
    //搜索框
    private SearchView searchView;
    private ListView listView;
    private final String[] strings = {"数据结构","C++","JAVA","操作系统"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_book);
        init();
        getBook();
        setPage();
        //设置可能喜欢列表
        setListView();
        //设置搜索框
        setSearch();
        setOnclickFun();
    }

    private void setSearch()  {
        //设置适配器
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strings));
        //listView启动过滤
        listView.setTextFilterEnabled(true);
        //设置一开始不显示listview
        listView.setVisibility(View.GONE);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入书籍名称");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                Toast.makeText(Reco_book.this,"数据结构",Toast.LENGTH_SHORT).show();
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
        morebook = (TextView)findViewById(R.id.morebook);
        latest =(Button) findViewById(R.id.latest);
        maylike =(Button) findViewById(R.id.likebook);
        B_one = (TextView)findViewById(R.id.B_one);
        B_two = (TextView)findViewById(R.id.B_two);
        B_three = (TextView)findViewById(R.id.B_three);
       // String a = "1."+Blist.get(0).getName()+"------"+Blist.get(0).getName();
        //T_C_one.setText(a);
        //String b = "2."+Blist.get(1).getName()+"------"+Blist.get(1).getName();
        //T_C_two.setText(b);
      //  String c = "3."+Blist.get(2).getName()+"------"+Blist.get(2).getName();
       // T_C_three.setText(c);
        list_re_B = (ListView)findViewById(R.id.ListView_like_book);
        //开始设置搜索框
        searchView = (SearchView) findViewById(R.id.searchEditBook);
        listView = (ListView)findViewById(R.id.lvb);
        book = new Book();
        mQueue = Volley.newRequestQueue(Reco_book.this);
    }
    private void setPage() {
        morebook.setOnClickListener(new moreb());
    }

    private void getBook() {
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetRecommendListOfBookPage";

        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Book tempuser = gson.fromJson(jsonObject.toString(), Book.class);
                // 签到成功，更新user的积分值并修改UI显示
                book = tempuser;//从后台请求第一个course成功

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
    class moreb implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Reco_book.this, Activity_Top20_book.class);
            startActivity(intent);
        }
    }
    private void setListView()//设置列表的点击事件
    {
        mAdapter = new RecommandAdapter(this);//得到自定义的RecommandAdapter对象
        list_re_B.setAdapter(mAdapter);//为ListView绑定Adapter
        /*为lsit添加点击事件*/
        list_re_B.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("##", "你点击了ListView条目" + i);//在LogCat中输出信息
                // ---显示点击之后的页面
                Intent intent = new Intent();
                intent.setClass(Reco_book.this, BookMainActivity.class);
                Bundle bundle = new Bundle();
                List<Book> bookList = book.getRecommendList();
                Book showbook = bookList.get(i);
                bundle.putString("bookone",showbook.getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    //设置数据的显示显示数据
    private ArrayList<HashMap<String ,Object>> getData(){
        ArrayList<HashMap<String ,Object>> listItem = new ArrayList<>();
        List<Book> Blist = new ArrayList<>();
            Blist = book.getRecommendList();
        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
       if(Blist ==null) return listItem;
        for(int i = 0; i <  Blist.size(); i++){
            HashMap<String, Object> map = new HashMap<>();
            //设置封面
            map.put("bookcover",Blist.get(i).getCover());
            map.put("bname",Blist.get(i).getName());
            map.put("author",Blist.get(i).getAuthor());
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
            String a = "书籍："+getData().get(position).get("bname").toString();
            String b = "作者："+getData().get(position).get("author").toString();
            holder.cname.setText(a);
            holder.tname.setText(b);

            NetImage netImage = new NetImage();
            netImage.setCoverImage(mQueue,holder.coursecover,"http://47.100.226.176:8080/"+getData().get(position).get("bookcover").toString());

            return convertView;
        }

    }
    private void setOnclickFun(){
        latest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                latest.setBackgroundColor(getResources().getColor(R.color.blue));
                maylike.setBackgroundColor(getResources().getColor(R.color.white));
                //使用list展示最新上架
                latest.setEnabled(false);
                maylike.setEnabled(true);
            }
        });
        maylike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //设置被点击的UI效果
                latest.setBackgroundColor(getResources().getColor(R.color.white));
                maylike.setBackgroundColor(getResources().getColor(R.color.blue));
                latest.setEnabled(true);
                maylike.setEnabled(false);
            }
        });

    }
}
