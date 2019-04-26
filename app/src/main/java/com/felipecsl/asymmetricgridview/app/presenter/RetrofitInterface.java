package com.felipecsl.asymmetricgridview.app.presenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @GET("v2/5cc293d433000073007e52f6")
    public Call<List<ApiObjectVideo>> getAllPost();
}