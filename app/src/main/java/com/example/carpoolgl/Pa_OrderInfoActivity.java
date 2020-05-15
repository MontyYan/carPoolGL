package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.bean.DriverCarInfo;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.publishedOrder.passenger.passenOrderPresenter;
import com.example.carpoolgl.publishedOrder.passenger.passenOrderView;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;

public class Pa_OrderInfoActivity extends baseActivity<passenOrderView, passenOrderPresenter> implements View.OnClickListener ,passenOrderView{

    private TextView pa_order_con_tv;//订单状态
    private TextView pa_order_seq_tv;//订单编号
    private TextView pa_publish_geton_tv;   //起点
    private TextView pa_publish_getoff_tv;//终点
    private TextView pa_money_tv;// 价格
    private TextView pa_publish_time_tv;//出发时间
    private Button pa_publish_route_bt;//查看路线

    private TextView pa_appreciate_tv;//感谢费
    private TextView pa_add_appreciate_tv;//加感谢费

    private CardView sus_driverCarInfo_cv;//订单成功时 显示司机card
    private TextView pa_nowPassen_tv;//乘坐人数
    private TextView pa_driverName_tv;//司机姓名
    private TextView car_describe_tv;//车辆型号
    private TextView car_num_tv;//车辆车牌
    private TextView driver_phone_tv;//司机电话
    private ImageView phone_call_iv;//电话img

    private RelOrder order;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288

    @Override
    public passenOrderPresenter createPresenter() {
        return new passenOrderPresenter();
    }

    @Override
    public passenOrderView createView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        initController();   //实例化fvb
        getOrderInfo();     //获取order信息
        getNewOrder();
    }

    public void getOrderInfo(){
        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
        pa_publish_geton_tv.setText(order.getStartLoc());
        pa_publish_getoff_tv.setText(order.getEndLoc());
        pa_money_tv.setText(order.getMoney()+"");
        pa_publish_time_tv.setText(order.getStartTime().substring(5));
        pa_order_seq_tv.setText(order.getReOrSeq());
        if(order.getCondition().equals(0)){
            pa_order_con_tv.setText("等待司机接单");
        }
//        pa_publish_num_tv.setText("人数："+order.getPassNum());

        setOnClick();
    }

    public void initController(){
        pa_order_con_tv = findViewById(R.id.pa_order_con_tv);
        pa_order_seq_tv = findViewById(R.id.pa_order_seq_tv);
        pa_publish_geton_tv = findViewById(R.id.pa_publish_geton_tv);
        pa_publish_getoff_tv = findViewById(R.id.pa_publish_getoff_tv);
        pa_money_tv = findViewById(R.id.pa_money_tv);
        pa_publish_time_tv = findViewById(R.id.pa_publish_time_tv);
        pa_publish_route_bt = findViewById(R.id.pa_publish_route_bt);

        pa_appreciate_tv = findViewById(R.id.pa_appreciate_tv);
        pa_add_appreciate_tv = findViewById(R.id.pa_add_appreciate_tv);

        sus_driverCarInfo_cv = findViewById(R.id.sus_driverCarInfo_cv);
        pa_nowPassen_tv = findViewById(R.id.pa_nowPassen_tv);
        pa_driverName_tv = findViewById(R.id.pa_driverName_tv);
        car_describe_tv = findViewById(R.id.car_describe_tv);
        car_num_tv = findViewById(R.id.car_num_tv);
        driver_phone_tv = findViewById(R.id.driver_phone_tv);
        phone_call_iv = findViewById(R.id.phone_call_iv);
    }

    public void setOnClick(){
        pa_publish_route_bt.setOnClickListener(this);
    }

    //获取服务端订单状态
    public void getNewOrder(){
        getPresenter().getNewOrder(this,order.getReOrSeq());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.pa_publish_route_bt:
                intent = new Intent(this, RouteActivity.class);
                intent.putExtra("drivePath", RouteUtil.getDrivePath(order.getListSteps()));
                intent.putExtra("mStartPoint",RouteUtil.getLatLon(order.getStartLonLat()));
                intent.putExtra("mEndPoint",RouteUtil.getLatLon(order.getEndLonLat()));
                startActivity(intent);

                break;

            case R.id.msgLogin_tv:
                intent = new Intent(this,msgLoginActivity.class);


                startActivity(intent);
                break;
        }
    }

    @Override
    public void setPaOrderInfo(DriverCarInfo dcInfo,String result,Integer con) {
        if(con.equals(2)){
            pa_money_tv.setText(dcInfo.getSingleMoney()+"");
            pa_nowPassen_tv.setText(dcInfo.getPassenNum()+"人 /共"+dcInfo.getSeatNum()+"座");
            pa_driverName_tv.setText("司机"+dcInfo.getDriverName());
            car_describe_tv.setText(dcInfo.getCarColor()+" "+dcInfo.getCarBrand());
            car_num_tv.setText(dcInfo.getCarNum());
            driver_phone_tv.setText(dcInfo.getDriverPhone());
            sus_driverCarInfo_cv.setVisibility(View.VISIBLE);
            //DriverCarInfo缺少condition字段。。。。。。。。。。。。。。。。。。fuck
            //能获取到司机信息证明已经接单
            pa_order_con_tv.setText("已被接单");
//            if(order.getCondition().equals(2)){
//                pa_order_con_tv.setText("已被接单");
//            }
        }
        ToastUtil.show(this,result);
    }
}
