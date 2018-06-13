package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lty.zgj.driver.R;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xdroidbase.cache.SharedPref;

/**
 * Created by Administrator on 2018/6/12.
 */

public class DepartAdapter extends SimpleRecAdapter<String , DepartAdapter.ViewHolder> {



    public DepartAdapter(Context context) {
        super(context);
        ShowDialogRelative.toastDialog(context,"DepartAdapter");
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
        int pos = SharedPref.getInstance(context).getInt("position", 0);

        if(pos > 0){
            if(position == pos - 1){
                setVisible(holder.view2);
            }
        }

        if(position == 0){
            setGone(holder.view3);
        }else if(position == data.size() -1){
            setGone(holder.view2);
        }

        if(data.size() == 1){
            setGone(holder.view3);
            setGone(holder.view2);
        }


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
