package com.example.gloria.myapplication.Recommend;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.example.gloria.myapplication.R;
import com.example.gloria.myapplication.showInfo.Activity_Top20_course;
import com.example.model.myapplication.Course;

public class Reco_course extends Activity {
    ListView list_re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_course);
        init();
    }

    private void init() {
        TextView morecourse = (TextView) findViewById(R.id.morecourse);
        morecourse.setOnClickListener(new morec());
        list_re = (ListView)findViewById(R.id.list_Top20);
        list_re.setAdapter(new ArrayAdapter<Course>(this,android.R.layout.simple_list_item_1,data));
        list_re.setOnItemClickListener(new mItemClick());
    }

    //为更多课程添加点击事件
    class morec implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Reco_course.this, Activity_Top20_course.class);
            startActivity(intent);
    }
}

    private class mItemClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void OnItemClick()
    }
}