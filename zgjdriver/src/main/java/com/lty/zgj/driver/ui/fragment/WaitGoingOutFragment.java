package com.lty.zgj.driver.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.adapter.WaitGoingOutAdapter;
import com.lty.zgj.driver.adapter.WaitGoingOutItemAdapter;
import com.lty.zgj.driver.adapter.WaitGoingOutTravelAdapter;
import com.lty.zgj.driver.base.BaseXFragment;
import com.lty.zgj.driver.bean.TripListModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.event.MessageEvent;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.ui.activity.PendingTripActivity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/6.
 * 待发车
 */

public class WaitGoingOutFragment extends BaseXFragment {

    @BindView(R.id.contentLayout)
    XRecyclerView contentLayout;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwiperefreshlayout;
    @BindView(R.id.au_ar_loading)
    AutoLinearLayout auLLoading;
    @BindView(R.id.au_autoLinearLayout)
    AutoLinearLayout auAutoLinearLayout;

    private WaitGoingOutItemAdapter goingOutItemAdapter; //
    private WaitGoingOutTravelAdapter waitGoingOutTravelAdapter; //未出行

    private WaitGoingOutAdapter waitGoingOutAdapter;
    private TripListModel tripListModelData;
    private WaitGoingOutItemAdapter surplusAdapter;
    private WaitGoingOutTravelAdapter travelAdapter;
    private int driverId;
    private List<TripListModel.TodayListBean> list = new ArrayList<>();
    private List<TripListModel.TodayListBean.ListBean> todayItemDates;
    private boolean isRefresh = false;//是否刷新中

    @Override
    public void initData(Bundle savedInstanceState) {
        initRv();
    }


    private void initRv() {
        contentLayout.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
        setLayoutManager(contentLayout);

        surplusAdapter = getSurplusAdapter();
        travelAdapter = getTravelAdapter();


        mSwiperefreshlayout.setOnRefreshListener(refreshListener);

        mSwiperefreshlayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);

    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!isRefresh) {
                isRefresh = true;
                //模拟加载网络数据，这里设置4秒，正好能看到4色进度条
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        //显示或隐藏刷新进度条
                        mSwiperefreshlayout.setRefreshing(false);
                        fetchTripListData(driverId);
                        isRefresh = false;
                    }
                }, 4000);
            }

        }
    };

    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.wait_going_out_fragment;
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    @Override
    protected void lazyLoad() {
        driverId = SharedPref.getInstance(context).getInt(Constant.DRIVER_ID, 0);
        fetchTripListData(driverId);
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以调用此方法
     */
    @Override
    protected void stopLoad() {

    }

    private void fetchTripListData(int id) {
        ObjectLoader.getInstance().getTripListModelData(new ProgressSubscriber<TripListModel>(new SubscriberOnNextListener<TripListModel>() {
            @Override
            public void onNext(TripListModel tripListModel) {

                if(tripListModel != null){
                    auLLoading.setVisibility(View.GONE);
                    auAutoLinearLayout.setVisibility(View.VISIBLE);

                    waitGoingOutAdapter = new WaitGoingOutAdapter(context, surplusAdapter, travelAdapter, tripListModel);
                    contentLayout.setAdapter(waitGoingOutAdapter);

                    List<TripListModel.NoStartListBean> noStartList = tripListModel.getNoStartList();
                    List<TripListModel.TodayListBean> todayList = tripListModel.getTodayList();

                    getSurplusAdapter().setData(todayList);
                    getTravelAdapter().setData(noStartList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        }, context),id);

    }
//    private void setStation() {
//        if (departModelList != null && departModelList.size() > 0) {
//            if (size < 4) {
//
//                list.clear();
//                list.addAll(departModelList);
//                getAdapter().setData(list);
//            } else {
//                list.clear();
//                list.add(departModelList.get(0));
//                list.add(departModelList.get(1));
//                list.add(departModelList.get(size - 2));
//                list.add(departModelList.get(size - 1));
//                getAdapter().setData(list);
//            }
//        }
//    }

    //今日行程
    public WaitGoingOutItemAdapter getSurplusAdapter() {
        if(goingOutItemAdapter == null){
            goingOutItemAdapter = new WaitGoingOutItemAdapter(context);
            goingOutItemAdapter.setRecItemClick(recItemClick);
        }else {
            goingOutItemAdapter.notifyDataSetChanged();
        }

        return goingOutItemAdapter;

    }

    private RecyclerItemCallback<TripListModel.TodayListBean,WaitGoingOutItemAdapter.ViewHolder> recItemClick = new RecyclerItemCallback<TripListModel.TodayListBean, WaitGoingOutItemAdapter.ViewHolder>() {
        /**
         * 单击事件
         *
         * @param position 位置
         * @param model    实体
         * @param tag      标签
         * @param holder   控件
         */

        @SuppressLint("ResourceType")
        @Override
        public void onItemClick(int position, TripListModel.TodayListBean model, int tag, WaitGoingOutItemAdapter.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);
            String id = model.getId();
            if(position == 0){
                //点击 条目之后 就加载 待出行详情
                EventBus.getDefault().post(new MessageEvent(id));
            }else {
                PendingTripActivity.launch(context, String.valueOf(id));
            }


        }
    };


    //其余为出行
    public WaitGoingOutTravelAdapter getTravelAdapter() {
        if(waitGoingOutTravelAdapter == null){
            waitGoingOutTravelAdapter = new WaitGoingOutTravelAdapter(context);
            waitGoingOutTravelAdapter.setRecItemClick(GoingOutTraveItemClick);
        }else {
            waitGoingOutTravelAdapter.notifyDataSetChanged();
        }

        return waitGoingOutTravelAdapter;

    }

    private RecyclerItemCallback<TripListModel.NoStartListBean,WaitGoingOutTravelAdapter.ViewHolder> GoingOutTraveItemClick = new RecyclerItemCallback<TripListModel.NoStartListBean, WaitGoingOutTravelAdapter.ViewHolder>() {
        /**
         * 单击事件
         *
         * @param position 位置
         * @param model    实体
         * @param tag      标签
         * @param holder   控件
         */
        @Override
        public void onItemClick(int position, TripListModel.NoStartListBean model, int tag, WaitGoingOutTravelAdapter.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);
             //点击 条目之后 就加载 其余未出行
            String itemId = model.getId();
//            EventBus.getDefault().post(new MessageEvent(model.getId()));
            PendingTripActivity.launch(context, String.valueOf(itemId));
        }
    };

}
