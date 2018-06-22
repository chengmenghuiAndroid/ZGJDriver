package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.weight.StatusBarUtils;

import butterknife.BindView;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class PersonalInformationActivity extends BaseXActivity {

    @BindView(R.id.tv_title)
    TextView title;

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(PersonalInformationActivity.class)
                .launch();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        title.setText("个人信息");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_information;
    }
}
