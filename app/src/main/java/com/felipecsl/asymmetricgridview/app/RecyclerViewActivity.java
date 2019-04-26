package com.felipecsl.asymmetricgridview.app;

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

import java.util.Arrays;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerViewCategories;
    private final DemoUtils demoUtils = new DemoUtils();
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        final AsymmetricRecyclerView recyclerView = findViewById(R.id.recyclerView);

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

        final List<DemoItem> dataList = demoUtils.moarItems(54);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataList, RecyclerViewActivity.this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //loading = false;
                }
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
                        Log.d("Temp_tag", "actual item count: " + recyclerView.getAdapter());
                        Log.d("RecyclerViewActivity2", "reached at the end:" + totalItemCount + ":" + lastVisibleItem);
                        loading = true;
                        int oldSize = dataList.size() / 3;
                        dataList.addAll(demoUtils.moarItems(54));
                        adapter.notifyItemRangeChanged(oldSize, 18);
                        loading = false;
                    }
                }
            }
        });


        recyclerView.setRequestedColumnCount(3);
//        recyclerView.setDebugging(true);
        recyclerView.setRequestedHorizontalSpacing(Utils.dpToPx(this, 3));
        recyclerView.addItemDecoration(
                new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_padding)));
        recyclerView.setAdapter(new AsymmetricRecyclerViewAdapter<>(this, recyclerView, adapter));
    }

}

