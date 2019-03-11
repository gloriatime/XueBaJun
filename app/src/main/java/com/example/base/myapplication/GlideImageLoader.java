package com.example.base.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.gloria.myapplication.MainActivity;
import com.example.gloria.myapplication.R;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {

    RequestQueue mQueue ;

    @Override
    public void displayImage(Context context, Object path, final ImageView imageView) {

        mQueue = Volley.newRequestQueue(context);

        ImageRequest imageRequest = new ImageRequest(
                path.toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable temp = new BitmapDrawable(response);
                        imageView.setBackground(temp);
                    }
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setBackgroundResource(R.drawable.ic_head_image);
            }
        });
        mQueue.add(imageRequest);
    }
}
