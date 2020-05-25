package com.example.carpoolgl.findOrders.passenger;

import com.example.carpoolgl.base.activity.baseView;
import com.example.carpoolgl.bean.RelOrder;

import java.util.List;

public interface fordersView extends baseView {
    //根据服务端返回值更新recyclerview
    public void setRecyclerView(Integer result, List<RelOrder> orders);

    public void setJoinResult(String result);

}
