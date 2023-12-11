package com.project.xiangmu.fragment;

import android.content.Intent;
import android.text.TextUtils;
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
import com.project.xiangmu.adapter.FriendAdapter;
import com.project.xiangmu.adapter.OnFriendListener;
import com.project.xiangmu.base.LazyTwoFragment;
import com.project.xiangmu.entity.Friend;
import com.project.xiangmu.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * @author admin
 * @description:
 */
public class TabZyFragment extends LazyTwoFragment implements OnFriendListener {
    FriendAdapter searchFriendAdapter;
    @BindView(R.id.rl_friend)
    RecyclerView rlFriend;
    @BindView(R.id.tv_ding)
    TextView tvDing;
    @BindView(R.id.rl_zhiding)
    RelativeLayout rlZhiding;
    List<Friend> friendList = new ArrayList<>();
    ValueEventListener postListener;

    @Override
    protected int getLayoutId() {
        return R.layout.tab_chat;
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
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    Log.e("hao", "friend key: " + snapshotNode.getKey());
                    Log.e("hao", "friend value: " + snapshotNode.getValue());
                    String value = (String) snapshotNode.getValue();
                    Friend friend = new Gson().fromJson(value, Friend.class);
                    friendList.add(friend);
                    isZhiding();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("friend").addValueEventListener(postListener);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (rlFriend != null && searchFriendAdapter != null) {
//            isZhiding();
//        }
//    }


    @Override
    public void onCLick(Friend friend) {
        Intent intent = new Intent(getActivity(), ChatDetailAct.class);
        if (App.getInstance().userBean.getName().equals(friend.getHaoyouname())) {
            intent.putExtra("haoyouname", friend.getName());
        } else {
            intent.putExtra("haoyouname", friend.getHaoyouname());
        }
        intent.putExtra("myname", App.getInstance().userBean.getName());
        startActivity(intent);
    }

    private void isZhiding() {

        SPUtil spUtil = new SPUtil(getContext(), "ding");
        String name = spUtil.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            rlZhiding.setVisibility(View.VISIBLE);
            tvDing.setText(name);
        }

        if (friendList != null && friendList.size() > 0) {
            List<Friend> friends = new ArrayList<>();
            for (int i = 0; i < friendList.size(); i++) {
                if ((App.getInstance().userBean.getName().equals(friendList.get(i).getName()) ||
                        App.getInstance().userBean.getName().equals(friendList.get(i).getHaoyouname()))
                        && !TextUtils.isEmpty(friendList.get(i).getChat())) {
                    if (App.getInstance().userBean.getName().equals(friendList.get(i).getName())) {
                        if (!name.equals(friendList.get(i).getHaoyouname())) {
                            friends.add(friendList.get(i));
                        }
                    } else if (App.getInstance().userBean.getName().equals(friendList.get(i).getHaoyouname())) {
                        if (!name.equals(friendList.get(i).getName())) {
                            friends.add(friendList.get(i));
                        }
                    }


                }
            }
            searchFriendAdapter.setNewData(friends);
            searchFriendAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLongClick(Friend friend) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("Pin Friends")
                .setMsg("Do you want to pin friends?")
                .setPositiveButton("Confirm", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlZhiding.setVisibility(View.VISIBLE);
                        rlZhiding.setBackgroundResource(R.color.main_color);
                        if (App.getInstance().userBean.getName().equals(friend.getName())) {
                            tvDing.setText(friend.getHaoyouname());
                            SPUtil spUtil = new SPUtil(getActivity(), "ding");
                            spUtil.putString("name", friend.getHaoyouname());
                            isZhiding();
                        } else {
                            tvDing.setText(friend.getName());
                            SPUtil spUtil = new SPUtil(getActivity(), "ding");
                            spUtil.putString("name", friend.getName());
                            isZhiding();
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
