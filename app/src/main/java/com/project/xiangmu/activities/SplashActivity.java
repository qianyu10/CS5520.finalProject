package com.project.xiangmu.activities;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.xiangmu.R;
import com.project.xiangmu.base.BaseActivity;
import com.project.xiangmu.widget.CountdownView;

import butterknife.BindView;

/**
 * 文件名：SplashActivity
 * 作  者：
 * 描述：启动页
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_count)
    CountdownView tvCount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        Glide.with(this).load(R.drawable.congwu).into(image);

        tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCount.timeEnd();//停止计时
                    showActivity(SplashActivity.this,LoginActivity.class);
                    finish();
            }
        });

        //倒计时按钮
        tvCount.setMaxTime(3)
                .setConcatStr("s to skip")
                .setBgStyle(CountdownView.BgStyle.FILL)
                .setBgColor(Color.RED)
                .setBgColor(30)
                .setEndListener(new CountdownView.CountdownEndListener() {
                    @Override
                    public void countdownEnd() {
                        //TODO 倒计时结束监听
                            showActivity(SplashActivity.this, LoginActivity.class);
                            finish();
                    }
                }).timeStart();
    }

}
