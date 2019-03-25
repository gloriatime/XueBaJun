package com.example.gloria.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.Comment;
import com.example.model.myapplication.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 2019/3/12.
 */

public class CommentAdapter extends BaseAdapter{
    private List<Comment> commentBeanList;
    private Context context;

    public CommentAdapter(Context context, List<Comment> commentBeanList) {
        this.context = context;
        this.commentBeanList = commentBeanList;
    }

    @Override
    public int getCount() {
        return commentBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return commentBeanList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentHolder commentHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, parent, false);
            commentHolder = new CommentHolder(convertView);
            convertView.setTag(commentHolder);
        }else {
            commentHolder = (CommentHolder) convertView.getTag();
        }
        RequestQueue mQueue = Volley.newRequestQueue(convertView.getContext());
        ImageRequest imageRequest = new ImageRequest(
                "http://47.100.226.176:8080/XueBaJun/head_image/"+commentBeanList.get(position).getCritic().getPhone()+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        commentHolder.logo.setImageDrawable(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                commentHolder.logo.setBackgroundResource(R.drawable.ic_head_image);
            }
        });
        mQueue.add(imageRequest);
        commentHolder.tv_name.setText(commentBeanList.get(position).getCritic().getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date =format.format(commentBeanList.get(position).getDate());
        commentHolder.tv_time.setText(date);
        commentHolder.tv_content.setText(commentBeanList.get(position).getContent());
        commentHolder.tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return convertView;
    }

    private class CommentHolder {
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;
        private TextView tv_reply;

        public CommentHolder(View view) {
            logo = (CircleImageView) view.findViewById(R.id.comment_item_logo);
            tv_content = (TextView) view.findViewById(R.id.comment_item_content);
            tv_name = (TextView) view.findViewById(R.id.comment_item_userName);
            tv_time = (TextView) view.findViewById(R.id.comment_item_time);
            tv_reply = (TextView) view.findViewById(R.id.comment_item_reply);
        }
    }

    /********
     * 插入一条新的评论
     * @param commentDetailBean
     */
    public void addTheCommentData(Comment commentDetailBean) {
        if (commentDetailBean != null) {
            commentDetailBean.setReplyList(null);
            commentBeanList.add(commentDetailBean);
            notifyDataSetChanged();
        }
    }
}
