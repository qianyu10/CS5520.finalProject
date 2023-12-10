package com.project.xiangmu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.project.xiangmu.R;
import com.project.xiangmu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：AboutActivity
 * 描述：关于APP
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        tvTitle.setText("About us");
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
