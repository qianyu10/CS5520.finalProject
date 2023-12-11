package com.project.xiangmu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.entity.Friend;

/**
 * @author admin
 * @description:
 */
public class FriendAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {
    private Context context;
    private OnFriendListener listener;
    public FriendAdapter(Context context,OnFriendListener listener) {
        super(R.layout.item_friend);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, Friend item) {
        if(App.getInstance().userBean.getName().equals(item.getName())){
            helper.setText(R.id.tv_name,item.getHaoyouname());
        }else {
            helper.setText(R.id.tv_name,item.getName());
        }

        if (!TextUtils.isEmpty(item.getHead_url())){
            Glide.with(context).load(item.getHead_url()).into((ImageView) helper.getView(R.id.img));
        }
        helper.getView(R.id.rl_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onCLick(item);
                }
            }
        });
        helper.getView(R.id.rl_ll).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null){
                    listener.onLongClick(item);
                }
                return false;
            }
        });
    }
}
