package com.project.xiangmu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateUtils {
    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
