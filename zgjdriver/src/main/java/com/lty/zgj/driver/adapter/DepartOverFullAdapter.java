package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.kit.KnifeKit;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DepartOverFullAdapter extends SimpleRecAdapter<HistoricalJourneyDetailModel.StationsBean, DepartOverFullAdapter.ViewHolder> {

    @BindView(R.id.tv_depart_time)
    TextView tvDepartTime;
    @BindView(R.id.tv_station)
    TextView tvStation;
    @BindView(R.id.al_item)
    AutoLinearLayout alItem;

    public DepartOverFullAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.depart_over_adapter;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoricalJourneyDetailModel.StationsBean stationsBean = data.get(position);

        String stationTime = setArriveStationTime(stationsBean);
        holder.tvDepartTime.setText(stationTime);
        holder.tvStation.setText(stationsBean.getStationName());
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_depart_time)
        TextView tvDepartTime;
        @BindView(R.id.tv_station)
        TextView tvStation;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }
}
