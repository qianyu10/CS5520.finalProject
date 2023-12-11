package com.project.xiangmu.activities;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.base.BaseActivity;
import com.project.xiangmu.entity.XiaoxiBean;
import com.project.xiangmu.utils.MyDateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatDetailAct extends BaseActivity {

    private TextView tv_name;
    private TextView tv_send;
    private RecyclerView recycleview;
    private PlanAdapter mPlanAdapter;
    private EditText et_content;
    List<XiaoxiBean> loginBeanList = new ArrayList<>();
    ValueEventListener postListener;
    Map<String, Object> mMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_detail;
    }

    @Override
    protected void init() {
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loginBeanList = new ArrayList<>();
                mMap = new HashMap<>();
                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {
                    String value = (String) snapshotNode.getValue();
                    Log.e("hao", "Chat: "+value);
                    XiaoxiBean friend = new Gson().fromJson(value, XiaoxiBean.class);
                    loginBeanList.add(friend);
                    mMap.put(snapshotNode.getKey(), snapshotNode.getValue());
                }
                initData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        App.mDatabase.child("xiaoxi").addValueEventListener(postListener);
        initView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.mDatabase.child("xiaoxi").removeEventListener(postListener);
    }

    public void initView() {
        tv_name = findViewById(R.id.tv_name);
        tv_send = findViewById(R.id.tv_send);
        et_content = findViewById(R.id.et_content);
        recycleview = findViewById(R.id.recycleview);
        tv_name.setText(getIntent().getStringExtra("haoyouname"));
    }


    public void initData() {
        mPlanAdapter = new PlanAdapter(this);
        recycleview.setAdapter(mPlanAdapter);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        List<XiaoxiBean> planBeanList = new ArrayList<>();
        if (loginBeanList != null && loginBeanList.size() > 0) {
            int size = loginBeanList.size();
            for (int i = 0; i < size; i++) {
                if (loginBeanList.get(i).getHaoyouname().equals(
                        getIntent().getStringExtra("haoyouname")) &&
                        loginBeanList.get(i).getMyname().equals(
                                getIntent().getStringExtra("myname"))
                        || loginBeanList.get(i).getMyname().equals(
                        getIntent().getStringExtra("haoyouname")) &&
                        loginBeanList.get(i).getHaoyouname().equals(
                                getIntent().getStringExtra("myname"))) {
                    planBeanList.add(loginBeanList.get(i));
                }
            }
        }
        mPlanAdapter.setData(planBeanList);
        if (mPlanAdapter.getItemCount() == planBeanList.size()) {

        } else {
            mPlanAdapter.setData(planBeanList);
            recycleview.scrollToPosition(planBeanList.size() - 1);
        }

    }


    public void initEvent() {
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(ChatDetailAct.this, "Please type in message", Toast.LENGTH_SHORT).show();
                    return;
                }
                String shijian = MyDateUtils.getTime();
                XiaoxiBean xiaoxiBean = new XiaoxiBean();
                xiaoxiBean.setId(System.currentTimeMillis() + "");
                xiaoxiBean.setContent(content);
                xiaoxiBean.setTime(shijian);
                xiaoxiBean.setHaoyouname(getIntent().getStringExtra("haoyouname"));
                xiaoxiBean.setMyname(getIntent().getStringExtra("myname"));
                    mMap.put(xiaoxiBean.getId(),new Gson().toJson(xiaoxiBean));
                    App.mDatabase.child("xiaoxi").setValue(mMap);
                    initData();
                et_content.setText("");
            }
        });
    }

    public int ITEM_TYPE_LEFT = 1;
    public int ITEM_TYPE_RIGHT = 2;


    public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private List<XiaoxiBean> dataBeans = new ArrayList<>();

        public PlanAdapter(Context context) {
            this.context = context;
        }

        public void setData(List list) {
            dataBeans = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (App.getInstance().userBean.getName().equals(dataBeans.get(position).getMyname())) {
                return ITEM_TYPE_RIGHT;
            } else {
                return ITEM_TYPE_LEFT;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_RIGHT) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_right, parent, false);
                return new RightViewHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.item_left, parent, false);
                return new LeftViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof RightViewHolder) {
                ((RightViewHolder) holder).chat_item_content_text.setText(dataBeans.get(position).getContent());
                ((RightViewHolder) holder).tv_time.setText(dataBeans.get(position).getTime());
            } else {
                ((LeftViewHolder) holder).chat_item_content_text.setText(dataBeans.get(position).getContent());
                ((LeftViewHolder) holder).tv_time.setText(dataBeans.get(position).getTime());
            }
        }

        public void removeList(int position) {
            dataBeans.remove(position);//删除数据源,移除集合中当前下标的数据
            notifyItemRemoved(position);//刷新被删除的地方
            notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
        }

        @Override
        public int getItemCount() {
            if (dataBeans != null && dataBeans.size() > 0) {
                return dataBeans.size();
            } else {
                return 0;
            }
        }

        class RightViewHolder extends RecyclerView.ViewHolder {
            private TextView chat_item_content_text;
            private TextView tv_time;

            public RightViewHolder(View itemView) {
                super(itemView);
                chat_item_content_text = itemView.findViewById(R.id.chat_item_content_text);
                tv_time = itemView.findViewById(R.id.tv_time);
            }
        }

        class LeftViewHolder extends RecyclerView.ViewHolder {
            private TextView chat_item_content_text;
            private TextView tv_time;

            public LeftViewHolder(View itemView) {
                super(itemView);
                chat_item_content_text = itemView.findViewById(R.id.chat_item_content_text);
                tv_time = itemView.findViewById(R.id.tv_time);
            }
        }
    }
}
