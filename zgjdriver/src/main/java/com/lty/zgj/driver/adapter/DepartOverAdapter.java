package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.RecyclerAdapter;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DepartOverAdapter extends RecyclerAdapter<HistoricalJourneyDetailModel.StationsBean, DepartOverAdapter.ViewHolder>{


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

        final int pos = getRealPosition(holder);

      if(data != null && data.size() >0 ){
          HistoricalJourneyDetailModel.StationsBean stationsBean = data.get(pos);
          if(stationsBean != null){
              String stationTime = setArriveStationTime(stationsBean);
              holder.tvDepartTime.setText(stationTime);
              holder.tvStation.setText(stationsBean.getStationName());

          }

      }

    }

    /**
     * @param stationsBean
     * @return
     */
    private String setArriveStationTime(HistoricalJourneyDetailModel.StationsBean stationsBean) {
        String planTime = stationsBean.getPlanTime();
        String substring_1 = planTime.substring(0, 2);
        String substring_2 = planTime.substring(2, planTime.length());
        String time = substring_1 + ":" + substring_2;
        return time;
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
        @BindView(R.id.tv_depart_time)
        TextView tvDepartTime;
        @BindView(R.id.tv_station)
        TextView tvStation;


        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mIconView) return;
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }

}
