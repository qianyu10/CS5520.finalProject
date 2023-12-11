package com.project.xiangmu.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.base.BaseActivity;
import com.project.xiangmu.entity.Luntan;
import com.project.xiangmu.utils.StringUtil;
import com.project.xiangmu.utils.Utils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author admin
 * @description:发布帖子
 */
public class SendLuntanActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.btn_send)
    AppCompatTextView btnSend;
    @BindView(R.id.tv_location)
    TextView tv_location;
    ValueEventListener postListener;
    Map<String, Object> mMap = new HashMap<>();
    String path = "";
    private LocationClient mLocClient;
    private MyLocationListener myListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_luntan;
    }

    @Override
    protected void init() {
        tvTitle.setText("Publish a post");

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap = new HashMap<>();
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    Luntan luntan = new Gson().fromJson(snapshotNode.getValue().toString(), Luntan.class);
//                    mMap.put(snapshotNode.getKey(), snapshotNode.getValue());
                    mMap.put(luntan.getId()+"", snapshotNode.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("luntan").addValueEventListener(postListener);
        try {
            initLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 定位初始化
     */
    public void initLocation() throws Exception {
        // 定位初始化
        mLocClient = new LocationClient(this);
        myListener = new MyLocationListener();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true);// 打开gps
        option.setOpenGnss(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置高精度定位
        option.setEnableSimulateGnss(false);
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocClient.setLocOption(option);
        mLocClient.start();//开始定位
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            String address = location.getAddress().address;
            Log.e("hao", "address: " + address);
            if (StringUtil.isEmpty(address)) {
                tv_location.setText("position：U.S.");
            } else {
                tv_location.setText("position：" + address);
            }
        }

    }


    @OnClick({R.id.rl_back, R.id.rl_head, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_head:
                PictureSelector
                        .create(SendLuntanActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture();
                break;
            case R.id.btn_send:
                String content = tvName.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToast("Please add information...");
                    return;
                } else {
                    Luntan luntan = new Luntan();
                    luntan.setHead_url(App.getInstance().userBean.getHead_url());
                    luntan.setUsername(App.getInstance().userBean.getName());
                    luntan.setUser_id(App.getInstance().userBean.getUser_id());
                    luntan.setPic(path);
                    luntan.setZan("0");
                    luntan.setContent(content);
                    luntan.setTime(Utils.getTime());
                    luntan.setId(mMap.size() + 1);
                    luntan.setAddress(tv_location.getText().toString());
                    mMap.put(luntan.getId()+"", new Gson().toJson(luntan));
                    App.mDatabase.child("luntan").setValue(mMap);
                    showToast("publish success");

                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                Glide.with(this).load(pictureBean.getPath()).into(imageHead);
                path = pictureBean.getPath();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.mDatabase.child("luntan").removeEventListener(postListener);
        mLocClient.unRegisterLocationListener(myListener);
    }
}
