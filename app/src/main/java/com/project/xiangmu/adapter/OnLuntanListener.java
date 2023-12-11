package com.project.xiangmu.adapter;


import com.project.xiangmu.entity.Luntan;

/**
 * @author admin

 */
public interface OnLuntanListener {
    void onCommentClick(Luntan luntan);//评论
    void onPraiseClick(Luntan luntan);//赞
}
