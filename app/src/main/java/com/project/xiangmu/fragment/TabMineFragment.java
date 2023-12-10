package com.project.xiangmu.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.activities.AboutActivity;
import com.project.xiangmu.activities.ChangeUserInfoActivity;
import com.project.xiangmu.activities.UserInfoActivity;
import com.project.xiangmu.base.LazyFragment;
import com.project.xiangmu.widget.CircleImageView;

import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author admin
 * @description:我的界面

 */
public class TabMineFragment extends LazyFragment {
    @BindView(R.id.image_head)
    CircleImageView imageHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_userinfo)
    RelativeLayout rlUserinfo;
    @BindView(R.id.rl_change_pwd)
    RelativeLayout rlChangePwd;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.rl_restart)
    RelativeLayout rlRestart;
    @BindView(R.id.rl_zhuxiao)
    RelativeLayout rlZhuxiao;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.rl_send)
    RelativeLayout rlSend;
    @BindView(R.id.rl_kefu)
    RelativeLayout rlKefu;


    @Override
    protected int getLayoutId() {
        return R.layout.tab_fragment_me;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (imageHead != null) {
            if (App.getInstance().userBean != null) {
                tvName.setText(App.getInstance().userBean.getName());
                if (!TextUtils.isEmpty(App.getInstance().userBean.getHead_url())) {
                    Glide.with(getActivity()).load(App.getInstance().userBean.getHead_url()).into(imageHead);
                }
            }
        }
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.rl_userinfo, R.id.rl_change_pwd, R.id.rl_about, R.id.rl_restart, R.id.rl_zhuxiao, R.id.tv_login, R.id.rl_send, R.id.rl_kefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.rl_change_pwd:
                startActivity(new Intent(getActivity(), ChangeUserInfoActivity.class));//修改密码
                break;
            case R.id.rl_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.rl_restart:
                reStartApp();
                break;
            case R.id.rl_zhuxiao:
                Map<String, Object> mUserMap = App.mUserMap;
                mUserMap.remove(App.getInstance().userBean.getName());
                App.mDatabase.child("user").setValue(mUserMap);
                reStartApp();
                break;
            case R.id.tv_login:
                dialog();
                break;
            case R.id.rl_send:
                startActivity(new Intent(getActivity(), SendLuntanActivity.class));
                break;
            case R.id.rl_kefu:
                callPhone("023-66666666");
                break;

        }
    }

    public void reStartApp() {
        Intent intent = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//与正常页面跳转一样可传递序列化数据,在Launch页面内获得
        intent.putExtra("REBOOT", "reboot");
        startActivity(intent);
        getActivity().finish();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    //正常退出
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
