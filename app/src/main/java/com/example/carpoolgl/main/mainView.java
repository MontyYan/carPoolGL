package com.example.carpoolgl.main;

import com.example.carpoolgl.base.activity.baseView;
import com.example.carpoolgl.bean.RelOrder;

public interface mainView extends baseView {
    //设置用户全局信息
    void SetUInfo();

    /*
    *   设置订单信息，根据用户是否已发布订单判断是否在主页面显示订单信息
    * */
    void SetOrder(RelOrder order);
}
