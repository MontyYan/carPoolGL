package com.example.carpoolgl;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.bean.DriverCarInfo;
import com.example.carpoolgl.bean.PayOrder;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.bean.payRequestInfo;
import com.example.carpoolgl.publishedOrder.passenger.passenOrderPresenter;
import com.example.carpoolgl.publishedOrder.passenger.passenOrderView;
import com.example.carpoolgl.route.RouteActivity;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;

public class Pa_OrderInfoActivity extends baseActivity<passenOrderView, passenOrderPresenter> implements View.OnClickListener ,passenOrderView{

    private CardView cost_card;
    private TextView pa_cost_tv;

    private passenOrderPresenter presenter;

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

    private ProgressBar pay_dialog_pb ;
    private TextView  pay_dialog_money_tv ;
    private PinEntryEditText pay_dialog_et;
    private Button pay_dialog_bt;
    private Button pay_cancel_bt;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private LinearLayout pay_dialog_ly;

    private DriverCarInfo driverCarInfo;


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
        }else if(order.getCondition().equals(1)){
            pa_order_con_tv.setText("已被接单");
        }
//        pa_publish_num_tv.setText("人数："+order.getPassNum());

        setOnClick();
    }

    public void initController(){

        cost_card = findViewById(R.id.cost_card);
        pa_cost_tv = findViewById(R.id.pa_cost_tv);

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
        pa_cost_tv.setOnClickListener(this);
    }

    //获取服务端订单状态
    public void getNewOrder(){
        presenter = getPresenter();
        presenter.getNewOrder(this,order.getReOrSeq(), STATIC_USERINFO.getUserSeq());
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

            case R.id.pa_cost_tv:
                setDialog();
                break;
        }
    }

    @Override
    public void setPaOrderInfo(DriverCarInfo dcInfo,String result,Integer con) {
        if(con.equals(1)){
            driverCarInfo = dcInfo;
            pa_money_tv.setText(dcInfo.getSingleMoney()+dcInfo.getAppreMoney()+"");
            pa_nowPassen_tv.setText(dcInfo.getPassenNum()+"人 /共"+dcInfo.getSeatNum()+"座");
            pa_driverName_tv.setText("司机"+dcInfo.getDriverName());
            car_describe_tv.setText(dcInfo.getCarColor()+" "+dcInfo.getCarBrand());
            car_num_tv.setText(dcInfo.getCarNum());
            driver_phone_tv.setText(dcInfo.getDriverPhone());
            sus_driverCarInfo_cv.setVisibility(View.VISIBLE);
//            pa_order_con_tv.setText("已被接单");
            if(dcInfo.getOrderCon().equals(1)){
                pa_order_con_tv.setText("已被接单");
                SharedPreferences.Editor shEdit = getSharedPreferences("pa_orderinfo", Context.MODE_PRIVATE).edit();
                shEdit.putInt("condition",1);
            }
            if(dcInfo.getOrderCon().equals(2)){   //订单已经完成，乘客界面添加支付控件
                cost_card.setVisibility(View.VISIBLE);
            }
        }
        ToastUtil.show(this,result);
    }

    @Override
    public void setPayDialog(Integer result, String payOrder) {
        /*
        * reuslt为1，则表明支付请求通过，可以显示支付页面
        * */
        if(result.equals(1)){
            showDialog(payOrder);
            //TODO 保存支付订单信息在sharepreference中，
            // 用于支付订单已经完成，但没有支付，退出应用，再打开应用时候还原支付订单信息，
        }
    }

    @Override
    public void PayingDialog(Integer result,String payResult) {
        if(result.equals(1)){
            ToastUtil.show(this,payResult);
            dialog.dismiss();
        }else {
            ToastUtil.show(this,"支付失败");
            dialog.dismiss();
        }
    }
    /*
    * 显示支付窗口
    * 窗口先显示pb 等待支付请求结果
    * */
    public void setDialog(){
        builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        dialog.setTitle("支付订单 ");
//        builder.setTitle("支付订单 ");
        View view = LayoutInflater.from(this).inflate(R.layout.pay_dialog,null);
        pay_dialog_pb = view.findViewById(R.id.pay_dialog_pb);
        pay_dialog_money_tv = view.findViewById(R.id.pay_dialog_money_tv);
        pay_dialog_et = view.findViewById(R.id.pay_dialog_et);
        pay_dialog_bt = view.findViewById(R.id.pay_dialog_bt);
        pay_cancel_bt = view.findViewById(R.id.pay_cancel_bt);
        pay_dialog_ly = view.findViewById(R.id.pay_dialog_ly);
        pay_dialog_ly.setVisibility(View.GONE);
        pay_dialog_pb.setVisibility(View.VISIBLE);
        dialog.setView(view);

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //开始支付请求
        presenter.payRequest(this,getPayReInfo());
    }

    /*
    * 初始化支付订单申请信息
    * */
    public payRequestInfo getPayReInfo(){

        return new payRequestInfo(driverCarInfo.getPassenSeq(),driverCarInfo.getDriverSeq(),driverCarInfo.getAppreMoney()+driverCarInfo.getSingleMoney());
    }

    /*
    * 支付请求通过，dialog界面由pb转换为密码输入框
    * */
    public void showDialog(final String seq){
//        builder.setTitle("支付订单："+seq);
        dialog.setTitle("支付订单："+seq);
        pay_dialog_ly.setVisibility(View.VISIBLE);      //显示linearLayout
        pay_dialog_pb.setVisibility(View.GONE);         //隐藏pb

        /*
        * 确定
        * 输入密码后，点击执行支付流程
        * */
        pay_dialog_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {        //设置支付点击事件
                //TODO 判断密码是否输入
                /* function()  */

                ToastUtil.show(Pa_OrderInfoActivity.this,"支付");
                PayOrder payOrder = new PayOrder(seq,
                        driverCarInfo.getPassenSeq(),
                        driverCarInfo.getDriverSeq(),
                        driverCarInfo.getAppreMoney()+driverCarInfo.getSingleMoney(),
                        pay_dialog_et.getText().toString());
                /*
                * 进行支付请求
                *
                * 先注释，测试前面支付请求显示
                * */
//                presenter.payOrder(Pa_OrderInfoActivity.this,payOrder);
                ToastUtil.show(Pa_OrderInfoActivity.this,"进行支付");
            }
        });

        /*
        * 取消
        * 取消支付流程
        * */
        pay_cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setCanceledOnTouchOutside(true);
                dialog.dismiss();
            }
        });
    }
}
