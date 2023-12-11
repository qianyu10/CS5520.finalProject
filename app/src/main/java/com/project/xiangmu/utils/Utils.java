package com.project.xiangmu.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author admin

 */
public class Utils {
    public static String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");

        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }


}
