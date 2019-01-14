package com.example.user.signuppage;

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

public class SignUp3Activity extends AppCompatActivity {
    private String[] mStrs = {"人文学院", "理学院", "环境学院","计算机科学与技术学院",
            "材料学院", "外语学院"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        Intent gintent = getIntent();
        final String mail = gintent.getStringExtra("mail");
        final String passwd = gintent.getStringExtra("passwd");
        final String name = gintent.getStringExtra("name");
        final String univers = gintent.getStringExtra("university");

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
                Intent intent = new Intent(SignUp3Activity.this, SignUp4Activity.class);
                intent.putExtra("mail",mail);
                intent.putExtra("passwd", passwd);
                intent.putExtra("name", name);
                intent.putExtra("university", univers);
                intent.putExtra("major", sTextView.getText());
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
                            Intent intent = new Intent(SignUp3Activity.this, SignUp4Activity.class);
                            intent.putExtra("mail",mail);
                            intent.putExtra("passwd", passwd);
                            intent.putExtra("name", name);
                            intent.putExtra("university", univers);
                            intent.putExtra("major", text);
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

    }
}


