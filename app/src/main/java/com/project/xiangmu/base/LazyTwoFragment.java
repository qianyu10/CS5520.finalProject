package com.project.xiangmu.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class LazyTwoFragment extends BaseFragment {

    protected View rootView;
    protected Unbinder unbinder;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    /** 状态栏沉浸 */
    private ImmersionBar mImmersionBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() == 0)
            return super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        isViewCreated = true;
        lazyLoad();
        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init();
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
//            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
            loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
        //页面销毁,恢复标记
        isViewCreated = false;
        isUIVisible = false;
        if (rootView != null) {
//            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }

    }



    protected abstract int getLayoutId();

    protected abstract void loadData();

    @Override
    public void onResume() {
        super.onResume();
        if (isStatusBarEnabled()) {
            // 重新初始化状态栏
            getStatusBarConfig().init();
        }
    }

    /**
     * 是否在 Fragment 使用沉浸式  默认显示
     */
    public boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    protected ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(statusBarDarkFont())
                // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardEnable(true);
    }

    /**
     * 获取状态栏字体颜色
     */
    protected boolean statusBarDarkFont() {
        // 返回真表示黑色字体
        return true;
    }

    protected void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


}
