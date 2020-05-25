package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.bean.CarInfo;
import com.example.carpoolgl.join.joinDrPresenter;
import com.example.carpoolgl.join.joinDrView;
import com.example.carpoolgl.util.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
public class JoinDrActivity extends baseActivity<joinDrView, joinDrPresenter> implements View.OnClickListener,joinDrView{


    private ImageView join_dr_back_iv;
    private TextView user_seq_tv;
    private TextView joining_tv;
    private MaterialEditText car_type_et;
    private MaterialEditText car_color_et;
    private MaterialEditText car_num_et;
    private MaterialEditText car_chair_et;
    private Button join_dr_bt;
//    private ProgressBar joining_pb;

    private CarInfo carInfo;
    private String UserSeq;
    private Integer downTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_dr);
        initController();
        getUserSeq();

    }

    public void initController(){
        join_dr_back_iv = findViewById(R.id.join_dr_back_iv);
        user_seq_tv = findViewById(R.id.user_seq_tv);
        car_type_et = findViewById(R.id.car_type_et);
        car_color_et = findViewById(R.id.car_color_et);
        car_num_et = findViewById(R.id.car_num_et);
        car_chair_et = findViewById(R.id.car_chair_et);
        join_dr_bt = findViewById(R.id.join_dr_bt);
        joining_tv = findViewById(R.id.joining_tv);

        setOnClick();
    }

    @Override
    public joinDrPresenter createPresenter() {
        return new joinDrPresenter();
    }

    @Override
    public joinDrView createView() {
        return this;
    }

    public void getUserSeq(){
        Intent intent = getIntent();
        UserSeq = intent.getStringExtra("userSeq");
        user_seq_tv.setText(UserSeq);
    }

    public void setOnClick(){
        join_dr_back_iv.setOnClickListener(this);
        join_dr_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.join_dr_bt:
                setCarInfo();
                cutDown();
                joinDrRequest();
                break;
                //左上角返回按钮
            case R.id.join_dr_back_iv:
                finish();
                break;
        }
    }

    public void setCarInfo(){

        carInfo = new CarInfo();
        carInfo.setUserSeq(UserSeq);
        carInfo.setCarNuM(car_num_et.getText()+"");
        carInfo.setCarBrand(car_type_et.getText()+"");
        carInfo.setCarColor(car_color_et.getText()+"");
        carInfo.setCarSeatNum(Integer.valueOf(car_chair_et.getText()+""));
    }

    public void joinDrRequest(){
        getPresenter().joinDriver(this,carInfo);
    }

    //获取到服务端数据 回调方法
    @Override
    public void JoinDrResult(Integer result,String info) {
        ToastUtil.show(JoinDrActivity.this,info);
        if(result.equals(1)){ //加入司机操作成功
            finish();
        }else{
            downTime=-2;
            EditClickable_true();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                EditClickable_false();
                String str ="";
                if(downTime%3==0){
                    str = "正在加入...";
                }else if(downTime%3==1){
                    str = "正在加入..";
                }else if(downTime%3==2){
                    str = "正在加入.";
                }
                joining_tv.setText(str);

            }else if(msg.what==2){
                EditClickable_true();
                ToastUtil.show(JoinDrActivity.this,"操作超时");
                downTime = 30;
            }
        }
    };


    public void cutDown(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;downTime>0;downTime--){
                    handler.sendEmptyMessage(1);
//                    if(downTime<=0){
//                        break;
//                    }
                    try{
                        Thread.sleep(700);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(downTime==0){
                    handler.sendEmptyMessage(2);
                }

            }
        }).start();

    }

    public void EditClickable_false(){
        Boolean flag = false;
        car_type_et.setFocusable(flag);
        car_color_et.setFocusable(flag);
        car_num_et.setFocusable(flag);
        car_chair_et.setFocusable(flag);
        join_dr_bt.setVisibility(View.GONE);
        joining_tv.setVisibility(View.VISIBLE);
    }

    public void EditClickable_true(){
        Boolean flag = true;
        car_type_et.setFocusable(flag);
        car_color_et.setFocusable(flag);
        car_num_et.setFocusable(flag);
        car_chair_et.setFocusable(flag);
        join_dr_bt.setVisibility(View.VISIBLE);
        joining_tv.setVisibility(View.GONE);
    }

}
