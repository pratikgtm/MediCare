package com.example.medihelp;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Created by abc on 27-02-2016.
 */
public class MyApplication extends Application{
   private static  MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }
    public static MyApplication getInstance(){
        return  sInstance;
    }
    public static Context getAppcontext(){
        return sInstance.getApplicationContext();
    }
}
