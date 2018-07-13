package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.adapter.MessageAdapter;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.DriverNoticeInfoModel;
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
 * Created by Administrator on 2018/6/14.
 */

public class MessageActivity extends BaseXActivity{


    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private MessageAdapter messageAdapter;
    private int driverId;

    @Override
    public void initData(Bundle savedInstanceState) {
        title.setText("消息");
        navButton.setVisibility(View.VISIBLE);
        initRv();
        driverId = SharedPref.getInstance(context).getInt(Constant.DRIVER_ID, 0);
        fetchDriverNoticeInfoData(driverId, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mssage;
    }


    private void initRv() {
        XRecyclerView recyclerView = contentLayout.getRecyclerView();
        setLayoutManager(recyclerView);
        recyclerView.setAdapter(getAdapter());
        recyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                fetchDriverNoticeInfoData(driverId, 1);
            }

            @Override
            public void onLoadMore(int page) {


                fetchDriverNoticeInfoData(driverId, page);

            }
        });

        recyclerView.useDefLoadMoreView();
    }

    private void fetchDriverNoticeInfoData(int driverId, int page) {
        ObjectLoader.getInstance().getDriverNoticeInfoData(new ProgressSubscriber<DriverNoticeInfoModel>(new SubscriberOnNextListener<DriverNoticeInfoModel>() {
            @Override
            public void onNext(DriverNoticeInfoModel driverNoticeInfoModel) {
                List<DriverNoticeInfoModel.RecordsBean> records = driverNoticeInfoModel.getRecords();
                int pages = driverNoticeInfoModel.getPages();
                int currentPage = driverNoticeInfoModel.getCurrent();

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

            @Override
            public void onError(Throwable e) {
                Log.e("MessageActivity" , "Throwable"+e.getMessage());
            }
        }, context),driverId, page);

    }

    private MessageAdapter getAdapter() {
        if(messageAdapter == null){
            messageAdapter = new MessageAdapter(context);
            messageAdapter.setRecItemClick(recItemClick);

        }else {
            messageAdapter.notifyDataSetChanged();
        }
        return messageAdapter;
    }


    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    public static void launch(Activity activity) {
        SharedPref.getInstance(activity).putInt(Constant.DOT_KET, Constant.HIDE_DOT);//点击状态栏进去消息详情 ,小红点隐藏
        Router.newIntent(activity)
                .to(MessageActivity.class)
                .launch();
    }

    private RecyclerItemCallback<DriverNoticeInfoModel.RecordsBean,MessageAdapter.ViewHolder> recItemClick = new RecyclerItemCallback<DriverNoticeInfoModel.RecordsBean, MessageAdapter.ViewHolder>() {

        @Override
        public void onItemClick(int position, DriverNoticeInfoModel.RecordsBean model, int tag, MessageAdapter.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);

            MessageDetailActivity.launch(context, Integer.parseInt(model.getType()), model.getId());
        }
    };

    public void back(View view) {
        finish();
    }

}
