package com.example.base.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

public class UnfinishDialog {

    public UnfinishDialog(Context context){
        // 弹出包含修改删除两种选项的对话框
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("非常抱歉，该功能尚在开发中。")//设置对话框的标题
                .setPositiveButton("我知道了，你搞快点。", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("我一点都不期待，嘻嘻。", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
