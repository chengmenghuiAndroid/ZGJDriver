package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.HistoricalJourneyModel;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.kit.KnifeKit;

/**
 * Created by Administrator on 2018/6/15.
 */

public class HistoricalJourneyAdapter extends SimpleRecAdapter<HistoricalJourneyModel.RecordsBean, HistoricalJourneyAdapter.ViewHolder> {


    private int TAG_ITEM = 1;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HistoricalJourneyModel.RecordsBean recordsBean = data.get(position);
        int status = recordsBean.getStatus();

        if (status == 0) {
            holder.tvStatus.setText("未开始");
        } else if (status == 1) {
            holder.tvStatus.setText("进行中");
        } else if (status == 2) {
            holder.tvStatus.setText("已结束");
        } else if (status == 3) {
            holder.tvStatus.setText("取消");
        }


        holder.tvPlateNumbers.setText(recordsBean.getBusNum());
        String dateTime = recordsBean.getDate();
        String dataYmd = getString(dateTime);
        holder.tvDataTime.setText(dataYmd);
        holder.tvRouteNameLeft.setText(recordsBean.getStartName());
        holder.tvRouteNameRight.setText(recordsBean.getEndName());


        String endTime = recordsBean.getEndTime();

        holder.tvTime.setText(setArriveStationTime(endTime));
        String startTime = recordsBean.getStartTime();

        holder.tvYmd.setText(setArriveStationTime(startTime));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecItemClick().onItemClick(position, recordsBean, TAG_ITEM, holder);
            }
        });
    }

    private String setArriveStationTime(String planTime) {
        String substring_1 = planTime.substring(0, 2);
        String substring_2 = planTime.substring(2, planTime.length());
        String time = substring_1 + ":" + substring_2;
        return time;
    }

    @NonNull
    private String getString(String dateTime) {
        String year = dateTime.substring(0, 4);
        String month = dateTime.substring(4, 6);
        String day = dateTime.substring(6, dateTime.length());
        return year + "年" + month + "月" + day + "日";
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_ymd)
        TextView tvYmd;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_routeName_left)
        TextView tvRouteNameLeft;
        @BindView(R.id.tv_routeName_right)
        TextView tvRouteNameRight;
        @BindView(R.id.tv_plate_numbers)
        TextView tvPlateNumbers;
        @BindView(R.id.tv_data_time)
        TextView tvDataTime;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }
}
