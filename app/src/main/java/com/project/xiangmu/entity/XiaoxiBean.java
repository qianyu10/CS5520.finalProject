package com.project.xiangmu.entity;
/*

 * @Desc
 */

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "XiaoxiBean")

public class XiaoxiBean {
    @Column(name = "id", isId = true, autoGen = true)
    private String id;
    @Column(name = "haoyouname")//
    private String haoyouname;
    @Column(name = "myname")//
    private String myname;
    @Column(name = "content")//
    private String content;
    @Column(name = "time")//
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHaoyouname() {
        return haoyouname;
    }

    public void setHaoyouname(String haoyouname) {
        this.haoyouname = haoyouname;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
