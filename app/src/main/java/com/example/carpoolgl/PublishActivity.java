package com.example.carpoolgl;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.example.carpoolgl.base.activity.baseActivity;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.publish.pubHandler;
import com.example.carpoolgl.publish.publishPresenter;
import com.example.carpoolgl.publish.publishView;

public class PublishActivity extends baseActivity<publishView, publishPresenter> implements publishView{

    private static final String TAG="PublishActivity";

    private TextView geton;
    private TextView getoff;
    private TextView time;
    private TextView number;
    private TextView money;
    private CardView publishing_card;
    private TextView publishing_tv;
    private ProgressBar publishing_pb;
    private LinearLayout publishing_lin;
    private RelOrder order;
    private Integer IDENTITY;

    private LatLonPoint mStartPoint = new LatLonPoint(39.995576,116.481288);
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        init();
        getOrdersDetail();
        publish();
    }

    @Override
    public publishPresenter createPresenter() {
        return new publishPresenter();
    }

    @Override
    public publishView createView() {
        return this;
    }

    public void init(){
        geton = findViewById(R.id.publish_geton_tv);
        getoff = findViewById(R.id.publish_getoff_tv);
        time = findViewById(R.id.publish_time_tv);
        number = findViewById(R.id.publish_num_tv);
        money = findViewById(R.id.publish_money_tv);
        publishing_card = findViewById(R.id.publishing_card);
        publishing_tv = findViewById(R.id.publishing_tv);
        publishing_pb = findViewById(R.id.publishing_pb);
        publishing_lin = findViewById(R.id.publishing_lin);
    }

    //获取intent数据后，更新order信息，
    public void getOrdersDetail(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        order = bundle.getParcelable("relorder");
        IDENTITY = intent.getIntExtra("identity",-1);
        Log.i(TAG,order.toString());
        //先设置UI数据，但是此时不显示
        geton.setText(order.getStartLoc());
        getoff.setText(order.getEndLoc());
        time.setText(order.getStartTime().substring(5));
        number.setText("人数："+order.getPassNum());
        money.setText("价格："+order.getMoney()+"元");
        Log.i(TAG,order.toString());
        //测试，用于输出超长Log***************
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                pubHandler pubHandler = new pubHandler();
                Message msg = pubHandler.obtainMessage();
                msg.what = 9;
                msg.obj = order;
                pubHandler.sendMessage(msg);
                Looper.loop();
            }
        }).start();
        //*********************************
    }

    public void publish(){
        getPresenter().publishing(PublishActivity.this,order,publishing_tv,publishing_pb,publishing_card,IDENTITY);
    }

    /*
    *
    * */
    @Override
    public void showOrders() {

    }
}
