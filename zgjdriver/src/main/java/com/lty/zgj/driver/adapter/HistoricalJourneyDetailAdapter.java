package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.RecyclerAdapter;

/**
 * Created by Administrator on 2018/6/21.
 */

public class HistoricalJourneyDetailAdapter extends RecyclerAdapter<HistoricalJourneyDetailModel.StationsBean, HistoricalJourneyDetailAdapter.ViewHolder> {

    private int TYPE_ICON = 0;
    private int TYPE_NORMAL = 1;
    private View mIconView;
    private View view;
    private int TAG_VIEW = 0;

    public HistoricalJourneyDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mIconView != null && viewType == TYPE_ICON) {
            return new ViewHolder(mIconView);
        }

//        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.depart_adapter_detail, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) return TYPE_ICON;
        return TYPE_NORMAL;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ICON) return;
        final int pos = getRealPosition(holder);


        if (data.size() > 0) {
            final HistoricalJourneyDetailModel.StationsBean stationsBean = data.get(pos);


            if (pos == 0) {
                setGone(holder.view3);
            } else if (pos == data.size() - 1) {
                setGone(holder.view2);
            } else {
                setVisible(holder.view3);
                setVisible(holder.view2);
            }

            holder.tvStation.setText(stationsBean.getStationName());
            String planTime = stationsBean.getPlanTime();
            String substring_1 = planTime.substring(0, 2);
            String substring_2 = planTime.substring(2, planTime.length());
            String time = substring_1 + ":" + substring_2;

            holder.tvDepartTime.setText("今天"+ time);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRecItemClick().onItemClick(pos, stationsBean, TAG_VIEW, holder);
                }
            });

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
        if(data.size() > 0){
            return mIconView == null ? data.size() : data.size() + 1;
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_1)
        View view1;
        @BindView(R.id.view_2)
        View view2;
        @BindView(R.id.view_3)
        View view3;
        @BindView(R.id.icon_oval)
        ImageView iconOval;
        @BindView(R.id.tv_depart_time)
        TextView tvDepartTime;
        @BindView(R.id.tv_station)
        TextView tvStation;
        @BindView(R.id.al_item)
        AutoLinearLayout alItem;

        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView == mIconView) return;
            KnifeKit.bind(this, view);
            AutoUtils.auto(itemView);
        }
    }

}
