package com.lty.zgj.driver.ui.fragment;

import android.os.Bundle;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXFragment;

/**
 * Created by Administrator on 2018/6/6.
 * 待发车
 */

public class WaitGoingOutFragment extends BaseXFragment {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.wait_going_out_fragment;
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    @Override
    protected void lazyLoad() {

    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以调用此方法
     */
    @Override
    protected void stopLoad() {

    }
}
