package com.example.gloria.myapplication.manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.DateGson;
import com.example.base.myapplication.MySpinner;
import com.example.gloria.myapplication.search.SearchResultActivity;
import com.example.gloria.myapplication.searchPaper.PaperDetailMainActivity;
import com.example.gloria.myapplication.showInfo.CourseDetailActivity;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.Tag;
import com.example.model.myapplication.TagTag;
import com.example.model.myapplication.User;
import com.google.gson.Gson;
import com.example.gloria.myapplication.R;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddTagActivity extends AppCompatActivity {

    EditText tag_name_edit;
    Button add_tag_button;
    Spinner spinner;
    ListView tag_list_view;
    TextView document_name_text,tag_name_text;

    List<Tag> tagList;
    List<String> data_list;
    ArrayAdapter<String> arr_adapter;
    RequestQueue mQueue;
    User user;
    Tag tag;
    Document document;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        init();

        getExtra();

        setPage();

        listenEditor();

    }


    private void init() {
        tag_name_edit = (EditText) findViewById(R.id.tag_name_edit);
        add_tag_button = (Button) findViewById(R.id.add_tag_button);
        spinner = (Spinner) findViewById(R.id.spinner);
        document_name_text = (TextView) findViewById(R.id.document_name_text);
        tag_name_text = (TextView) findViewById(R.id.tag_name_text);
        tag_list_view = (ListView) findViewById(R.id.tag_list_view);
        mQueue = Volley.newRequestQueue(this);
    }

    private void getExtra() {
        Intent intent = getIntent();
       user = (User) intent.getSerializableExtra("user");
       document = (Document) intent.getSerializableExtra("document");
    }

    private void setPage() {
        document_name_text.setText("为 "+document.getName()+" 添加");
        add_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag==null){
                    // 新标签选择标签类别
                    showSelectTypeDialog();
                    //addNewTag();
                }else {
                    // 旧标签，添加关系
                    tagTagToDocument();
                }
            }
        });
    }

    private void showSelectTypeDialog() {
        final String values[] = {"art", "management", "humanity", "technology", "medicine",
                "agriculture", "play","all"};
        final String items[] = {"艺术类", "管理类", "人文类", "技术类", "医学类",
                "农学类", "生活类","其他"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                //.setIcon(R.drawable.icon_personal_message)
                //设置标题的图片
                .setTitle("请选择新标签的类别")//设置对话框的标题
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int chosen) {
                        tag = new Tag();
                        tag.setType(values[chosen]);
                        tag.setName(tag_name_text.getText().toString());
                        addNewTag();
                    } })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); } })
                .create();

        dialog.show();
    }

    private void addNewTag() {

        Log.e("##","新建标签开始");

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("name", tag.getName());
            jsonObject.put("type", tag.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://47.100.226.176:8080/XueBaJun/AddTag";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Tag temp = gson.fromJson(jsonObject.toString(), Tag.class);

                tag = temp;

                Log.e("##","新建标签完成"+tag.getId()+" "+tag.getName());
                // 返回新添加标签Id后添加标签
                tagTagToDocument();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("##","新建标签出错");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void addUserTagRelation() {

        Log.e("##","添加用户标签关联度"+tag.getId()+" "+user.getPhone());

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("user", user.getPhone());
            jsonObject.put("tag", tag.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://47.100.226.176:8080/XueBaJun/UserTagPlusTen";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {

                // 直接跳转到资料的详情页面
                Intent intent = new Intent(AddTagActivity.this, PaperDetailMainActivity.class);
                intent.putExtra("user",(Serializable) user);
                intent.putExtra("document_id",document.getId());
                startActivity(intent);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 直接跳转到资料的详情页面
                Intent intent = new Intent(AddTagActivity.this, PaperDetailMainActivity.class);
                intent.putExtra("user",(Serializable) user);
                intent.putExtra("document_id",document.getId());
                startActivity(intent);
            }
        });
        mQueue.add(jsonObjectRequest);
    }


    private void tagTagToDocument() {

        Log.e("##","添加标签关系"+tag.getId()+" "+document.getId());

        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("tag", tag.getId());
            jsonObject.put("belong", document.getId());
            jsonObject.put("type", "document");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://47.100.226.176:8080/XueBaJun/TagToDocument";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                Log.e("##","为文档粘贴标签返回");
                // 返回新添加标签Id后增加用户与此标签的联系程度
                addUserTagRelation();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Log.e("##","突然的500闪了腰"+volleyError.toString());
                byte[] htmlBodyBytes = volleyError.networkResponse.data;
                Log.e("VolleyError body---->", new String(htmlBodyBytes), volleyError);
                Log.e("##","为文档粘贴标签返回");
                // 返回新添加标签Id后增加用户与此标签的联系程度
                addUserTagRelation();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void listenEditor() {
        tag_name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchTag(s.toString());
                tag_name_text.setText(" "+tag_name_edit.getText().toString()+" ");
            }
        });
    }

    private void searchTag(String s) {
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("name", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 与服务器交互得到标签可选列表
        String url = "http://47.100.226.176:8080/XueBaJun/GetTagListByLike";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new com.android.volley.Response.Listener<org.json.JSONObject>() {

            public void onResponse(org.json.JSONObject jsonObject) {
                Gson gson = new DateGson().getGson();
                Tag temp = gson.fromJson(jsonObject.toString(), Tag.class);
                tagList = temp.getTagList();
                Log.e("##","tagList返回："+jsonObject.toString());

                // 设置选择列表
                showTagList();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void showTagList() {
        //数据
        data_list = new ArrayList<String>();
        for(Tag t:tagList){
            data_list.add(t.getName());
        }

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, R.layout.simple_spinner_item, data_list);
        tag_list_view.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, data_list));
        tag_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag_name_text.setText(" "+data_list.get(position)+" ");
                tag = tagList.get(position);
            }
        });
        /*//设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //删除默认选中的状态
        //spinner.setSelection(-1,true);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        // 保持菜单展开状态
        spinner.performClick();
        // 监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });*/


    }

}
