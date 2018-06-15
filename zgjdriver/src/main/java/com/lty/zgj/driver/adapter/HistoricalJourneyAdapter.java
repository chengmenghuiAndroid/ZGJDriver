package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lty.zgj.driver.R;

import cn.droidlover.xdroid.base.SimpleRecAdapter;

/**
 * Created by Administrator on 2018/6/15.
 */

public class HistoricalJourneyAdapter extends SimpleRecAdapter<String, HistoricalJourneyAdapter.ViewHolder> {

    public HistoricalJourneyAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.historical_journey_adapter;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
