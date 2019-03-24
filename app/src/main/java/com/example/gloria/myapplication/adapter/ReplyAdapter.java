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
import com.example.gloria.myapplication.searchPaper.PaperDetailMainActivity;
import com.example.model.myapplication.Document;
import com.example.model.myapplication.Reply;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 2019/3/12.
 */

public class ReplyAdapter extends BaseAdapter{
    private List<Reply> replyBeanList;
    private Context context;

    public ReplyAdapter(Context context, List<Reply> replyBeanList) {
        this.context = context;
        this.replyBeanList = replyBeanList;
    }

    @Override
    public int getCount() {
        return replyBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return replyBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return replyBeanList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReplyHolder replyHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout, parent, false);
            replyHolder = new ReplyHolder(convertView);
            convertView.setTag(replyHolder);
        }else {
            replyHolder = (ReplyHolder) convertView.getTag();
        }
        RequestQueue mQueue = Volley.newRequestQueue(convertView.getContext());
        ImageRequest imageRequest = new ImageRequest(
                "http://47.100.226.176:8080/XueBaJun/head_image/"+replyBeanList.get(position).getCritic().getPhone()+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        replyHolder.logo.setImageDrawable(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                replyHolder.logo.setBackgroundResource(R.drawable.ic_head_image);
            }
        });
        mQueue.add(imageRequest);
        replyHolder.tv_name.setText(replyBeanList.get(position).getCritic().getName());
        String reback = "";
        if(replyBeanList.get(position).getAt() != null){
            reback = "@ "+ replyBeanList.get(position).getAt().getName()+" ";
        }
        replyHolder.tv_content.setText(reback+replyBeanList.get(position).getContent());

        return convertView;
    }

    private class ReplyHolder {
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;

        public ReplyHolder(View view) {
            logo = (CircleImageView) view.findViewById(R.id.reply_item_logo);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
            tv_name = (TextView) view.findViewById(R.id.reply_item_userName);
        }
    }

    /********
     * 插入一条新的评论
     * @param replyDetailBean
     */
    public void addTheCommentData(Reply replyDetailBean) {
        if (replyDetailBean != null) {
            replyBeanList.add(replyDetailBean);
            notifyDataSetChanged();
        }
    }
}
