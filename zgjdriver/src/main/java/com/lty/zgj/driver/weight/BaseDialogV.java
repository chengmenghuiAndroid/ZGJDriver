package com.lty.zgj.driver.weight;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by chengmenghui on 2018/1/22.
 */

public class BaseDialogV extends Dialog {
    private int res;

    public BaseDialogV(Context context, int theme, int res) {
        super(context, theme);
        // TODO 自动生成的构造函数存根
        setContentView(res);
        this.res = res;
        setCanceledOnTouchOutside(false);
    }
}
