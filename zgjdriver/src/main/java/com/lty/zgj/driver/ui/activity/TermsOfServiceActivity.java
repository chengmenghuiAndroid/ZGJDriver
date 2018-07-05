package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.TicketGuideModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.weight.StatusBarUtils;

import butterknife.BindView;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class TermsOfServiceActivity extends BaseXActivity {

    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_terms_content)
    TextView tvTermsContent;
    @BindView(R.id.tv_terms_title)
    TextView tvTermstitle;

    protected void initView() {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        tvTitle.setText("软件服务条款");
        getUiDelegate().visible(true, navButton);

        String cityCode = SharedPref.getInstance(context).getString(Constant.cityCode, null);

        if (cityCode != null) {

            fetchDateTicketGuide(cityCode, 4);
        }
    }

    private void fetchDateTicketGuide(String cityCode, int guideType) {
        ObjectLoader.getInstance().getticketGuideData(new ProgressSubscriber<TicketGuideModel>(new SubscriberOnNextListener<TicketGuideModel>() {
            @Override
            public void onNext(TicketGuideModel ticketGuideModel) {

                if (ticketGuideModel != null) {
                    tvTermstitle.setText(ticketGuideModel.getTitle());
                    tvTermsContent.setText(Html.fromHtml(ticketGuideModel.getContent()));
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("TermsOfServiceActivity", "--" + e.getMessage());
            }
        }, context), cityCode, guideType);
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(TermsOfServiceActivity.class)
                .launch();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_terms_of_service;
    }

    public void back(View view) {
        finish();
    }


}
