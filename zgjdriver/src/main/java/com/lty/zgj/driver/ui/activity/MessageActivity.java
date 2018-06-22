package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.adapter.MessageAdapter;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.lty.zgj.driver.weight.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.droidlover.xdroidbase.router.Router;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2018/6/14.
 */

public class MessageActivity extends AbsBaseWebSocketActivity{


    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    private MessageAdapter messageAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mssage;
    }

    @Override
    protected void initView() {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        title.setText("消息");
        EventBus.getDefault().register(this);
        closeRoundProgressDialog();//关闭加载对话框
        XRecyclerView recyclerView = contentLayout.getRecyclerView();
        setLayoutManager(recyclerView);
        recyclerView.setAdapter(getAdapter());
        recyclerView.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore(int page) {

            }
        });

        recyclerView.useDefLoadMoreView();
    }

    private MessageAdapter getAdapter() {
        if(messageAdapter == null){

            messageAdapter = new MessageAdapter(context);

        }else {
            messageAdapter.notifyDataSetChanged();
        }
        return messageAdapter;
    }


    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        closeRoundProgressDialog();//关闭加载对话框
    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {
        closeRoundProgressDialog();//关闭加载对话框
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(MessageActivity.class)
                .launch();
    }
}
