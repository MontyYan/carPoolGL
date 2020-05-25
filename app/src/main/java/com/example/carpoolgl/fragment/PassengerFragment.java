package com.example.carpoolgl.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.example.carpoolgl.Pa_OrderInfoActivity;
import com.example.carpoolgl.R;
import com.example.carpoolgl.SearchActivity;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.fragment.baseFragment;
import com.example.carpoolgl.bean.RecordInfo;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.dataBase.orderRecord.RecordHelper;
import com.example.carpoolgl.fragment.passenger.passengerPresenter;
import com.example.carpoolgl.fragment.passenger.passengerView;
import com.example.carpoolgl.nowLoc.nowLocPresenter;
import com.example.carpoolgl.nowLoc.nowLocView;
import com.example.carpoolgl.util.RouteUtil;
import com.example.carpoolgl.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;

public class PassengerFragment extends baseFragment<passengerView, passengerPresenter> implements View.OnClickListener, nowLocView, passengerView{

    private static final String TAG="MainActivity";
    private static final int PASSENID=1;

    private TextView getOnTv;
    private TextView getOffTv;
    private ImageButton personalInfo;
    private nowLocPresenter nowLocP;
    private String nowLocation;
    //drawelayout不覆盖
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private TextView QuitTv;
    private LatLonPoint nowLatLon = new LatLonPoint(39.995576,116.481288);//记录当前位置的经纬度

    //个人用户数据控件
    private TextView PhoneNum_Navheader_tv;
    private TextView regDate_Navheader_tv;

    //路线测试
//    private Button searchRoute;

    //当前发布订单控件
    private CardView pa_published_order_cv;
    private TextView pa_start_loc_tv;
    private TextView pa_end_loc_tv;
    private TextView pa_date_tv;
    private TextView pa_num_tv;
    private Button pa_orderDetail_bt;
    private TextView pa_money_tv;
    private RelOrder order;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initController(view);
        init_Info_order();
        nowLocP = new nowLocPresenter(getActivity());
        nowLocP.attachView(this);
//        pass_tv = view.findViewById(R.id.pass_tv);
    }


    @Override
    public passengerPresenter createPresenter() {
        return new passengerPresenter();
    }

    @Override
    public passengerView createView() {
        return this;
    }

    public void initController(View view){
        getOnTv = view.findViewById(R.id.getOn_TV);
        getOffTv = view.findViewById(R.id.getOff_TV);
        pa_published_order_cv = view.findViewById(R.id.pa_published_order_cv);
        pa_start_loc_tv=view.findViewById(R.id.pa_start_loc_tv);
        pa_end_loc_tv = view.findViewById(R.id.pa_end_loc_tv);
        pa_date_tv = view.findViewById(R.id.pa_date_tv);
//        pa_num_tv = view.findViewById(R.id.pa_num_tv);
        pa_orderDetail_bt = view.findViewById(R.id.pa_orderDetail_bt);
        pa_money_tv = view.findViewById(R.id.pa_money_tv);

        setOnClick();
    }

    public void setOnClick(){
        getOnTv.setOnClickListener(this);
        getOffTv.setOnClickListener(this);
        pa_orderDetail_bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        boolean flag = false;   //false代表getonTv编辑，true代表getoffTv编辑
        Intent intent;
        switch (v.getId()){
            case R.id.getOn_TV:
                if(!STATIC_USERINFO.getCon().equals(0)){
                    flag = false;
                    intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("Edit_select",flag);
                    intent.putExtra("nowLocation",nowLocation);
                    intent.putExtra("nowLatLon",nowLatLon);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"请先登录账号",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.getOff_TV:
                if(!STATIC_USERINFO.getCon().equals(0)){
                    flag = true;
                    intent = new Intent(getActivity(),SearchActivity.class);
                    intent.putExtra("Edit_select",flag);
                    intent.putExtra("nowLocation",nowLocation);
                    intent.putExtra("nowLatLon",nowLatLon);
                    intent.putExtra("identity",PASSENID);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"请先登录账号",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.pa_orderDetail_bt:
                intent = new Intent(getActivity(), Pa_OrderInfoActivity.class);
                intent.putExtra("order",this.order);
                Log.i("paorderinfo",order.toString());
                startActivity(intent);
                break;

//            case R.id.insetTest_bt:
//                insertTest();
//                ToastUtil.show(getActivity(),"点击插入数据");
//                break;

        }
    }

    //实现初始化已发布订单信息与已登录用户信息
    public void init_Info_order(){
        getPresenter().findPa_OrderInfo(getActivity());
    }


    //设置全局静态
    @Override
    public void SetUInfo() {
//        PhoneNum_Navheader_tv.setText(STATIC_USERINFO.getPhone());
//        regDate_Navheader_tv.setText("注册日期："+STATIC_USERINFO.getRegisterDate());
    }

    @Override
    public void SetOrder(RelOrder order) {
        this.order = order;

        pa_published_order_cv.setVisibility(View.VISIBLE);
        pa_start_loc_tv.setText(order.getStartLoc());
        pa_end_loc_tv.setText(order.getEndLoc());
        pa_date_tv.setText("出发时间："+order.getStartTime().substring(5));

//        pa_money_tv.setText(order.getMoney()+""); //主页面显示订单不应该有具体花费数额 ，随着加入人数增多，数额需要变化，而主页面不提供实时更新
    }

    //toast当前中文地址
    @Override
    public void setGetonText(String addr) {
        nowLocation = addr;
        getOnTv.setText(addr);
    }

    @Override
    public void setLatLon(double lat, double lon) {
        nowLatLon.setLongitude(lon);
        nowLatLon.setLatitude(lat);
    }

    //销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        nowLocP.destory();
        nowLocP.detachView();
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
