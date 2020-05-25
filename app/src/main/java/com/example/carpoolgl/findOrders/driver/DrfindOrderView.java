package com.example.carpoolgl.findOrders.driver;

import com.example.carpoolgl.base.activity.baseView;
import com.example.carpoolgl.bean.RelOrder;

import java.util.List;

public interface DrfindOrderView extends baseView {

    public void setRecyclerView(Integer result, List<RelOrder> orders);

}
