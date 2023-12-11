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
import com.hb.dialog.myDialog.MyAlertDialog;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.adapter.OnUserListener;
import com.project.xiangmu.adapter.SearchFriendAdapter;
import com.project.xiangmu.base.BaseActivity;
import com.project.xiangmu.entity.Friend;
import com.project.xiangmu.entity.UserBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author admin
 * @description:搜索好友
 */
public class SerachActivity extends BaseActivity implements OnUserListener {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.serach)
    TextView serach;
    @BindView(R.id.rl_friend)
    RecyclerView rlFriend;
    SearchFriendAdapter searchFriendAdapter;
    List<Friend> friendList = new ArrayList<>();
    ValueEventListener postListener;
    Map<String, Object> mMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        tvTitle.setText("Find Friend");
        searchFriendAdapter = new SearchFriendAdapter(SerachActivity.this, this);
        rlFriend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rlFriend.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        searchFriendAdapter.bindToRecyclerView(rlFriend);
        searchFriendAdapter.disableLoadMoreIfNotFullPage();

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendList = new ArrayList<>();
                mMap = new HashMap<>();
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    String value = (String) snapshotNode.getValue();
                    Friend friend = new Gson().fromJson(value, Friend.class);
                    friendList.add(friend);
                    mMap.put(friend.getId()+"", snapshotNode.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("friend").addValueEventListener(postListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.mDatabase.child("friend").removeEventListener(postListener);

    }

    @OnClick({R.id.rl_back, R.id.serach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.serach:
                String name = etInput.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showToast("Please type in friend info");
                    return;
                }
                Map<String, Object> mUserMap = App.mUserMap;
                List<UserBean> userBeans = new ArrayList<>();
                for (Map.Entry<String, Object> entry : mUserMap.entrySet()) {
                    String value = (String) entry.getValue();
                    UserBean user = new Gson().fromJson(value, UserBean.class);
                    if (user != null) {
                        if (user.getName().contains(name) || user.getMobile().contains(name) || user.getStudent_num().contains(name)) {
                            userBeans.add(user);
                        }
                    }
                }
                searchFriendAdapter.setNewData(userBeans);
                searchFriendAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onclick(UserBean userBean) {
        info(userBean);
    }

    private void info(UserBean userBean) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(this).builder()
                .setTitle("Prompt")
                .setMsg("Are you sure to add this user？")
                .setPositiveButton("confirm", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFriend(userBean);
                    }
                }).setNegativeButton("cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        myAlertDialog.show();
    }

    private void addFriend(UserBean userBean) {
        if (App.getInstance().userBean.getId() == userBean.getId()
                && App.getInstance().userBean.getName().equals(userBean.getName())) {
            showToast("Can't add self");
            return;
        }
        if (friendList == null || friendList.size() == 0) {
            Friend friend = new Friend();
            friend.setChat("");
            friend.setId(System.currentTimeMillis() + "");
            friend.setHaoyouid(userBean.getId() + "");
            friend.setHaoyouname(userBean.getName());
            friend.setHaoyou_head_url(userBean.getHead_url());
            friend.setHead_url(App.getInstance().userBean.getHead_url());
            friend.setName(App.getInstance().userBean.getName());
            mMap.put(friend.getId(), new Gson().toJson(friend));
            App.mDatabase.child("friend").setValue(mMap);
            showToast("success add");
            finish();
            return;
        }
        boolean isAdd = false;
        List<String> tempList = new ArrayList();
        tempList.add(App.getInstance().userBean.getName());
        tempList.add(userBean.getName());
        for (int i = 0; i < friendList.size(); i++) {
//            if (userBean.getName().equals(friendList.get(i).getHaoyouname())
//                    && userBean.getId() + "" == friendList.get(i).getHaoyouid()) {
//                isAdd = true;
//            }
            if (tempList.contains(friendList.get(i).getHaoyouname())
            && tempList.contains(friendList.get(i).getName())) {
                isAdd = true;
            }
        }
        if (isAdd) {
            showToast("Have added friend，do not need add again");
            return;
        }
        Friend friend = new Friend();
        friend.setChat("");
        friend.setId(System.currentTimeMillis() + "");
        friend.setHaoyouid(userBean.getId() + "");
        friend.setHaoyouname(userBean.getName());
        friend.setName(App.getInstance().userBean.getName());
        friend.setHaoyou_head_url(userBean.getHead_url());
        friend.setHead_url(App.getInstance().userBean.getHead_url());
        mMap.put(friend.getId(), new Gson().toJson(friend));
        App.mDatabase.child("friend").setValue(mMap);
        showToast("Success add");
        finish();

    }
}
