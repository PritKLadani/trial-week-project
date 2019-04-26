package com.felipecsl.asymmetricgridview.app.presenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @GET("v2/5cc27bce3300000d007e5298")
    public Call<List<ApiObject>> getAllPost();
}
