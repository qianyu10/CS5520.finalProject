package com.project.xiangmu.entity;

import java.io.Serializable;

/**
 * 用户信息实体类
 */
public class UserBean implements Serializable {
    public int id;
    public String user_id;//用户id
    public String name;//姓名
    public String password;//密码
    public String student_num;//学号
    public String mobile;//手机号
    public String head_url;//头像

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudent_num() {
        return student_num;
    }

    public void setStudent_num(String student_num) {
        this.student_num = student_num;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", student_num='" + student_num + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
