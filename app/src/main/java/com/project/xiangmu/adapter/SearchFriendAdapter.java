package com.project.xiangmu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.xiangmu.R;
import com.project.xiangmu.entity.UserBean;

/**
 * @author admin

 */
public class SearchFriendAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
    private Context context;
    private OnUserListener listener;
    public SearchFriendAdapter(Context context,OnUserListener listener) {
        super(R.layout.item_friend);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        helper.setText(R.id.tv_name,item.getName());
        if (!TextUtils.isEmpty(item.getHead_url())){
            Glide.with(context).load(item.getHead_url()).into((ImageView) helper.getView(R.id.img));
        }
        helper.getView(R.id.rl_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onclick(item);
                }
            }
        });
    }
}
