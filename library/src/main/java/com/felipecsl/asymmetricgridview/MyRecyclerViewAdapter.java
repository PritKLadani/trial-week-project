package com.felipecsl.asymmetricgridview;

import android.support.v7.widget.RecyclerView;

public abstract class MyRecyclerViewAdapter extends RecyclerView.Adapter<AdapterImpl.ViewHolder> {

    abstract int getMyActualItemCount();
}
