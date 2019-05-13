package com.example.base.myapplication;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class ListItemViewHolder {
    public TextView name;
    public ImageView head;
    public Button button;

    public TextView content;

    // for upload_document_list
    public TextView author,path,tag;
    public Button choose_tag;

    // for user_info_dynamic
    public TextView comment_num;
    public TextView score;
    public TextView course_name;
    // public LinearLayout list_item;
}
