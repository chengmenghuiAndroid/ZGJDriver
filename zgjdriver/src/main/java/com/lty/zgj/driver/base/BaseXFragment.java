package com.lty.zgj.driver.base;

import android.os.Bundle;

import cn.droidlover.xdroid.base.XFragment;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2017/12/15.
 */

public abstract class BaseXFragment extends XFragment {

    public void launch(Class<?> clz){
        Router.newIntent(context)
                .to(clz)
                .launch();
    }

    public void launch(Class<?> clz,Bundle bundle){
        Router.newIntent(context)
                .to(clz)
                .data(bundle)
                .launch();
    }
    public void launchForResult(Class<?> clz,int requestCode){
        Router.newIntent(context)
                .to(clz)
                .requestCode(requestCode)
                .launch();
    }
    public void launchForResult(Class<?> clz,Bundle bundle,int requestCode){
        Router.newIntent(context)
                .to(clz)
                .requestCode(requestCode)
                .launch();
    }

}
