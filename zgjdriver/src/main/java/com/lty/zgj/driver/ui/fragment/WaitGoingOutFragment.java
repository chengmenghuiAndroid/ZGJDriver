package com.lty.zgj.driver.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketFragment;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.adapter.WaitGoingOutAdapter;
import com.lty.zgj.driver.websocketdemo.WebSocketService;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/6.
 * 待发车
 */

public class WaitGoingOutFragment extends AbsBaseWebSocketFragment {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;

    private WaitGoingOutAdapter waitGoingOutAdapter;


    @Override
    protected void initView() {

    }

    /**
     * 返回Fragment layout资源ID
     *
     * @return
     */
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.wait_going_out_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        contentLayout.getRecyclerView().setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView().setAdapter(getAdapter());
        contentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore(int page) {

            }
        });

        contentLayout.getRecyclerView().useDefLoadMoreView();
    }

    private WaitGoingOutAdapter getAdapter() {
        if(waitGoingOutAdapter == null){
            waitGoingOutAdapter = new WaitGoingOutAdapter(context);
        }else {
            waitGoingOutAdapter.notifyDataSetChanged();
        }

        return waitGoingOutAdapter;

    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {

    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {

    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }
}
