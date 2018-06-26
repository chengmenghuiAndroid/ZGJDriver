package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.DepartModel;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.kit.KnifeKit;

/**
 * Created by Administrator on 2018/6/22.
 */

public class DepartFullAdapter extends SimpleRecAdapter<DepartModel.ListBean, DepartFullAdapter.ViewHolder>{
    public DepartFullAdapter(Context context) {
        super(context);
    }

    @Override
    public DepartFullAdapter.ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.depart_adapter;
    }

    @Override
    public void onBindViewHolder(DepartFullAdapter.ViewHolder holder, int position) {
        DepartModel.ListBean departModel = data.get(position);


        String stationTime = setArriveStationTime(departModel);
        holder.tvDepartTime.setText(stationTime);
        holder.tvStation.setText(departModel.getStationName());
        int stationNo = departModel.getStationNo();
        holder.tvStationPerson.setText(String.valueOf(stationNo));


        if (position == 0) {
            setInvisible(holder.dashed_icon_1);
            holder.iconOval.setImageDrawable(context.getResources().getDrawable(R.mipmap.start));
        } else if (position == data.size() - 1) {
            setInvisible(holder.dashed_icon_2);
            holder.iconOval.setImageDrawable(context.getResources().getDrawable(R.mipmap.over));
        } else {
            setVisible(holder.dashed_icon_1);
            setVisible(holder.dashed_icon_2);
            holder.iconOval.setImageDrawable(context.getResources().getDrawable(R.drawable.shape_oval_green));
        }


    }

    /**
     * @param departModel
     * @return
     */
    private String setArriveStationTime(DepartModel.ListBean departModel) {
        String planTime = departModel.getPlanTime();
        String substring_1 = planTime.substring(0, 2);
        String substring_2 = planTime.substring(2, planTime.length());
        String time = substring_1 + ":" + substring_2;
        return time;
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
        @BindView(R.id.tv_depart_time)
        TextView tvDepartTime;
        @BindView(R.id.tv_station)
        TextView tvStation;
        @BindView(R.id.tv_station_person)
        TextView tvStationPerson;


        public ViewHolder(View itemView) {

            super(itemView);
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }
}
