package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.DepartModel;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.RecyclerAdapter;

/**
 * Created by Administrator on 2018/6/12.
 */

public class DepartAdapter extends RecyclerAdapter<DepartModel.ListBean, DepartAdapter.ViewHolder> {


    private int TYPE_ICON = 0;
    private int TYPE_NORMAL = 1;
    private View mIconView;
    private View view;
    private int TAG_DEPART = 3;

    private int defItem = -1;


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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_ICON) {
            return;
        }

        final int pos = getRealPosition(holder);

        if (data.size() > 0) {


            final DepartModel.ListBean departModel = data.get(pos);

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

            if (pos == 1) {
                setGone(holder.dashed_icon_2);
            } else if (pos == 2) {
                setGone(holder.dashed_icon_1);
            }

            if(data.size() == 2){
                mIconView.setVisibility(View.GONE);
            }

            String stationTime = setArriveStationTime(departModel);
            holder.tvDepartTime.setText(stationTime);
            holder.tvStation.setText(departModel.getStationName());
            int stationNo = departModel.getPeopleCount();
            holder.tvStationPerson.setText(String.valueOf(stationNo)+"人");

            if(defItem != -1){
                if (position == defItem ) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFEDECFE"));
                } else {
                    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRecItemClick().onItemClick(position, departModel, TAG_DEPART, holder);

                    defItem  = position;
                    notifyDataSetChanged();
                }
            });


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
        @BindView(R.id.icon_oval)
        ImageView iconOval;
        @BindView(R.id.dashed_1)
        ImageView dashed_icon_1;
        @BindView(R.id.dashed_2)
        ImageView dashed_icon_2;
        @BindView(R.id.tv_depart_time)
        TextView tvDepartTime;
        @BindView(R.id.tv_station)
        TextView tvStation;
        @BindView(R.id.tv_station_person)
        TextView tvStationPerson;
        @BindView(R.id.al_item)
        AutoLinearLayout autoRela;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mIconView) return;
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }


}
