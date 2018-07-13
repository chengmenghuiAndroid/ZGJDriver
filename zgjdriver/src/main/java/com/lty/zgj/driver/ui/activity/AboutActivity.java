package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.AppVersionModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.AllenVersionUtils;
import com.lty.zgj.driver.core.tool.AppUtils;
import com.lty.zgj.driver.core.tool.ShowDialogRelative;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.weight.StatusBarUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidbase.cache.SharedPref;
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
    @BindView(R.id.tv_versionName_tip)
    TextView tvVersionNameTip;
    @BindView(R.id.au_versionName_tip)
    AutoLinearLayout auVersionNameTip;

    private String versionName;
    private int compareVersion;
    private String versionUrl;
    private String contentDesc;

    /**
     * @param v
     */
    @OnClick({
            R.id.terms_of_service,
            R.id.version_check,

    })

    public void onClickEventSet(View v) {
        switch (v.getId()) {
            case R.id.terms_of_service:
                TermsOfServiceActivity.launch(context);
                break;

                case R.id.version_check:
                    if(!TextUtils.isEmpty(versionUrl)){
                        updateApp();
                    }else {
                        ShowDialogRelative.toastDialog(context, "应用下载地址为空");
                    }
                break;

        }
    }

    private void updateApp() {
        Spanned spanned = Html.fromHtml(contentDesc);
        String s = spanned.toString();
        AllenVersionUtils.getInstance().checkLastVersion(compareVersion, context,versionUrl, s);
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
        tvVersionName.setText("V"+AppUtils.getVersionName(context)+"_3");
        versionName = AppUtils.getVersionName(context);
        auVersionNameTip.setEnabled(false);
        String cityCode = SharedPref.getInstance(context).getString(Constant.cityCode, null);
        fetchLatestVersionDate(cityCode);
    }

    private void fetchLatestVersionDate(String cityCode) {

        ObjectLoader.getInstance().getFndLatestVersionDate(new ProgressSubscriber<>(new SubscriberOnNextListener<AppVersionModel>() {
            @Override
            public void onNext(AppVersionModel appVersionModel) {

                if(appVersionModel != null){
                    String versionId = appVersionModel.getVersionId();
                    contentDesc = appVersionModel.getContentDesc();
                    if(appVersionModel.getVersionUrl() != null){
                        versionUrl = appVersionModel.getVersionUrl();
                    }
                    compareVersion = AppUtils.compareVersion(versionName, versionId);
                    //0代表相等，1代表version1大于version2，-1代表version1小于version2
                    if(compareVersion == -1){
                        auVersionNameTip.setEnabled(true);
                        getUiDelegate().visible(true, auVersionNameTip);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("AboutActivity", "Throwable-----" + e.getMessage());
            }
        }, context), cityCode);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }


    public void back(View view) {
        finish();
    }


}
