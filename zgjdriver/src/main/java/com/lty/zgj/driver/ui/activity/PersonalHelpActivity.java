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

public class PersonalHelpActivity extends BaseXActivity {

    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_help_title)
    TextView tvHelpTitle;
    @BindView(R.id.tv_help_content)
    TextView tvHelpContent;


    protected void initView() {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        title.setText("使用帮助");
        getUiDelegate().visible(true, navButton);

        String cityCode = SharedPref.getInstance(context).getString(Constant.cityCode, null);

        if (cityCode != null) {

            fetchDateTicketGuide(cityCode, 3);
        }
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(PersonalHelpActivity.class)
                .launch();
    }



    private void fetchDateTicketGuide(String cityCode, int guideType) {
        ObjectLoader.getInstance().getticketGuideData(new ProgressSubscriber<TicketGuideModel>(new SubscriberOnNextListener<TicketGuideModel>() {
            @Override
            public void onNext(TicketGuideModel ticketGuideModel) {

                if(ticketGuideModel != null){
                    tvHelpTitle.setText(ticketGuideModel.getTitle());
                    tvHelpContent.setText(Html.fromHtml(ticketGuideModel.getContent()));
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("TermsOfServiceActivity", "--"+e.getMessage());
            }
        }, context), cityCode, guideType);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_help;
    }

    public void back(View view) {
        finish();
    }

}
