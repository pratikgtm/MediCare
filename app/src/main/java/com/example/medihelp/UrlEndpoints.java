package com.example.medihelp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by abc on 27-02-2016.
 */
public class UrlEndpoints {

    public static final String URL_MAIN_CURRENT = "https://data.gov.in/api/datastore/resource.json?resource_id=7d208ae4-5d65-47ec-8cb8-2a7a7ac89f8c";
    public static final String URL_APIKEY = "api-key=946020eb6017783afc4d38989ea7e2ec";
    public static  final String URL_CHAR_AMPERSAND = "&";
    public static final String URL_FILTER_DISTRICT = "filters[district]=";
    public static String getUrlCurrent(String district){
        return URL_MAIN_CURRENT + URL_CHAR_AMPERSAND + URL_APIKEY + URL_CHAR_AMPERSAND + URL_FILTER_DISTRICT + district ;
    }
    public static String getUrlCurrentAll(){
        return URL_MAIN_CURRENT + URL_CHAR_AMPERSAND + URL_APIKEY ;
    }
}

