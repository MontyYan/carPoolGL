package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.example.carpoolgl.Static.STATIC_USERINFO;
import com.example.carpoolgl.base.baseActivity;
import com.example.carpoolgl.base.baseView;
import com.example.carpoolgl.bean.RelOrder;
import com.example.carpoolgl.publish.publishPresenter;
import com.example.carpoolgl.publish.publishView;
import com.lidroid.xutils.ViewUtils;

import org.xutils.view.annotation.ViewInject;

public class PublishActivity extends baseActivity<publishView, publishPresenter> implements publishView{

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

    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        init();
        initOrdersDetail();
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

    public void initOrdersDetail(){
        Intent intent = getIntent();
        String startLoc = intent.getStringExtra("geton");
        geton.setText(startLoc);
        String endLoc = intent.getStringExtra("getoff");
        getoff.setText(endLoc);
        mStartPoint = intent.getParcelableExtra("mStartPoint");
        mEndPoint = intent.getParcelableExtra("mEndPoint");
        String startTime = intent.getStringExtra("time").replaceAll(" >","");
        time.setText(startTime);
        String num = intent.getStringExtra("number").charAt(0)+"";
        number.setText("人数："+num);
        String mon = intent.getStringExtra("money");
        money.setText("价格："+mon+"元");

    }

    public void initRelOrder(String startLoc,String endLoc,String startTime,String num, String mon){
        order.setRePaSeq(STATIC_USERINFO.getUserSeq());

    }

    public void publish(){

        getPresenter().publishing(order,publishing_lin,publishing_card);
    }

    /*
    * 取消progress，显示发布订单order
    * */
    @Override
    public void showOrders() {

    }
}
