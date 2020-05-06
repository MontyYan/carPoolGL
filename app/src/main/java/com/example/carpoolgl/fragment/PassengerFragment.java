package com.example.carpoolgl.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.amap.api.services.core.LatLonPoint;
import com.example.carpoolgl.OrderInfoActivity;
import com.example.carpoolgl.PhoneActivity;
import com.example.carpoolgl.R;
import com.example.carpoolgl.SearchActivity;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.fragment.baseFragment;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.fragment.passenger.passengerPresenter;
import com.example.carpoolgl.fragment.passenger.passengerView;
import com.example.carpoolgl.nowLoc.nowLocPresenter;
import com.example.carpoolgl.nowLoc.nowLocView;
import com.google.android.material.navigation.NavigationView;

import static android.content.Context.MODE_PRIVATE;

public class PassengerFragment extends baseFragment<passengerView, passengerPresenter> implements View.OnClickListener, nowLocView, passengerView{

    private static final String TAG="MainActivity";

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
    private CardView main_published_order_cv;
    private TextView main_start_loc_tv;
    private TextView main_end_loc_tv;
    private TextView main_date_tv;
    private TextView main_num_tv;
    private Button main_orderDetail_bt;
    private TextView main_money_tv;

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
        getOnTv.setOnClickListener(this);
        getOffTv = view.findViewById(R.id.getOff_TV);
        getOffTv.setOnClickListener(this);
        main_published_order_cv = view.findViewById(R.id.main_published_order_cv);
        main_start_loc_tv=view.findViewById(R.id.main_start_loc_tv);
        main_end_loc_tv = view.findViewById(R.id.main_end_loc_tv);
        main_date_tv = view.findViewById(R.id.main_date_tv);
        main_num_tv = view.findViewById(R.id.main_num_tv);
        main_orderDetail_bt = view.findViewById(R.id.main_orderDetail_bt);
        main_orderDetail_bt.setOnClickListener(this);
        main_money_tv = view.findViewById(R.id.main_money_tv);

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
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"请先登录账号",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.main_orderDetail_bt:
                intent = new Intent(getActivity(), OrderInfoActivity.class);
                startActivity(intent);
                break;

        }
    }

    //实现初始化已发布订单信息与已登录用户信息
    public void init_Info_order(){
        getPresenter().findLocUInfo(getActivity());
    }



    //退出登录后，没有登录，本地用户登录数据覆盖
    public void deShareData(){
        STATIC_USERINFO.setCon(0);
        SharedPreferences.Editor shaEdit = getActivity().getSharedPreferences("userinfo",MODE_PRIVATE).edit();
        shaEdit.putInt("conCode",0);
        shaEdit.putString("phone","未登录");
        shaEdit.putString("registerDate","");
        shaEdit.putString("sequence","");
        shaEdit.apply();

    }

    //设置全局静态
    @Override
    public void SetUInfo() {
//        PhoneNum_Navheader_tv.setText(STATIC_USERINFO.getPhone());
//        regDate_Navheader_tv.setText("注册日期："+STATIC_USERINFO.getRegisterDate());
    }

    @Override
    public void SetOrder(RelOrder order) {
        main_published_order_cv.setVisibility(View.VISIBLE);
        main_start_loc_tv.setText(order.getStartLoc());
        main_end_loc_tv.setText(order.getEndLoc());
        main_date_tv.setText(order.getStartTime().substring(5));
        main_num_tv.setText(order.getPassNum()+"");
        main_money_tv.setText(order.getMoney()+"");
    }

    //toast当前中文地址
    @Override
    public void setGetonText(String addr) {
        nowLocation = addr;
        getOnTv.setText(addr);
//        Toast.makeText(this,addr,Toast.LENGTH_LONG).show();
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
