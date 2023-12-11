package com.project.xiangmu.entity;


public class EventMessage {

    private Object mObject;

    private int messageType;

    private int Num;

    public EventMessage(int type) {
        messageType = type;
    }

    public EventMessage(int type, Object object) {
        mObject = object;
        messageType = type;
    }

    public EventMessage(int type, Object object, int num) {
        mObject = object;
        messageType = type;
        Num = num;
    }

    public Object getmObject() {
        return mObject;
    }

    public int getMessageType() {
        return messageType;
    }

    public int getNum() {
        return Num;
    }

    public static final int LOGIN = 3;
    public static final int HEAD = 1;
    public static final int LOGOUT = 2;
    public static final int REFRESH = 4;
    public static final int REMOVE = 5;
    public static final int ADD = 6;
    public static final int MODIFY_SELF = 7;
    public static final int REFRESH_SHOP_CAR = 8;
    public static final int CHOICE_ADDRESS = 9;


    public static final int ModifyHead = 10;

}
