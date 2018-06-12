package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lty.zgj.driver.R;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.kit.KnifeKit;

/**
 * Created by Administrator on 2018/6/12.
 */

public class DepartAdapter extends SimpleRecAdapter<String , DepartAdapter.ViewHolder> {



    public DepartAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.depart_adapter;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String s = data.get(position);

        if(position == 0){
            holder.view3.setVisibility(View.GONE);
        }

        if(position == data.size() -1){
            holder.view2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_3)
        View view3;
        @BindView(R.id.view_2)
        View view2;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }
}
