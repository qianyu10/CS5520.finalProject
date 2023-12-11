package com.project.xiangmu.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.activities.ChatDetailAct;
import com.project.xiangmu.activities.SerachActivity;
import com.project.xiangmu.adapter.FriendAdapter;
import com.project.xiangmu.adapter.OnFriendListener;
import com.project.xiangmu.base.LazyTwoFragment;
import com.project.xiangmu.entity.Friend;

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
 */
public class TabLianxirenFragment extends LazyTwoFragment implements OnFriendListener {

    @BindView(R.id.rl_add)
    RelativeLayout rlAdd;
    @BindView(R.id.tv_ding)
    TextView tvDing;
    @BindView(R.id.rl_zhiding)
    RelativeLayout rlZhiding;
    FriendAdapter searchFriendAdapter;
    @BindView(R.id.rl_friend)
    RecyclerView rlFriend;
    List<Friend> friendList = new ArrayList<>();
    ValueEventListener postListener;
    Map<String, Object> mMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.tab_circle;
    }


    @Override
    protected void loadData() {
        searchFriendAdapter = new FriendAdapter(getActivity(), this);
        rlFriend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlFriend.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        searchFriendAdapter.bindToRecyclerView(rlFriend);
        searchFriendAdapter.disableLoadMoreIfNotFullPage();
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendList = new ArrayList<>();
                mMap = new HashMap<>();
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    Log.e("hao", "link: "+snapshotNode.getValue());
                    String value = (String) snapshotNode.getValue();
                    Friend friend = new Gson().fromJson(value, Friend.class);
                    friendList.add(friend);
                    mMap.put(snapshotNode.getKey(), snapshotNode.getValue());
                }
                if (friendList != null && friendList.size() > 0) {
                    List<Friend> friends = new ArrayList<>();
                    for (int i = 0; i < friendList.size(); i++) {
                        if (App.getInstance().userBean.getName().equals(friendList.get(i).getName())) {
                            friends.add(friendList.get(i));
                        }
                    }
                    searchFriendAdapter.setNewData(friends);
                    searchFriendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("friend").addValueEventListener(postListener);


    }


    @OnClick({R.id.rl_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_add:
                startActivity(new Intent(getActivity(), SerachActivity.class));
                break;
        }
    }


    @Override
    public void onCLick(Friend friend) {
        Intent intent = new Intent(getActivity(), ChatDetailAct.class);
        friend.setChat("已聊天");
        mMap.put(friend.getId(), new Gson().toJson(friend));
        App.mDatabase.child("friend").setValue(mMap);
        if (App.getInstance().userBean.getName().equals(friend.getHaoyouname())) {
            intent.putExtra("haoyouname", friend.getName());
        } else {
            intent.putExtra("haoyouname", friend.getHaoyouname());
        }

        intent.putExtra("myname", App.getInstance().userBean.getName());


        startActivity(intent);
    }

    @Override
    public void onLongClick(Friend friend) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("Delete Friend")
                .setMsg("Do you confirm to delete friend?")
                .setPositiveButton("Confirm", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            mMap.remove(friend.getId());
                            Log.e("hao", "TabLianxirenFragment onClick(): "+mMap.toString());
                            App.mDatabase.child("friend").setValue(mMap);
                            if (friendList != null && friendList.size() > 0) {
                                List<Friend> friends = new ArrayList<>();
                                for (int i = 0; i < friendList.size(); i++) {
                                    if (App.getInstance().userBean.getName().equals(friendList.get(i).getName())) {
                                        friends.add(friendList.get(i));
                                    }
                                }
                                searchFriendAdapter.setNewData(friends);
                                searchFriendAdapter.notifyDataSetChanged();
                            }
                    }
                }).setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.mDatabase.child("friend").removeEventListener(postListener);

    }
}
