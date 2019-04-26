package com.felipecsl.asymmetricgridview.app.presenter;

public class ApiUtil {

    private static final String BASE_URL = "http://www.mocky.io/";

    public static RetrofitInterface getServiceClass(){
        return RetrofitApi.getRetrofit(BASE_URL).create(RetrofitInterface.class);
    }
}