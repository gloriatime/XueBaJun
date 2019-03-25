package com.example.gloria.myapplication.paper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Document;

import java.util.List;

public class FavoriteListAdapter extends BaseAdapter {
    private List<Document> recommendList;
    private Context context;

    public FavoriteListAdapter(Context context, List<Document> recommendList) {
        this.context = context;
        this.recommendList = recommendList;
    }

    @Override
    public int getCount() {
        return recommendList.size();
    }

    @Override
    public Object getItem(int position) {
        return recommendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recommendList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentHolder commentHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.favorites_item, parent, false);
            commentHolder = new CommentHolder(convertView);
            convertView.setTag(commentHolder);
        }else {
            commentHolder = (CommentHolder) convertView.getTag();
        }
        commentHolder.tv_title.setText(recommendList.get(position).getName());
        commentHolder.tv_massage.setText(String.valueOf(recommendList.get(position).getComment()));
        commentHolder.tv_look.setText(String.valueOf(recommendList.get(position).getNumber()));

        return convertView;
    }

    private class CommentHolder {
        private ImageView massage_logo, look_logo;
        private TextView tv_massage, tv_look;
        private TextView tv_title;

        public CommentHolder(View view) {
            massage_logo = (ImageView) view.findViewById(R.id.imageViewMessage);
            look_logo = (ImageView) view.findViewById(R.id.imageViewLook);
            tv_massage = (TextView) view.findViewById(R.id.textViewMessage);
            tv_look = (TextView) view.findViewById(R.id.textViewLook);
            tv_title = (TextView)view.findViewById(R.id.textViewTitle);
        }
    }
}
