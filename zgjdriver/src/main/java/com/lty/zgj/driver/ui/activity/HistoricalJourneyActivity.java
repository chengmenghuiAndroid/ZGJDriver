package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.adapter.HistoricalJourneyAdapter;
import com.lty.zgj.driver.websocketdemo.WebSocketService;

import butterknife.BindView;
import cn.droidlover.xdroidbase.router.Router;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/15.
 */

public class HistoricalJourneyActivity extends AbsBaseWebSocketActivity {


    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private HistoricalJourneyAdapter historicalJourneyAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.historical_journeya_ctivity;
    }

    @Override
    protected void initView() {
        tvTitle.setText("历史行程");
        initAdapter();
    }


    private void initAdapter() {
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView()
                .setAdapter(getAdapter());
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {

                    }

                    @Override
                    public void onLoadMore(int page) {

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

        }else {
            historicalJourneyAdapter.notifyDataSetChanged();
        }

       return historicalJourneyAdapter;
    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {

    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {

    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(HistoricalJourneyActivity.class)
                .launch();
    }

}
