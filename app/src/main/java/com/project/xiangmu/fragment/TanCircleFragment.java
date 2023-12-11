package com.project.xiangmu.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.activities.PinglunActivity;
import com.project.xiangmu.adapter.OnLuntanListener;
import com.project.xiangmu.base.LazyTwoFragment;
import com.project.xiangmu.entity.EventMessage;
import com.project.xiangmu.entity.Friend;
import com.project.xiangmu.entity.Luntan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class TanCircleFragment extends LazyTwoFragment implements OnLuntanListener {
    @BindView(R.id.rl_home)
    RecyclerView rlHome;
    @BindView(R.id.swiper)
    SwipeRefreshLayout swipeRefresh;

    LuntanAdapter luntanAdapter;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    List<Luntan> luntanList = new ArrayList<>();
    ValueEventListener postListener;
    ValueEventListener postListener1;
    Map<String, Object> mMap = new HashMap<>();
    List<Friend> friendList = new ArrayList<>();
    Map<String, Object> mFriendMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.tab_circle_fragment1;
    }

    @Override
    protected void loadData() {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SendLuntanActivity.class));
            }
        });
        rlBack.setVisibility(View.GONE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
            }
        });
        luntanAdapter = new LuntanAdapter(getActivity(), this);

        luntanAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_header) {
                    //添加好友
                    MyAlertDialog myAlertDialog = new MyAlertDialog(getContext()).builder()
                            .setTitle("Reminds")
                            .setMsg("Are you sure to add friends？")
                            .setPositiveButton("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addFriend(luntanList.get(position));
                                }
                            }).setNegativeButton("No", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    myAlertDialog.show();
                }
            }
        });
        rlHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlHome.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        luntanAdapter.bindToRecyclerView(rlHome);
        luntanAdapter.disableLoadMoreIfNotFullPage();

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                luntanList = new ArrayList<>();
                mMap = new HashMap<>();
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    String value = (String) snapshotNode.getValue();
                    Luntan friend = new Gson().fromJson(value, Luntan.class);
                    luntanList.add(friend);
                    mMap.put(friend.getId()+"", snapshotNode.getValue());
                    Log.e("hao", "snapshotNode.getKey(): "+snapshotNode.getKey());
                    Log.e("hao", "snapshotNode.getValue(): "+snapshotNode.getValue());
                    luntanAdapter.setNewData(luntanList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("luntan").addValueEventListener(postListener);
        postListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendList = new ArrayList<>();
                mFriendMap = new HashMap<>();
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    String value = (String) snapshotNode.getValue();
                    Friend friend = new Gson().fromJson(value, Friend.class);
                    friendList.add(friend);
                    mFriendMap.put(friend.getId()+"", snapshotNode.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("friend").addValueEventListener(postListener1);
    }

    private void addFriend(Luntan userBean) {
        if (userBean.getUser_id().equals(App.getInstance().userBean.getId() + "")
                && App.getInstance().userBean.getName().equals(userBean.getUsername())) {
            showToast("Can't add self as friend");
            return;
        }
        if (friendList == null || friendList.size() == 0) {
            Friend friend = new Friend();
            friend.setChat("");
            friend.setId(System.currentTimeMillis() + "");
            friend.setHaoyouid(userBean.getUser_id() + "");
            friend.setHaoyouname(userBean.getUsername());
            friend.setHaoyou_head_url(userBean.getHead_url());
            friend.setHead_url(App.getInstance().userBean.getHead_url());
            friend.setName(App.getInstance().userBean.getName());
            mMap.put(friend.getId(), new Gson().toJson(friend));
            App.mDatabase.child("friend").setValue(mMap);
            showToast("Success for add friends");
            return;
        }
        boolean isAdd = false;
        List<String> tempList = new ArrayList();
        tempList.add(App.getInstance().userBean.getName());
        tempList.add(userBean.getUsername());
        for (int i = 0; i < friendList.size(); i++) {
            if (tempList.contains(friendList.get(i).getHaoyouname())
                    && tempList.contains(friendList.get(i).getName())) {
                isAdd = true;
            }
        }
        if (isAdd) {
            showToast("Friend added, can't repeat");
            return;
        }
        Friend friend = new Friend();
        friend.setChat("");
        friend.setId(System.currentTimeMillis() + "");
        friend.setHaoyouid(userBean.getId() + "");
        friend.setHaoyouname(userBean.getUsername());
        friend.setName(App.getInstance().userBean.getName());
        friend.setHaoyou_head_url(userBean.getHead_url());
        friend.setHead_url(App.getInstance().userBean.getHead_url());
        mFriendMap.put(friend.getId(), new Gson().toJson(friend));
        App.mDatabase.child("friend").setValue(mFriendMap);
        showToast("Add Successes");

    }

    @Override
    public void onCommentClick(Luntan luntan) {
        Intent intent = new Intent(getActivity(), PinglunActivity.class);
        intent.putExtra("detail", luntan.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPraiseClick(Luntan luntan) {
        int zan = Integer.parseInt(luntan.getZan()) + 1;
        luntan.setZan(zan + "");
        mMap.put(luntan.getId() + "", new Gson().toJson(luntan));
        App.mDatabase.child("luntan").setValue(mMap);
        luntanAdapter.setNewData(luntanList);
        luntanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEvent(EventMessage msg) {
        super.onEvent(msg);
        luntanAdapter.setNewData(luntanList);
        luntanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        App.mDatabase.child("luntan").removeEventListener(postListener);
        App.mDatabase.child("friend").removeEventListener(postListener);
    }
}
