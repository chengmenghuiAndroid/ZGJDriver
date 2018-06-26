package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.TripListModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xdroidbase.base.SimpleRecAdapter;

/**
 * Created by Administrator on 2018/6/12.
 */

public class WaitGoingOutItemAdapter extends SimpleRecAdapter<TripListModel.TodayListBean, WaitGoingOutItemAdapter.ViewHolder> {


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
        TripListModel.TodayListBean todayList = data.get(position);
        List<TripListModel.TodayListBean.ListBean> beanList = todayList.getList();

        setStationInfo(holder, beanList);


    }

    private void setStationInfo(ViewHolder holder, List<TripListModel.TodayListBean.ListBean> beanList) {
        TripListModel.TodayListBean.ListBean listBean = beanList.get(0);
        String planTime = listBean.getPlanTime();
        holder.surplusTime1.setText(setArriveStationTime(planTime));
        holder.surplusStation1.setText(listBean.getStationName());


        TripListModel.TodayListBean.ListBean listBean_1 = beanList.get(1);
        String planTime_1 = listBean_1.getPlanTime();
        holder.surplusTime2.setText(setArriveStationTime(planTime_1));
        holder.surplusStation2.setText(listBean_1.getStationName());

        TripListModel.TodayListBean.ListBean listBean_3 = beanList.get(beanList.size() - 2);
        String planTime_3 = listBean_3.getPlanTime();
        holder.surplusTime3.setText(setArriveStationTime(planTime_3));
        holder.surplusStation3.setText(listBean_3.getStationName());


        TripListModel.TodayListBean.ListBean listBean_4 = beanList.get(beanList.size() - 1);
        String planTime_4 = listBean_4.getPlanTime();
        holder.surplusTime4.setText(setArriveStationTime(planTime_4));
        holder.surplusStation4.setText(listBean_4.getStationName());

    }

    private String setArriveStationTime(String planTime) {
        String substring_1 = planTime.substring(0, 2);
        String substring_2 = planTime.substring(2, planTime.length());
        String time = substring_1 + ":" + substring_2;
        return time;
    }

    /**
     * 设置到站时间
     * @param holder
     * @param todayList
     * @param textView
     */
    private void setPlanTime(ViewHolder holder, TripListModel.TodayListBean todayList, TextView textView) {
        TripListModel.TodayListBean.ListBean listBean = todayList.getList().get(0);
        textView.setText(setArriveStationTime(listBean));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.surplus_time_1)
        TextView surplusTime1;
        @BindView(R.id.surplus_station_1)
        TextView surplusStation1;
        @BindView(R.id.surplus_time_2)
        TextView surplusTime2;
        @BindView(R.id.surplus_station_2)
        TextView surplusStation2;
        @BindView(R.id.surplus_time_3)
        TextView surplusTime3;
        @BindView(R.id.surplus_station_3)
        TextView surplusStation3;
        @BindView(R.id.surplus_time_4)
        TextView surplusTime4;
        @BindView(R.id.surplus_station_4)
        TextView surplusStation4;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }

    /**
     * @param departModel
     * @return
     */
    private String setArriveStationTime(TripListModel.TodayListBean.ListBean departModel) {
        String planTime = departModel.getPlanTime();
        String substring_1 = planTime.substring(0, 2);
        String substring_2 = planTime.substring(2, planTime.length());
        String time = substring_1 + ":" + substring_2;
        return time;
    }
}
