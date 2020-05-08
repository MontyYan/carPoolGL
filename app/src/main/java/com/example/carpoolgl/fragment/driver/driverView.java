package com.example.carpoolgl.fragment.driver;

import com.example.carpoolgl.base.fragment.baseFragView;
import com.example.carpoolgl.bean.RelOrder;

public interface driverView extends baseFragView {
    /*
     *   设置订单信息，根据用户是否已发布订单判断是否在主页面显示订单信息
     * */
    void SetOrder(RelOrder order);
}
