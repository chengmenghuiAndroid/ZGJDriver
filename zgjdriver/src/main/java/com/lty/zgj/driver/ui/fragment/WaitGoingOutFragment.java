package com.lty.zgj.driver.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/6.
 * 待发车
 */

public class WaitGoingOutFragment extends BaseXFragment {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;

    private WaitGoingOutItemAdapter goingOutItemAdapter; //
    private WaitGoingOutTravelAdapter waitGoingOutTravelAdapter; //未出行

    private WaitGoingOutAdapter waitGoingOutAdapter;
    private TripListModel tripListModelData;
    private WaitGoingOutItemAdapter surplusAdapter;
    private WaitGoingOutTravelAdapter travelAdapter;
    private int driverId;


    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initRv();
    }


    private void initRv() {
        contentLayout.getRecyclerView().setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
        setLayoutManager(contentLayout.getRecyclerView());

        contentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore(int page) {

            }
        });


        surplusAdapter = getSurplusAdapter();
        travelAdapter = getTravelAdapter();



//        contentLayout.getRecyclerView().useDefLoadMoreView();
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

                waitGoingOutAdapter = new WaitGoingOutAdapter(context, surplusAdapter, travelAdapter, tripListModel);
                contentLayout.getRecyclerView().setAdapter(waitGoingOutAdapter);

                List<TripListModel.NoStartListBean> noStartList = tripListModel.getNoStartList();
                List<TripListModel.TodayListBean> todayList = tripListModel.getTodayList();


                if(todayList != null && todayList.size() >0){
                    getSurplusAdapter().setData(todayList);

                }

                if(noStartList != null && noStartList.size() >0){
                    getTravelAdapter().setData(noStartList);

                }


            }

            @Override
            public void onError(Throwable e) {

            }
        }, context),id);

    }

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
            //点击 条目之后 就加载 待出行详情
            EventBus.getDefault().post(new MessageEvent(model.getId()));


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
            EventBus.getDefault().post(new MessageEvent(model.getId()));
        }
    };

}
