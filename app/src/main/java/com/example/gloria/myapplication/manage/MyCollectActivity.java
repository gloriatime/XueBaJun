package com.example.gloria.myapplication.manage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Book;
import com.example.model.myapplication.Course;
import com.example.model.myapplication.Document;

import java.util.ArrayList;

public class MyCollectActivity extends AppCompatActivity {

    private Button documents_button,books_button,courses_button;
    private ListView list;
    private ArrayList<Document> documents;
    private ArrayList<Book> books;
    private ArrayList<Course> courses;
    private int DOCUMENT=1,BOOK=2,COURSR=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        init();
        // 默认显示收藏的资料列表
        documents_button.performClick();
    }

    private void init(){
        documents_button = (Button) findViewById(R.id.documents_button);
        books_button  = (Button) findViewById(R.id.books_button);
        courses_button= (Button) findViewById(R.id.courses_button);
        list= (ListView) findViewById(R.id.list);
    }

    private void setClickFun(){
        documents_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonUI(documents_button,books_button,courses_button);
                updataDoucuments();
                setList(DOCUMENT);
            }
        });
    }
    private void changeButtonUI(Button be_clicked_button,Button normal_button_1,Button normal_button_2){
        be_clicked_button.setTextColor(getResources().getColor(R.color.white));
        be_clicked_button.setBackgroundColor(getResources().getColor(R.color.red));
        be_clicked_button.setEnabled(false);

        normal_button_1.setTextColor(getResources().getColor(R.color.red));
        normal_button_1.setBackground(getResources().getDrawable(R.drawable.bg_stroke));
        normal_button_1.setEnabled(true);

        normal_button_2.setTextColor(getResources().getColor(R.color.red));
        normal_button_2.setBackground(getResources().getDrawable(R.drawable.bg_stroke));
        normal_button_2.setEnabled(true);
    }

    private void setList(int listContent) {
    }

    private void updataDoucuments() {

    }
}
