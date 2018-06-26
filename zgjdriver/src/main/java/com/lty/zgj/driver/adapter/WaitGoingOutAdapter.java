package com.lty.zgj.driver.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.TripListModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.RecyclerAdapter;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/11.
 */

public class WaitGoingOutAdapter extends RecyclerAdapter<String, RecyclerView.ViewHolder> {

    DisplayMetrics dm;
//    private WaitGoingOutItemAdapter goingOutItemAdapter;
//    private WaitGoingOutTravelAdapter waitGoingOutTravelAdapter; //未出行
    private  static WaitGoingOutItemAdapter mSurplusAdapter;
    private  static WaitGoingOutTravelAdapter mTravelAdapter;
    private  static TripListModel mtripListModel;
    public static enum ITEM_TYPE {
        ITEM_TYPE_surplus, //剩余的行程
        ITEM_TYPE_travel   //未出行的
    }

    public WaitGoingOutAdapter(Context context, WaitGoingOutItemAdapter surplusAdapter, WaitGoingOutTravelAdapter travelAdapter,
                               TripListModel tripListModel) {
        super(context);
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.mSurplusAdapter = surplusAdapter;
        this.mTravelAdapter = travelAdapter;
        this.mtripListModel = tripListModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_TYPE.ITEM_TYPE_surplus.ordinal()){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.surplus_rv,parent,false);
            setRvHeight(view);

            return new ViewSurplusHolder(view);

        }else if(viewType == ITEM_TYPE.ITEM_TYPE_travel.ordinal()){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.travel_rv,parent,false);
            setRvHeight(view);
            return new ViewTravelHolder(view);

        }
        return null;
    }

    /**
     *解决嵌套Rv item 显示不全
     * @param view
     */
    private void setRvHeight(View view) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        List<TripListModel.NoStartListBean> noStartList = mtripListModel.getNoStartList();
        List<TripListModel.TodayListBean> todayList = mtripListModel.getTodayList();






        if(holder instanceof ViewSurplusHolder){
            ((ViewSurplusHolder) holder).surplusRe.setNestedScrollingEnabled(false);
            setLayoutManager(((ViewSurplusHolder) holder).surplusRe);
            ((ViewSurplusHolder) holder).surplusRe.setAdapter(mSurplusAdapter);
            if(todayList != null && todayList.size() >0){
                ((ViewSurplusHolder) holder).tvSurplus.setText("今日"+(getSysTime())+"行程    "+todayList.size()+"趟");
            }else {
                ((ViewSurplusHolder) holder).tvSurplus.setText("今日"+(getSysTime())+"行程    "+todayList.size()+"趟");
            }


            //今日(05月08日)行程  1趟

        } else if(holder instanceof ViewTravelHolder) {
            ((ViewTravelHolder) holder).travelRe.setNestedScrollingEnabled(false);
            setLayoutManager(((ViewTravelHolder) holder).travelRe);
            ((ViewTravelHolder) holder).travelRe.setAdapter(mTravelAdapter);
            ((ViewTravelHolder) holder).tvTravel.setText("其余未出行    "+noStartList.size()+"趟");
        }
    }

    private  String getSysTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String formatTime = simpleDateFormat.format(date);
        return formatTime;
    }

    private String getStringDate(String dateTime) {
        String year = dateTime.substring(0, 4);
        String month = dateTime.substring(4, 6);
        String day = dateTime.substring(6, dateTime.length());
        return  month+"月"+ day+"日";
    }



    @Override
    public int getItemViewType(int position) {
        //Enum类提供了一个ordinal()方法，返回枚举类型的序数，这里ITEM_TYPE.ITEM1.ordinal()代表0， ITEM_TYPE.ITEM2.ordinal()代表1
        if(position == 0){
            return ITEM_TYPE.ITEM_TYPE_surplus.ordinal();
        }else if (position == 1){
            return ITEM_TYPE.ITEM_TYPE_travel.ordinal();
        }

        return 0;
    }




//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }

    public class ViewTravelHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.travel_re)
        XRecyclerView travelRe;
        @BindView(R.id.tv_travel)
        TextView tvTravel;

        public ViewTravelHolder(View view) {
            super(view);
            KnifeKit.bind(this, view);
            AutoUtils.auto(view);
        }
    }

    public class ViewSurplusHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.surplus_re)
        XRecyclerView surplusRe;
        @BindView(R.id.tv_surplus)
        TextView tvSurplus;

        public ViewSurplusHolder(View view) {
            super(view);
            KnifeKit.bind(this, view);
            AutoUtils.auto(view);
        }
    }


//    public WaitGoingOutItemAdapter getSurplusAdapter() {
//        if(goingOutItemAdapter == null){
//
//            goingOutItemAdapter = new WaitGoingOutItemAdapter(context);
//
//        }else {
//            goingOutItemAdapter.notifyDataSetChanged();
//        }
//
//        return goingOutItemAdapter;
//
//    }
//
//
//    public WaitGoingOutTravelAdapter getTravelAdapter() {
//        if(waitGoingOutTravelAdapter == null){
//            waitGoingOutTravelAdapter = new WaitGoingOutTravelAdapter(context);
//
//        }else {
//            waitGoingOutTravelAdapter.notifyDataSetChanged();
//        }
//
//        return waitGoingOutTravelAdapter;
//
//    }
//
    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);

    }

}
