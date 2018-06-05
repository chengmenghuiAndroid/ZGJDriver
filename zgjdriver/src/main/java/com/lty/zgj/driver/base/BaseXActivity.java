package com.lty.zgj.driver.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.lty.zgj.driver.core.tool.HideSoftKeyboard;
import cn.droidlover.xdroid.base.XActivity;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2017/12/15.
 */

public abstract class BaseXActivity extends XActivity {


    /**
     * 点击上方返回键，关闭当前页
     * @param v
     */
    public void back(View v) {
        finish();
    }

    public void launch(Class<?> clz){
        Router.newIntent(this)
                .to(clz)
                .launch();
    }

    public void launch(Class<?> clz,Bundle bundle){
        Router.newIntent(this)
                .to(clz)
                .data(bundle)
                .launch();
    }
    public void launchForResult(Class<?> clz,int requestCode){
        Router.newIntent(this)
                .to(clz)
                .requestCode(requestCode)
                .launch();
    }
    public void launchForResult(Class<?> clz,Bundle bundle,int requestCode){
        Router.newIntent(this)
                .to(clz)
                .data(bundle)
                .requestCode(requestCode)
                .launch();
    }

    /**
     * 点击空白区域隐藏软键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (HideSoftKeyboard.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
