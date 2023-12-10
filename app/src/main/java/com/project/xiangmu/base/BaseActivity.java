package com.project.xiangmu.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;

import com.project.xiangmu.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public abstract class BaseActivity extends RxAppCompatActivity {

    protected Unbinder unBinder;
    protected Context mContext;


    /** 状态栏沉浸 */
    private ImmersionBar mImmersionBar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWindowParams();

        setContentView(getLayoutId());
        mContext = this;
        unBinder = ButterKnife.bind(this);
        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init();
        }

        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        init();
    }

    public void setWindowParams(){

    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onEvent(EventMessage msg) {

    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onEventSticky(EventMessage msg) {

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
        try {
            //移除所有的粘性事件
            EventBus.getDefault().removeAllStickyEvents();
            //取消注册
            EventBus.getDefault().unregister(this);
        }catch (Exception e){

        }

    }

    public Context getViewContext(){
        return mContext;
    }

    protected abstract int getLayoutId();

    protected abstract void init();



    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    /**
     * 初始化沉浸式状态栏
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(isStatusBarDarkFont());
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    public ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    protected void showToast(String message){
        Toast.makeText(BaseActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
        this.overridePendingTransition(R.anim.start_in, R.anim.start_out);
    }

    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
        this.overridePendingTransition(R.anim.start_in, R.anim.start_out);
    }

    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
        this.overridePendingTransition(R.anim.start_in, R.anim.start_out);
    }
}
