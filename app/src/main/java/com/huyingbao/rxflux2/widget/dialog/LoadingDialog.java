package com.huyingbao.rxflux2.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


/**
 * 进度提示
 * Created by liujunfeng on 2017/1/1.
 */
public class LoadingDialog extends AppCompatDialog implements OnShowListener {
    private Context mContext;
    private CharSequence mMsg;
    private TextView tvTip;

    public LoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_loading);
//        tvTip = findViewById(R.id.tv_loading_notice);
        setOnShowListener(this);
    }


    @Override
    public void onShow(DialogInterface dialog) {
        if (!TextUtils.isEmpty(this.mMsg)) {
            tvTip.setVisibility(View.VISIBLE);
            tvTip.setText(this.mMsg);
        }
    }

    /**
     * 设置文字
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        this.mMsg = message;
    }
}
