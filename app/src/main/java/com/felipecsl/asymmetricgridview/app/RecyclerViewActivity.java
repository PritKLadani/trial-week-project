package com.felipecsl.asymmetricgridview.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.felipecsl.asymmetricgridview.AsymmetricRecyclerView;
import com.felipecsl.asymmetricgridview.AsymmetricRecyclerViewAdapter;
import com.felipecsl.asymmetricgridview.Utils;
import com.felipecsl.asymmetricgridview.app.model.DemoItem;
import com.felipecsl.asymmetricgridview.app.presenter.ApiObject;
import com.felipecsl.asymmetricgridview.app.presenter.ApiUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerViewCategories;
    private final DemoUtils demoUtils = new DemoUtils();
    private boolean loading = false;
    List<ApiObject> postList = new ArrayList<>(18);
    List<DemoItem> dataList;
    RecyclerViewAdapter adapter;
    AsymmetricRecyclerView recyclerView;
    AsymmetricRecyclerViewAdapter asymmetricRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(RecyclerViewActivity.this, "refreshed", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerViewCategories = findViewById(R.id.recycler_view_categories);

        List<String> categoryItems = Arrays.asList(getResources().getStringArray(R.array.categories));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryItems);

        recyclerViewCategories.setAdapter(categoryAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewCategories.setLayoutManager(layoutManager);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ApiUtil.getServiceClass().getAllPost().enqueue(new Callback<List<ApiObject>>() {
            @Override
            public void onResponse(Call<List<ApiObject>> call, Response<List<ApiObject>> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    postList = response.body();
                    Log.d("retro", "Returned count " + postList.size());
                    setupRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<ApiObject>> call, Throwable t) {
                //showErrorMessage();
                Log.d("retro", "error loading from API ::::" + t.getLocalizedMessage() + " :::: " + t.getMessage() + " :::: " + t.getCause());
            }
        });
    }

    private void setupRecyclerView() {
        dataList = demoUtils.moreItems(postList.size());
        adapter = new RecyclerViewAdapter(dataList, RecyclerViewActivity.this);
        /*asymmetricRecyclerViewAdapter = new AsymmetricRecyclerViewAdapter(this, recyclerView, adapter);
        adapter.notifyDataSetChanged();
        asymmetricRecyclerViewAdapter.notifyDataSetChanged();*/

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                Log.d("Temp_log", "firstvisible: " + linearLayoutManager.findFirstVisibleItemPosition());

                AsymmetricRecyclerViewAdapter asymmetricRecyclerViewAdapter = (AsymmetricRecyclerViewAdapter) recyclerView.getAdapter();
                Log.d("Temp_log", "Actual Item count: " + asymmetricRecyclerViewAdapter.getActualItemCount());

                AsymmetricRecyclerView asymmetricRecyclerView = (AsymmetricRecyclerView) recyclerView;
                int actualFirstVisibleItem = asymmetricRecyclerView.findActualFirstVisibleItem();
                Log.d("Temp_log", "Actual first item visible: " + actualFirstVisibleItem);

                Log.d("Temp_log_aaa", "isVideoView: ");

                if (dy > 0) {

                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + 5)) {
                        Log.d("Temp_tag", "item count: " + recyclerView.getAdapter().getItemCount());
                        Log.d("Temp_tag", "actual item count: " + ((AsymmetricRecyclerViewAdapter) recyclerView.getAdapter()).getActualItemCount());
                        Log.d("RecyclerViewActivity2", "reached at the end:" + totalItemCount + ":" + lastVisibleItem);
                        loading = true;
                        int oldSize = dataList.size() / 3;
                        /*dataList.addAll(demoUtils.moreItems(postList.size(), postList));
                        adapter.notifyItemRangeChanged(oldSize, postList.size());*/
                        loading = false;
                    }
                }
            }
        });


        recyclerView.setRequestedColumnCount(3);
        recyclerView.setRequestedHorizontalSpacing(Utils.dpToPx(this, 3));
        recyclerView.addItemDecoration(
                new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_padding)));
        asymmetricRecyclerViewAdapter = new AsymmetricRecyclerViewAdapter<>(this, recyclerView, adapter);
        recyclerView.setAdapter(asymmetricRecyclerViewAdapter);
    }

}

