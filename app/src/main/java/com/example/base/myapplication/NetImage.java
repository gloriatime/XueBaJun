package com.example.base.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.gloria.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetImage {

    // 从服务器取出图片设置UI
    public void setHeadImage(RequestQueue mQueue,final ImageButton imageButton, String url){
       /* ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        imageButton.setBackground(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageButton.setBackgroundResource(R.drawable.ic_head_image);
            }
        });
        mQueue.add(imageRequest);*/
    }

    // 将图片上传到服务器
    public void uploadImage(RequestQueue mQueue,Bitmap image,String phone,File file){

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        String url = "http://47.100.226.176:8080/XueBaJun/UploadImage";

        if (file != null && file.exists()) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM) .addFormDataPart("files", "img" + "_" + System.currentTimeMillis() + ".jpg",
                            RequestBody.create(MEDIA_TYPE_PNG, file));
            Request request = new Request.Builder()
                    .header("Authorization", "123")
                    .url(url)
                    .post(builder.build())
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.e("---", "onResponse: 成功上传图片之后服务器的返回数据：" + result); //result就是图片服务器返回的图片地址。
                }
            });

        }





        // volley上传图片图片破损
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",phone+".jpg");
            jsonObject.put("image" ,image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,jsonObject,new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject jsonObject) {
                User tempuser = new Gson().fromJson(jsonObject.toString(), User.class);
                // 签到成功，更新user的积分值并修改UI显示
                if(tempuser != null) {
                    Log.d("##uploadSuccess##", "name"+tempuser.getName()+ "\n");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("##head", error.getMessage(), error);
                byte[] htmlBodyBytes = error.networkResponse.data;
                Log.e("##head", new String(htmlBodyBytes), error);
            }
        });
        mQueue.add(jsonObjectRequest);*/
    }
}
