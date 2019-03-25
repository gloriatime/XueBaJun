package com.example.gloria.myapplication.showInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.ListItemViewHolderCAndB;
import com.example.base.myapplication.NetImage;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.Recommend.Reco_book;
import com.example.gloria.myapplication.bookDetail.BookMainActivity;
import com.example.model.myapplication.Book;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_Top20_book  extends AppCompatActivity {
    private ListView list_re_C;
    private RecommandAdapter mAdapter;
    //以上为列表内容
    private Book book;
    RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top20);
        getBook();
        //设置可能喜欢列表
        init();
        setListView();
    }
    private void init() {
        list_re_C = (ListView)findViewById(R.id.ListView_like);
        mQueue = Volley.newRequestQueue(Activity_Top20_book.this);
    }
    //从后台请求一本书籍
    private void getBook() {
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetBook";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                intent.setClass(Activity_Top20_book.this, BookMainActivity.class);
                Bundle bundle = new Bundle();
                List<Book> bookList = book.getRecommendList();
                bundle.putString("bookone",bookList.get(i).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    //设置数据的显示显示数据
    private ArrayList<HashMap<String ,Object>> getData(){
        ArrayList<HashMap<String ,Object>> listItem = new ArrayList<>();
        List<Book> Blist = book.getRecommendList();
        Log.e("##", "----------------开始为listItem添加内容----------");//在LogCat中输出信息
        for(int i = 0; i <  Blist.size(); i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("bookcover",Blist.get(i).getCover());
            map.put("bname",Blist.get(i).getName());
            map.put("author",Blist.get(i).getAuthor());
            listItem.add(map);
        }
        return listItem;
    }
    private class RecommandAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        RecommandAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount()
        {
            return book.getRecommendList().size();
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
}
