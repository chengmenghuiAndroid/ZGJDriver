package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lty.zgj.driver.R;

import cn.droidlover.xdroidbase.base.SimpleRecAdapter;

/**
 * Created by Administrator on 2018/6/12.
 */

public class WaitGoingOutItemAdapter extends SimpleRecAdapter<String, WaitGoingOutItemAdapter.ViewHolder> {

    public WaitGoingOutItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.surplus;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
