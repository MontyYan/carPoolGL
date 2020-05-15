package com.example.carpoolgl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.findOrders.driver.DrfindOrderPresenter;
import com.example.carpoolgl.findOrders.driver.DrfindOrderView;
import com.example.carpoolgl.recyclerView.ordersRecycAdapter;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;

import java.util.List;

public class DrSearchOrderActivity extends baseActivity<DrfindOrderView, DrfindOrderPresenter> implements DrfindOrderView {

    private static final int IDENTITY = 6;
    private DrfindOrderPresenter presenter;

    private TextView dr_finding_order_tv;
    private ProgressBar dr_finding_order_pb;
    private RecyclerView dr_recyc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_search_order);
        initController();
        findOrders();
    }

    @Override
    public DrfindOrderPresenter createPresenter() {
        return new DrfindOrderPresenter();
    }

    @Override
    public DrfindOrderView createView() {
        return this;
    }

    public void initController(){
        dr_finding_order_tv = findViewById(R.id.dr_finding_order_tv);
        dr_finding_order_pb = findViewById(R.id.dr_finding_order_pb);
        dr_recyc = findViewById(R.id.dr_orderRecycView);
    }

    public void findOrders(){
        presenter = getPresenter();
        presenter.findOrders(DrSearchOrderActivity.this);
    }


    @Override
    public void setRecyclerView(Integer result, List<RelOrder> orders) {
        if(result.equals(0)){   //没有获取到服务端数据，页面显示查找失败
            ToastUtil.show(this,"查找失败");
            dr_finding_order_tv.setText("查找失败");
            dr_finding_order_pb.setVisibility(View.GONE);
        }else{ //显示recycler，tv与pb全部设置不可见
            dr_finding_order_tv.setVisibility(View.GONE);
            dr_finding_order_pb.setVisibility(View.GONE);

            ordersRecycAdapter adapter = new ordersRecycAdapter(DrSearchOrderActivity.this,orders,IDENTITY);
            dr_recyc.setAdapter(adapter);
            dr_recyc.setLayoutManager(new LinearLayoutManager(
                    DrSearchOrderActivity.this,
                    LinearLayoutManager.VERTICAL, //垂直方向
                    false             //非倒序
            ));
            dr_recyc.setVisibility(View.VISIBLE);
            adapter.setOnItemClickListener(new ordersRecycAdapter.OrderClickLisener() {
                @Override
                public void OnRouteClick(RelOrder order) {
                    Intent intent = new Intent(DrSearchOrderActivity.this, RouteActivity.class);
                    LatLonPoint mstartlatlon = RouteUtil.getLatLon(order.getStartLonLat());
                    LatLonPoint mendlatlon = RouteUtil.getLatLon(order.getEndLonLat());
                    DrivePath drivePath = RouteUtil.getDrivePath(order.getListSteps());

                    intent.putExtra("mStartPoint",mstartlatlon);
                    intent.putExtra("mEndPoint",mendlatlon);
                    intent.putExtra("drivePath",drivePath);
                    startActivity(intent);
                }
                @Override
                public void OnJoinClick(RelOrder order) {
                    quitDialog();
                }
            });
        }
    }

    public void quitDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(DrSearchOrderActivity.this);
        dialog.setTitle("确认加入");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
}
