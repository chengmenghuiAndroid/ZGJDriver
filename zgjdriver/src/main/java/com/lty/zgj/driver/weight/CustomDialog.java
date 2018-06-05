package com.lty.zgj.driver.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lty.zgj.driver.R;


/**
 * Created by chengmenghui on 2017/12/27.
 */

public class CustomDialog extends Dialog {
    private String title;
    private String content;
    private String buttonConfirm;
    private String buttonCancel;
    private View.OnClickListener confirmClickListener;
    private View.OnClickListener cancelClickListener;
    private int layoutResID;
    private static final int SHOW_ONE = 1;
    private int show = 2;

    public CustomDialog(Context context, int layoutResID, String title, String content,
                        String buttonConfirm,
                        View.OnClickListener confirmClickListener) {
        super(context, R.style.Dialog);
        this.title = title;
        this.content = content;
        this.buttonConfirm = buttonConfirm;
        this.confirmClickListener = confirmClickListener;
        this.show = SHOW_ONE;
        this.layoutResID = layoutResID;
    }

    public CustomDialog(Context context, int layoutResID, String title, String content,
                        String buttonConfirm,
                        View.OnClickListener confirmClickListener, String buttonCancel) {
        super(context, R.style.Dialog);
        this.title = title;
        this.content = content;
        this.buttonConfirm = buttonConfirm;
        this.buttonCancel = buttonCancel;
        this.confirmClickListener = confirmClickListener;
        this.layoutResID = layoutResID;
    }

    public CustomDialog(Context context, int layoutResID, String title, String content,
                        View.OnClickListener confirmClickListener, String buttonConfirm, String buttonCancel) {
        super(context, R.style.Dialog);
        this.title = title;
        this.content = content;
        this.buttonConfirm = buttonConfirm;
        this.buttonCancel = buttonCancel;
        this.confirmClickListener = confirmClickListener;
        this.layoutResID = layoutResID;
    }

    public CustomDialog(Context context, int layoutResID, String title, String content,
                        View.OnClickListener confirmClickListener, View.OnClickListener cancelClickListener, String
                                buttonConfirm, String buttonCancel) {
        super(context, R.style.Dialog);
        this.title = title;
        this.content = content;
        this.buttonConfirm = buttonConfirm;
        this.buttonCancel = buttonCancel;
        this.confirmClickListener = confirmClickListener;
        this.cancelClickListener = cancelClickListener;
        this.layoutResID = layoutResID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);
        TextView dialog_title = (TextView) findViewById(R.id.dialog_title);
        TextView dialog_content = (TextView) findViewById(R.id.dialog_content);
        TextView dialog_confirm = (TextView) findViewById(R.id.dialog_confirm);
        TextView dialog_cancel = (TextView) findViewById(R.id.dialog_cancel);
        View dialog_line = findViewById(R.id.dialog_line);
        if (!TextUtils.isEmpty(title)){
            dialog_title.setText(title);
            dialog_title .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        if (!TextUtils.isEmpty(content)) {
            dialog_content.setText(content);
        } else {
            dialog_content.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(buttonConfirm))
            dialog_confirm.setText(buttonConfirm);
        if (!TextUtils.isEmpty(buttonCancel))
            dialog_cancel.setText(buttonCancel);
        if (SHOW_ONE == show) {
            dialog_line.setVisibility(View.GONE);
            dialog_cancel.setVisibility(View.GONE);
            if (null != confirmClickListener) {
                dialog_confirm.setOnClickListener(confirmClickListener);
            }
            dialog_confirm.setBackgroundResource(R.drawable.back_text_selector);
        } else {
            if (null != confirmClickListener) {
                dialog_confirm.setOnClickListener(confirmClickListener);
            }
            if (null != cancelClickListener) {
                dialog_cancel.setOnClickListener(cancelClickListener);
            } else {
                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomDialog.this.dismiss();
                    }
                });
            }

        }
    }

    public void setCanotBackPress() {
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
    }

}
