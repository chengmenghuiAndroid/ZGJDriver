package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.DriverNoticeInfoDetailModel;
import com.lty.zgj.driver.core.tool.TimeUtils;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.weight.StatusBarUtils;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidbase.router.Router;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/6/26.
 */

public class MessageDetailActivity extends BaseXActivity {
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.profile_image_type)
    CircleImageView profileImageType;
    @BindView(R.id.au_al)
    AutoLinearLayout auAl;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_msg_detail_date)
    TextView tvMsgDetailDate;
    @BindView(R.id.tv_msg_detail_time)
    TextView tvMsgDetailTime;
    @BindView(R.id.tv_msg_detail_title)
    TextView tvMsgDetailTitle;
    @BindView(R.id.tv_msg_detail_content)
    TextView tvMsgDetailContent;

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        tvTitle.setText("通知/提醒");
        getUiDelegate().visible(true, navButton);
        int itemId = getIntent().getIntExtra("itemId", 0);

        fetchDetailData(itemId);
    }

    private void fetchDetailData(int itemId) {

        ObjectLoader.getInstance().getDriverNoticeInfoDetailData(new ProgressSubscriber<DriverNoticeInfoDetailModel>(new SubscriberOnNextListener<DriverNoticeInfoDetailModel>() {
            @Override
            public void onNext(DriverNoticeInfoDetailModel driverNoticeInfoDetailModel) {
                if(driverNoticeInfoDetailModel != null){
                    tvMsgDetailTitle.setText(driverNoticeInfoDetailModel.getTitle());
                    tvMsgDetailContent.setText(driverNoticeInfoDetailModel.getContent());
                    String createTime = driverNoticeInfoDetailModel.getCreateTime();
                    String ymdHms = TimeUtils.getYMDHms(createTime);
                    tvMsgDetailDate.setText(ymdHms);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        }, context), itemId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    public static void launch(Activity activity, int type, int itemId) {
        Router.newIntent(activity)
                .to(MessageDetailActivity.class)
                .putInt("type", type)
                .putInt("itemId", itemId)
                .launch();
    }

    public void back(View view) {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
