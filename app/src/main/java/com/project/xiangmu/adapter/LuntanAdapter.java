package com.project.xiangmu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.xiangmu.R;
import com.project.xiangmu.entity.Luntan;
import com.project.xiangmu.utils.StringUtil;


public class LuntanAdapter extends BaseQuickAdapter<Luntan, BaseViewHolder> {
    private OnLuntanListener listener;
    private Context context;
    public LuntanAdapter(Context context,OnLuntanListener luntanListener) {
        super(R.layout.item_home_luntan);
        this.context = context;
        this.listener = luntanListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Luntan item) {
        helper.addOnClickListener(R.id.ll_header);
        helper.setText(R.id.tv_name,item.getUsername());
        if (!TextUtils.isEmpty(item.getHead_url())){
            Glide.with(context).load(item.getHead_url()).into((ImageView) helper.getView(R.id.image_head));
        }
        if (!TextUtils.isEmpty(item.getPic())){
            helper.getView(R.id.iv_pic).setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getPic()).into((ImageView) helper.getView(R.id.iv_pic));
        }
        helper.setText(R.id.tv_content,item.getContent());
        helper.setText(R.id.tv_zan,"like"+item.getZan());
        helper.getView(R.id.ll_pl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onCommentClick(item);
                }
            }
        });
        helper.getView(R.id.ll_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onPraiseClick(item);
                }
            }
        });

        String address = item.getAddress();
        if (StringUtil.isEmpty(address)) {
            helper.setText(R.id.tv_address,"position: U.S.A");
        }else {
            helper.setText(R.id.tv_address,address);
        }
    }
}
