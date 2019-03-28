package com.example.gloria.myapplication.manage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.myapplication.BackJump;
import com.example.base.myapplication.NetImage;
import com.example.base.myapplication.ScreenSizeUtil;
import com.example.gloria.myapplication.MainActivity;
import com.example.gloria.myapplication.R;
import com.example.model.myapplication.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ChangeInfoActivity extends AppCompatActivity {

    private ImageButton head_image_button;
    private TextView name_text,grade_text,college_text,interest_text;
    private LinearLayout change_head,change_name,change_grade,change_college,change_interest,change_pwd,change_status;
    RequestQueue mQueue;
    User user;
    int ALBUM_REQUEST_CODE = 1;
    int CROP_REQUEST_CODE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        init();
        getUser();
        setUserInfo();
        setClickFunction();
    }

    private void init(){
        head_image_button = (ImageButton) findViewById(R.id.head_image_button);
        name_text = (TextView) findViewById(R.id.name_text);
        grade_text = (TextView) findViewById(R.id.grade_text);
        college_text = (TextView) findViewById(R.id.college_text);
        interest_text = (TextView) findViewById(R.id.interest_text);
        change_head = (LinearLayout) findViewById(R.id.change_head);
        change_name = (LinearLayout) findViewById(R.id.change_name);
        change_grade = (LinearLayout) findViewById(R.id.change_grade);
        change_college = (LinearLayout) findViewById(R.id.change_college);
        change_interest = (LinearLayout) findViewById(R.id.change_interest);
        change_pwd = (LinearLayout) findViewById(R.id.change_pwd);
        change_status = (LinearLayout) findViewById(R.id.change_status);
        user = new User();
        mQueue = Volley.newRequestQueue(this);
    }

    private void getUser(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }
    private void setUserInfo(){
        NetImage image = new NetImage();
        String url = "http://47.100.226.176:8080/XueBaJun/head_image/"+user.getPhone()+".jpg";
        image.setHeadImage(mQueue,head_image_button,url);

        name_text.setText(user.getName());
        grade_text.setText(user.getGrade());
        college_text.setText(user.getCollege());
        StringBuffer interest_string = new StringBuffer();
        if(user.isArt()){
            interest_string.insert(interest_string.length(),"艺 ");
        }
        if(user.isAgriculture()){
            interest_string.insert(interest_string.length(),"农 ");
        }
        if(user.isManagement()){
            interest_string.insert(interest_string.length(),"管 ");
        }
        if(user.isHumanity()){
            interest_string.insert(interest_string.length(),"文 ");
        }
        if(user.isTechnology()){
            interest_string.insert(interest_string.length(),"理");
        }
        if(user.isPlay()){
            interest_string.insert(interest_string.length(),"乐 ");
        }
        if(user.isMedicine()){
            interest_string.insert(interest_string.length(),"医 ");
        }
        interest_text.setText(interest_string);
    }

    // 设置点击事件
    private void setClickFunction(){

        // 头像从手机相册上传，剪裁为300*300的大小
        change_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
            }
        });

        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        change_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeGradeDialog();
            }
        });

        change_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeInterestDialog();
            }
        });

        change_college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeCollegeDialog();
            }
        });

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePwdDialog();
            }
        });

        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeStatusDialog();
            }
        });
    }

    // 裁剪图片
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (requestCode){
            case 1:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case 3:     //调用剪裁后返回
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    // 将image上传到服务器中，并变更UI
                    NetImage head = new NetImage();
                    head.uploadImage(image,user.getPhone(),head_image_button,mQueue);
                }
                break;
        }
    }

    private void showInputDialog() {

        final EditText editText = new EditText(this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入新的昵称")
                .setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user.setName(editText.getText().toString());
                        // 发送给后台处理
                        String url = "http://47.100.226.176:8080/XueBaJun/ChangeName";
                        sendChangeMessage(url,user);
                        // 交互成功更改UI显示
                        name_text.setText(user.getName());
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        inputDialog.show();
    }

    private void showChangeGradeDialog(){
        final String items[] = {"大一", "大二", "大三", "大四"};
        AlertDialog dialog = new AlertDialog.Builder(this)
         .setIcon(R.drawable.ic_head_image)
        //设置标题的图片
        .setTitle("请选择年级")//设置对话框的标题
        .setItems(items, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int chosen) {
                user.setGrade(items[chosen]);
                // 与后台交互
                String url = "http://47.100.226.176:8080/XueBaJun/ChangeGrade";
                sendChangeMessage(url,user);
                // 交互成功更改UI显示
                grade_text.setText(user.getGrade());
            } })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); } })
        .create();
        dialog.show();

    }

    private void showChangeInterestDialog(){
        final String items[] = {"艺术", "农业", "理工", "管理","人文","生活","医学"};
        final boolean checkedItems[] = {false, false, false, false, false, false, false};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_personal_message)//设置标题的图片
                .setTitle("请选择自己的兴趣")//设置对话框的标题
                .setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked; } })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); } })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int chosen) {
                        user.setArt(checkedItems[0]);
                        user.setAgriculture(checkedItems[1]);
                        user.setTechnology(checkedItems[2]);
                        user.setManagement(checkedItems[3]);
                        user.setHumanity(checkedItems[4]);
                        user.setPlay(checkedItems[5]);
                        user.setMedicine(checkedItems[6]);
                        dialog.dismiss();
                        // 发送给后台处理
                        String url = "http://47.100.226.176:8080/XueBaJun/ChangeInterest";
                        sendChangeMessage(url,user);
                        // 成功后更改UI显示
                        setUserInfo();
                    } })
                .create();
        dialog.show();
    }

    private void showChangeCollegeDialog(){

        final String items[] = {"纺织学院", "管理学院", "机械工程学院", "信息科学与技术学院","计算机科学与技术学院", "化学化工与生物工程学院",
                "材料科学与工程学院", "环境科学与工程学院","人文学院", "理学院", "外语学院", "马克思主义学院","搞体育的","学艺术的"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_personal_message)
                //设置标题的图片
                .setTitle("请选择你的学院")//设置对话框的标题
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int chosen) {
                        user.setCollege(items[chosen]);
                        // 与后台交互
                        String url = "http://47.100.226.176:8080/XueBaJun/ChangeCollege";
                        sendChangeMessage(url,user);
                        // 交互成功更改UI显示
                        college_text.setText(user.getCollege());
                    } })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); } })
                .create();

        dialog.show();

    }

    private void showChangePwdDialog() {

        View view = View.inflate(this, R.layout.dialog_change_pwd, null);
        final EditText pwd_edit = (EditText) view.findViewById(R.id.pwd_string);
        final EditText pwd_confirm_edit = (EditText) view.findViewById(R.id.pwd_confirm_string);
        Button cancel = (Button) view.findViewById(R.id.cancel_button);
        Button confirm = (Button) view.findViewById(R.id.confirm_button);
        final Dialog inputDialog = new Dialog(this, R.style.NormalDialogStyle);
        inputDialog.setContentView(view);
        inputDialog.setCanceledOnTouchOutside(true);

        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtil.getInstance(this).getScreenHeight() * 0.23f));
        Window dialogWindow = inputDialog.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtil.getInstance(this).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 验证密码是否一致且长度不超过30字符且不能为空
                assert pwd_confirm_edit != null;
                assert pwd_edit != null;
                if(pwd_edit.getText().toString().equals("")||pwd_confirm_edit.getText().toString().equals("")){
                    Toast.makeText(ChangeInfoActivity.this, "密码和确认密码不能为空", Toast.LENGTH_SHORT).show();
                }else if (!pwd_edit.getText().toString().equals(pwd_confirm_edit.getText().toString())){
                    Toast.makeText(ChangeInfoActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    pwd_edit.setText("");
                    pwd_confirm_edit.setText("");
                }else if(pwd_edit.getText().toString().length()>30){
                    Toast.makeText(ChangeInfoActivity.this, "密码长度不能超过30个字符", Toast.LENGTH_SHORT).show();
                    pwd_edit.setText("");
                    pwd_confirm_edit.setText("");
                }else{
                    user.setPwd(pwd_edit.getText().toString());
                    // 发送给后台处理
                    String url = "http://47.100.226.176:8080/XueBaJun/ChangePwd";
                    sendChangeMessage(url,user);
                    // 交互成功toast提示
                    Toast.makeText(ChangeInfoActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    inputDialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.dismiss();
            }
        });
        inputDialog.show();
    }

    private void showChangeStatusDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_personal_message)
                //设置标题的图片
                .setTitle("确认要退出登录么？")//设置对话框的标题
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("##","需要退出登录");
                        user = null;
                        Intent intent = new Intent(ChangeInfoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void sendChangeMessage(String url, User user){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",user.getPhone());
            jsonObject.put("name" ,user.getName());
            jsonObject.put("pwd",user.getPwd());
            jsonObject.put("grade",user.getGrade());
            jsonObject.put("college",user.getCollege());
            jsonObject.put("art",user.isArt());
            jsonObject.put("agriculture",user.isAgriculture());
            jsonObject.put("technology",user.isTechnology());
            jsonObject.put("medicine",user.isMedicine());
            jsonObject.put("management",user.isManagement());
            jsonObject.put("humanity",user.isHumanity());
            jsonObject.put("play",user.isPlay());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                Log.e("##","更改信息成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("##","更改信息失败"+volleyError.toString());
            }
        });
        mQueue.add(jsonObjectRequest);
    }

}
