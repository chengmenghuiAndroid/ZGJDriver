package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.DepartModel;
import com.zhy.autolayout.utils.AutoUtils;

import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.RecyclerAdapter;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DepartOverAdapter extends RecyclerAdapter<DepartModel.ListBean, DepartOverAdapter.ViewHolder>{


    private int TYPE_ICON = 0;
    private int TYPE_NORMAL = 1;
    private View mIconView;
    private View view;

    public DepartOverAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mIconView != null && viewType == TYPE_ICON) {
            return new ViewHolder(mIconView);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.depart_over_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) return TYPE_ICON;
        return TYPE_NORMAL;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ICON) {
            return;
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (position > 2) {
            return mIconView == null ? position : position - 1;
        }

        return position;
    }

    public void setIconView(View iconView) {
        mIconView = iconView;
    }


    @Override
    public int getItemCount() {
        return mIconView == null ? data.size() : data.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mIconView) return;
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }

}
