package com.example.carpoolgl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;

import org.xutils.view.annotation.ViewInject;

public class PublishActivity extends AppCompatActivity {

//    @ViewInject(R.id.publish_geton_tv)
    private TextView geton;
//    @ViewInject(R.id.publish_getoff_tv)
    private TextView getoff;
//    @ViewInject(R.id.publish_time_tv)
    private TextView time;
//    @ViewInject(R.id.publish_num_tv)
    private TextView number;
//    @ViewInject(R.id.publish_money_tv)
    private TextView money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
//        ViewUtils.inject(this);
        geton = findViewById(R.id.publish_geton_tv);
        getoff = findViewById(R.id.publish_getoff_tv);
        time = findViewById(R.id.publish_time_tv);
        number = findViewById(R.id.publish_num_tv);
        money = findViewById(R.id.publish_money_tv);
        initOrdersDetail();
    }

    public void initOrdersDetail(){
        Intent intent = getIntent();
        geton.setText(intent.getStringExtra("geton"));
        getoff.setText(intent.getStringExtra("getoff"));
        time.setText(intent.getStringExtra("time").replaceAll(" >",""));
        number.setText("人数："+intent.getStringExtra("number").charAt(0));
        money.setText("价格："+intent.getStringExtra("money")+"元");
    }
}
