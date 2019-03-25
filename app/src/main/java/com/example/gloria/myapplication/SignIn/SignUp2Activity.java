package com.example.gloria.myapplication.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.gloria.myapplication.R;

public class SignUp2Activity extends AppCompatActivity {
    private String[] mStrs = {"东华大学", "上海外国语大学", "上海工程技术大学","华东政法大学",
            "复旦大学视觉艺术学院", "对外贸易大学", "立信会计学院"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        Intent gintent = getIntent();
        final String mail = gintent.getStringExtra("mail");
        final String passwd = gintent.getStringExtra("passwd");
        final String name = gintent.getStringExtra("name");

        mSearchView = (SearchView) findViewById(R.id.searchView);
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        final TextView sTextView = (TextView) mSearchView.findViewById(id);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SignUp2Activity.this, SignUp3Activity.class);
                intent.putExtra("mail",mail);
                intent.putExtra("passwd", passwd);
                intent.putExtra("name", name);
                intent.putExtra("university", sTextView.getText());
                startActivity(intent);
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String text = (String) mListView.getItemAtPosition(position);
                            sTextView.setText(text);
                            Intent intent = new Intent(SignUp2Activity.this, SignUp3Activity.class);
                            intent.putExtra("mail",mail);
                            intent.putExtra("passwd", passwd);
                            intent.putExtra("name", name);
                            intent.putExtra("university", sTextView.getText());
                            startActivity(intent);
                        }
                    });
                }
                else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = (String) mListView.getItemAtPosition(position);
                sTextView.setText(text);
                Intent intent = new Intent(SignUp2Activity.this, SignUp3Activity.class);
                intent.putExtra("mail",mail);
                intent.putExtra("passwd", passwd);
                intent.putExtra("name", name);
                intent.putExtra("university", sTextView.getText());
                startActivity(intent);
            }
        });

    }
}


