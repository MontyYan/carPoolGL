package com.example.carpoolgl.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CostUtil {

    public static int computedValue(float routeLength){

        double length = (double)routeLength;
        int value = (int)Math.ceil(length/1000*2.1);
        return value;

    }

    public static String getNowTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }

    public static Date StringToDate(String datestr) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(datestr);
        return date;
    }

}
