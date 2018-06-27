package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.adapter.HistoricalJourneyAdapter;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.HistoricalJourneyModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xdroidbase.router.Router;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/15.
 */

public class HistoricalJourneyActivity extends BaseXActivity {


    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private HistoricalJourneyAdapter historicalJourneyAdapter;
    private int driverId;

    @Override
    public void initData(Bundle savedInstanceState) {
//        StatusBarUtils.with(this)
//                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
//                .init();
        tvTitle.setText("历史行程");
        initAdapter();
        driverId = SharedPref.getInstance(context).getInt(Constant.DRIVER_ID, 0);

        fetchTripDate(driverId, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.historical_journeya_ctivity;
    }

    private void initAdapter() {
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView()
                .setAdapter(getAdapter());
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        fetchTripDate(driverId, 0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        fetchTripDate(driverId, page);
                    }
                });

        contentLayout.getRecyclerView().useDefLoadMoreView();
    }


    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    private HistoricalJourneyAdapter getAdapter() {
        if(historicalJourneyAdapter == null){
            historicalJourneyAdapter = new HistoricalJourneyAdapter(context);

            historicalJourneyAdapter.setRecItemClick(itemClick);
        }else {
            historicalJourneyAdapter.notifyDataSetChanged();
        }

       return historicalJourneyAdapter;
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(HistoricalJourneyActivity.class)
                .launch();
    }

    private void fetchTripDate(int Id , int page) {
        ObjectLoader.getInstance().getTripDate(new ProgressSubscriber<HistoricalJourneyModel>(new SubscriberOnNextListener<HistoricalJourneyModel>() {
            @Override
            public void onNext(HistoricalJourneyModel historicalJourneyModel) {
                if(historicalJourneyModel != null){
                    List<HistoricalJourneyModel.RecordsBean> records = historicalJourneyModel.getRecords();

                    int pages = historicalJourneyModel.getPages();
                    int currentPage = historicalJourneyModel.getCurrent();

                    if(currentPage > 1){
                        getAdapter().addData(records);
                    }else {
                        getAdapter().setData(records);
                    }

                    contentLayout.getRecyclerView().setPage(currentPage, pages);

                    if (getAdapter().getItemCount() < 1) {
                        return;
                    }

                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Throwable", "---"+ e.getMessage());
            }
        }, context), Id, page);
    }

    private RecyclerItemCallback<HistoricalJourneyModel.RecordsBean,HistoricalJourneyAdapter.ViewHolder> itemClick = new RecyclerItemCallback<HistoricalJourneyModel.RecordsBean, HistoricalJourneyAdapter.ViewHolder>() {
        /**
         * 单击事件
         *
         * @param position 位置
         * @param model    实体
         * @param tag      标签
         * @param holder   控件
         */
        @Override
        public void onItemClick(int position, HistoricalJourneyModel.RecordsBean model, int tag, HistoricalJourneyAdapter.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);
            int status = model.getStatus();
            int tripNo = model.getTripNo();
            String id = model.getId();

            HistoricalJourneyDetailActivity.launch(context, status, tripNo, id);
        }
    };

}
