package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.core.tool.AppUtils;
import com.lty.zgj.driver.weight.StatusBarUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class AboutActivity extends BaseXActivity {

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
    @BindView(R.id.version_check)
    AutoRelativeLayout versionCheck;
    @BindView(R.id.tv_versionName)
    TextView tvVersionName;

    /**
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

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        title.setText("关于坐公交");
        getUiDelegate().visible(true, navButton);
        tvVersionName.setText(AppUtils.getAppName(context)+"_2");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }


    public void back(View view) {
        finish();
    }


}
