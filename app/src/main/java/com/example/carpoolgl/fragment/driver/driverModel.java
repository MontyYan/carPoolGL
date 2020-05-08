package com.example.carpoolgl.fragment.driver;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.example.carpoolgl.bean.RelOrder;

public class driverModel {

    public void findOrder(Context context, driverView driverV){
        SharedPreferences shapre = context.getSharedPreferences("dr_orderinfo",Context.MODE_PRIVATE);
        Integer result = shapre.getInt("orderConCode",0);
        if(result.equals(1)){
            String orderJson = shapre.getString("orderJson","").replaceAll("&quot;","");
            RelOrder order = JSONObject.parseObject(orderJson,RelOrder.class);//relorder Json格式映射
            driverV.SetOrder(order);
        }
    }
}
