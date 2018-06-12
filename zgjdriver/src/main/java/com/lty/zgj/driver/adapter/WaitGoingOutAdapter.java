package com.lty.zgj.driver.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lty.zgj.driver.R;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.KnifeKit;
import cn.droidlover.xrecyclerview.RecyclerAdapter;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/11.
 */

public class WaitGoingOutAdapter extends RecyclerAdapter<String, RecyclerView.ViewHolder> {

    DisplayMetrics dm;
    private WaitGoingOutItemAdapter goingOutItemAdapter;
    private WaitGoingOutTravelAdapter waitGoingOutTravelAdapter;

    public static enum ITEM_TYPE {
        ITEM_TYPE_surplus, //剩余的行程
        ITEM_TYPE_travel   //未出行的
    }

    public WaitGoingOutAdapter(Context context) {
        super(context);
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
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

        if(holder instanceof ViewSurplusHolder){
            ((ViewSurplusHolder) holder).surplusRe.setNestedScrollingEnabled(false);
            setLayoutManager(((ViewSurplusHolder) holder).surplusRe);
            ((ViewSurplusHolder) holder).surplusRe.setAdapter(getSurplusAdapter());

        } else if(holder instanceof ViewTravelHolder) {
            ((ViewTravelHolder) holder).travelRe.setNestedScrollingEnabled(false);
            setLayoutManager(((ViewTravelHolder) holder).travelRe);
            ((ViewTravelHolder) holder).travelRe.setAdapter(getTravelAdapter());
        }
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
        public ViewTravelHolder(View view) {
            super(view);
            KnifeKit.bind(this, view);
            AutoUtils.auto(view);
        }
    }

    public class ViewSurplusHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.surplus_re)
        XRecyclerView surplusRe;

        public ViewSurplusHolder(View view) {
            super(view);
            KnifeKit.bind(this, view);
            AutoUtils.auto(view);
        }
    }


    public WaitGoingOutItemAdapter getSurplusAdapter() {
        if(goingOutItemAdapter == null){

            goingOutItemAdapter = new WaitGoingOutItemAdapter(context);

        }else {
            goingOutItemAdapter.notifyDataSetChanged();
        }

        return goingOutItemAdapter;

    }


    public WaitGoingOutTravelAdapter getTravelAdapter() {
        if(waitGoingOutTravelAdapter == null){
            waitGoingOutTravelAdapter = new WaitGoingOutTravelAdapter(context);

        }else {
            waitGoingOutTravelAdapter.notifyDataSetChanged();
        }

        return waitGoingOutTravelAdapter;

    }

    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);

    }

}
