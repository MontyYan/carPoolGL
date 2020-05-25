package com.example.carpoolgl.publishedOrder.passenger;

import com.example.carpoolgl.base.activity.baseView;
import com.example.carpoolgl.bean.DriverCarInfo;

public interface passenOrderView extends baseView {
//    void setPaOrderInfo(String payment,String passenNum);
    void setPaOrderInfo(DriverCarInfo dcInfo,String result,Integer con);

    void setPayDialog(Integer result,String payOrder);

    void PayingDialog(Integer result,String payResult);
}
