package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.weight.StatusBarUtils;

import butterknife.BindView;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class VersionIntroductionActivity extends BaseXActivity {
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;


    protected void initView() {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        tvTitle.setText("软件版本介绍");
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(VersionIntroductionActivity.class)
                .launch();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_version_introduction;
    }

    public void back(View view) {
        finish();
    }
}
