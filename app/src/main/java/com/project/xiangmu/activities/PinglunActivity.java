package com.project.xiangmu.activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.adapter.PinglunAdapter;
import com.project.xiangmu.base.BaseActivity;
import com.project.xiangmu.entity.Pinglun;
import com.project.xiangmu.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class PinglunActivity extends BaseActivity {

    PinglunAdapter pinglunAdapter;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.rl_pinglun)
    RecyclerView rlPinglun;
    @BindView(R.id.et_pinglun)
    EditText etPinglun;
    @BindView(R.id.tv_pinglun)
    TextView tvPinglun;
    List<Pinglun> pinglunList;
    ValueEventListener postListener;
    Map<String, Object> mMap = new HashMap<>();
    int luntan_id;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pinglun;
    }

    @Override
    protected void init() {
        tvTitle.setText("Comment");
        luntan_id = getIntent().getIntExtra("detail",0);
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pinglunList = new ArrayList<>();
                mMap = new HashMap<>();
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    String value = (String) snapshotNode.getValue();
                    Pinglun friend = new Gson().fromJson(value, Pinglun.class);
                    if (friend.getLuntan_id().equals(luntan_id+"")) {
                        pinglunList.add(friend);
                    }
                    mMap.put(snapshotNode.getKey(), snapshotNode.getValue());
                    pinglunAdapter.setNewData(pinglunList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("pinglun").addValueEventListener(postListener);
        pinglunAdapter = new PinglunAdapter(PinglunActivity.this);
        rlPinglun.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rlPinglun.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        pinglunAdapter.bindToRecyclerView(rlPinglun);
        pinglunAdapter.disableLoadMoreIfNotFullPage();
    }



    @OnClick({R.id.rl_back, R.id.tv_pinglun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_pinglun:
                String content = etPinglun.getText().toString();
                if (TextUtils.isEmpty(content)){
                    showToast("Please enter");
                    return;
                }else {
                    Pinglun pinglun = new Pinglun();
                    pinglun.setLuntan_id(luntan_id+"");
                    pinglun.setContent(content);
                    pinglun.setTime(Utils.getTime());
                    pinglun.setId(mMap.size()+1);
                    pinglun.setUsername(App.getInstance().userBean.getName());
                    pinglun.setHead_url(App.getInstance().userBean.getHead_url());
                    mMap.put(pinglun.getId()+"",new Gson().toJson(pinglun));
                    App.mDatabase.child("pinglun").setValue(mMap);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.mDatabase.child("pinglun").removeEventListener(postListener);
    }
}
