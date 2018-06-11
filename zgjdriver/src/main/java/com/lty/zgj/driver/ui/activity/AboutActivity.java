package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class AboutActivity extends AbsBaseWebSocketActivity {

    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.terms_of_service)
    AutoRelativeLayout termsOfService;
    @BindView(R.id.version_introduction)
    AutoRelativeLayout versionIntroduction;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        title.setText("关于坐公交");
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


    /**
     *
     * @param v
     */
    @OnClick({
            R.id.terms_of_service,
            R.id.version_introduction,

    })

    public void onClickEventSet(View v) {
        switch (v.getId()) {
            case R.id.terms_of_service:
                TermsOfServiceActivity.launch(context);
                break;
            case R.id.version_introduction:
                VersionIntroductionActivity.launch(context);
                break;

        }
    }


    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(AboutActivity.class)
                .launch();
    }

}
