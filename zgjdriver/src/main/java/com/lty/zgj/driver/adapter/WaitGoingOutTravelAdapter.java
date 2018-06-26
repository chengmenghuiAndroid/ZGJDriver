package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.TripListModel;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.kit.KnifeKit;

/**
 * Created by Administrator on 2018/6/12.
 */

public class WaitGoingOutTravelAdapter extends SimpleRecAdapter<TripListModel.NoStartListBean, WaitGoingOutTravelAdapter.ViewHolder> {


    public WaitGoingOutTravelAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.travel;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TripListModel.NoStartListBean noStartListBean = data.get(position);
        holder.tvPlateNumbers.setText(noStartListBean.getBusPlateNumber());
        holder.tvStationStart.setText(noStartListBean.getStartName());
        holder.tvStationEnd.setText(noStartListBean.getEndName());
        holder.tvGoOutData.setText(getStringDate(noStartListBean.getDate()));


        String stationStartTime = setArriveStationTime(noStartListBean.getStartTime());
        String stationEndTime = setArriveStationTime(noStartListBean.getEndTime());
        holder.startTime.setText(stationStartTime);
        holder.endTime.setText(stationEndTime);
    }


    private String getStringDate(String dateTime) {
        String year = dateTime.substring(0, 4);
        String month = dateTime.substring(4, 6);
        String day = dateTime.substring(6, dateTime.length());
        return year +"年"+ month+"月"+ day+"日";
    }


    private String setArriveStationTime(String planTime) {
        String substring_1 = planTime.substring(0, 2);
        String substring_2 = planTime.substring(2, planTime.length());
        String time = substring_1 + ":" + substring_2;
        return time;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_plate_numbers)
        TextView tvPlateNumbers;
        @BindView(R.id.tv_go_out_data)
        TextView tvGoOutData;
        @BindView(R.id.tv_station_start)
        TextView tvStationStart;
        @BindView(R.id.icon_arrowhead)
        ImageView iconArrowhead;
        @BindView(R.id.tv_station_end)
        TextView tvStationEnd;
        @BindView(R.id.tv_go_out_start_time)
        TextView startTime;
        @BindView(R.id.tv_go_out_end_time)
        TextView endTime;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }
}
