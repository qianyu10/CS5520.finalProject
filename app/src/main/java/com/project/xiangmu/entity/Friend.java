package com.project.xiangmu.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * @author admin
 * @description:
 */
@Table(name = "Friend")
public class Friend implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private String id;
    @Column(name = "name")//
    private String name;
    @Column(name = "haoyouname")//
    private String haoyouname;
    @Column(name = "haoyouid")//
    private String haoyouid;
    @Column(name = "chat")//
    private String chat;
    @Column(name = "haoyou_head_url")//
    private String haoyou_head_url;
    @Column(name = "head_url")//
    private String head_url;

    public String getHaoyou_head_url() {
        return haoyou_head_url;
    }

    public void setHaoyou_head_url(String haoyou_head_url) {
        this.haoyou_head_url = haoyou_head_url;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHaoyouname() {
        return haoyouname;
    }

    public void setHaoyouname(String haoyouname) {
        this.haoyouname = haoyouname;
    }

    public String getHaoyouid() {
        return haoyouid;
    }

    public void setHaoyouid(String haoyouid) {
        this.haoyouid = haoyouid;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
