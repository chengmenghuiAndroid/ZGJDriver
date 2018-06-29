package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.UserInfoModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.GsonUtils;
import com.lty.zgj.driver.weight.StatusBarUtils;

import butterknife.BindView;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class PersonalInformationActivity extends BaseXActivity {

    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_tel_numbers)
    TextView tvTelNumbers;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.nav_button)
    ImageView navButton;

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

        getUiDelegate().visible(true, navButton);
        title.setText("个人信息");
        String userInfoData = SharedPref.getInstance(context).getString(Constant.USER_INFO, null);
        UserInfoModel loginModel = GsonUtils.parserJsonToArrayBean(userInfoData, UserInfoModel.class);
        String account = loginModel.getAccount();
        String name = loginModel.getName();
        tvUserName.setText(name);
        tvTelNumbers.setText(account);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_information;
    }


    public void back(View view) {
        finish();
    }
}
