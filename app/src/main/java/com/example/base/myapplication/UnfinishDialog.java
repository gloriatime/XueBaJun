package com.example.base.myapplication;

import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

public class UnfinishDialog {

    public UnfinishDialog(final Context context){
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
                        showGoodByeDialog(context);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void showGoodByeDialog(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("再！ 见！")//设置对话框的标题
                .setPositiveButton("好聚好散", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid()) ;   //获取PID
                        System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
                    }
                })
                .create();
        dialog.show();
    }
}
