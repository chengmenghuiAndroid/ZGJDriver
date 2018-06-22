package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lty.zgj.driver.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.RecyclerAdapter;

/**
 * Created by Administrator on 2018/6/12.
 */

public class DepartAdapter extends RecyclerAdapter<String, DepartAdapter.ViewHolder> {


    private int TYPE_ICON = 0;
    private int TYPE_NORMAL = 1;
    private View mIconView;
    private View view;

    public DepartAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


//        if (viewType == TYPE_ICON){
//
//            mIconView = LayoutInflater.from(parent.getContext()).inflate(R.layout.depart_icon,parent,false);
//
//            return new ViewIconHolder(view);

        if (mIconView != null && viewType == TYPE_ICON) {
            return new ViewHolder(mIconView);
        }

//        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.depart_adapter, parent, false);

        return new ViewHolder(view);

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 2) return TYPE_ICON;
        return TYPE_NORMAL;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ICON){
            return;
        }

        final int pos = getRealPosition(holder);
        String s = data.get(pos);

        if (pos == 0) {
            setInvisible(holder.dashed_icon_1);
            holder.iconOval.setImageDrawable(context.getResources().getDrawable(R.mipmap.start));
        } else if (pos == data.size() - 1) {
            setInvisible(holder.dashed_icon_2);
            holder.iconOval.setImageDrawable(context.getResources().getDrawable(R.mipmap.over));
        } else {
            setVisible(holder.dashed_icon_1);
            setVisible(holder.dashed_icon_2);
            holder.iconOval.setImageDrawable(context.getResources().getDrawable(R.drawable.shape_oval_green));
        }

        if(pos == 1){
            setGone(holder.dashed_icon_2);
        }else if(pos == 2){
            setGone(holder.dashed_icon_1);
        }
    }


    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if(position > 2){
            return mIconView == null ? position : position - 1;
        }

        return position;
    }

    public void setIconView(View iconView) {
        mIconView = iconView;
    }


//    public class ViewIconHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.view_1)
//        View view1;
//
//        public ViewIconHolder(View view) {
//            super(view);
//            KnifeKit.bind(this, view);
//        }
//    }
//
//    public class ViewNormalHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.view_3)
//        View view3;
//        @BindView(R.id.view_2)
//        View view2;
//        @BindView(R.id.al_item)
//        AutoLinearLayout alItem;
//
//        public ViewNormalHolder(View view) {
//            super(view);
//            KnifeKit.bind(this, view);
//        }
//    }

    @Override
    public int getItemCount() {
        return mIconView == null ? data.size() : data.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_oval)
        ImageView iconOval;
        @BindView(R.id.dashed_1)
        ImageView dashed_icon_1;
        @BindView(R.id.dashed_2)
        ImageView dashed_icon_2;
        @BindView(R.id.al_item)
        AutoLinearLayout alItem;

        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView == mIconView) return;
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }


}
