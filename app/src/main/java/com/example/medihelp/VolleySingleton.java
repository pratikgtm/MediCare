package com.example.medihelp;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by abc on 17-12-2015.
 */
public class VolleySingleton {
    private static VolleySingleton mInstance=null;
    private RequestQueue mRequestQueue;
    private VolleySingleton(){
        mRequestQueue= Volley.newRequestQueue(MyApplication.getAppcontext());

    }
    public static VolleySingleton getInstance(){
        if(mInstance==null){
            mInstance=new VolleySingleton();
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
