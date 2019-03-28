package com.example.gloria.myapplication.paper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.gloria.myapplication.MainActivity;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.search.SearchResultActivity;
import com.example.gloria.myapplication.searchPaper.PaperDetailMainActivity;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataMainActivity extends AppCompatActivity {

    private TextView reback;
    private EditText search_content;
    private TextView search;

    private ListView listViewLeft;
    private ListView listViewRight;
    List<Document> recommendLeft = new ArrayList<Document>();
    List<Document> recommendRight = new ArrayList<Document>();
    List<Document> recommendList = new ArrayList<Document>();
    FavoriteListAdapter adapterLeft;
    FavoriteListAdapter adapterRight;
    Document document;
    RequestQueue mQueue;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_main);

        reback = (TextView)findViewById(R.id.textViewBack);
        search_content = (EditText)findViewById(R.id.textViewSearchBar);
        search = (TextView)findViewById(R.id.textViewSearchIcon);

        //获得user
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        //返回
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataMainActivity.this, MainActivity.class);
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
        //搜索
        final String content = search_content.getText().toString().trim();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //***跳入“资料”详情界面
                 Intent intent = new Intent(DataMainActivity.this, SearchResultActivity.class);
                intent.putExtra("user", (Serializable) user);
                 intent.putExtra("type", "资料");
                 intent.putExtra("search_content",content);
                 startActivity(intent);
            }
        });

        //展示可能喜欢的内容
        listViewLeft = (ListView)findViewById(R.id.favorites_left);
        listViewRight = (ListView)findViewById(R.id.favorites_right);
        document = new Document();
        mQueue = Volley.newRequestQueue(DataMainActivity.this);
        Log.e("##","开始list了");
        showList(content);
        if(recommendList.size()!=0) {
            showClickResponse();
        }
    }

    private void showList(String content) {
        // 与服务器交互
        String url = "http://47.100.226.176:8080/XueBaJun/GetRecommendListOfDocumentPage";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                document = gson.fromJson(jsonObject.toString(), Document.class);
                if (document != null) {
                    recommendList = document.getRecommendList();
                    Log.e("##","分页推荐资料列表返回"+jsonObject.toString());
                    setListView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
      /*recommendList.add(new Document("book1", 20, 7));
      recommendList.add(new Document("book2", 30, 6));
        recommendList.add(new Document("book3", 30, 6));*/
      Log.e("##","总共的资料数"+recommendList.size());
        setListView();
    }

    private void setListView(){
        int n = recommendList.size();
        int half = (n+1)/2;
        for(int i = 0; i < n; i++){
            if(i < half){
                recommendLeft.add(recommendList.get(i));
                Log.e("##","left+1");
            }
            else{
                recommendRight.add(recommendList.get(i));
                Log.e("##","right+1");
            }
        }
        Log.e("##","开始适配器1");
        adapterLeft = new FavoriteListAdapter(DataMainActivity.this, recommendLeft);
        Log.e("##","开始适配器2");
        listViewLeft.setAdapter(adapterLeft);
        adapterRight = new FavoriteListAdapter(DataMainActivity.this,recommendRight);
        listViewRight.setAdapter(adapterRight);
    }

    private void showClickResponse(){
        listViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 通过recommendList可想下一界面传递name/id
                 */
                Log.e("###","left"+position);
            }
        });
        listViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("###","right"+position);
            }
        });
    }
}
