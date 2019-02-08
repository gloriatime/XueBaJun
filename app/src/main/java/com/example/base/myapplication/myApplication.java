package com.example.base.myapplication;

import android.app.Application;
import android.graphics.Typeface;
import java.lang.reflect.Field;


public class myApplication extends Application {
    public static Typeface typeface;
    @Override
    public void onCreate() {
        super.onCreate();
        typeface = Typeface.createFromAsset(getAssets(), "font/xinwei.TTF");
        try { Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, typeface); }
            catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace(); }
    }
    
    }
