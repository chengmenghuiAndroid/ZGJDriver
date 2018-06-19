package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;

import butterknife.BindView;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/19.
 */

public class DepartOverActivity extends BaseXActivity {
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    public void initData(Bundle savedInstanceState) {
        tvTitle.setText("发车完成");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_depart_over;
    }


    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(DepartOverActivity.class)
                .launch();
    }

}
