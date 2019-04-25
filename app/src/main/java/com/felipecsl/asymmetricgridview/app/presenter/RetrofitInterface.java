package com.felipecsl.asymmetricgridview.app.presenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @GET("v2/5cc2099d2d0000df595e9c7f")
    public Call<List<ApiObject>> getAllPost();
}
