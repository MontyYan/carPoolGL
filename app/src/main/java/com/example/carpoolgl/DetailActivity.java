package com.example.carpoolgl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private View bottom_Detail;
    private BottomSheetBehavior mBoSheetBehavior;
    private TextView detail_geton_tv;
    private TextView detail_getoff_tv;
    private TextView start_time;
    private TextView person_num;
    private TextView Fog;
    private Button publish_bt;


    private String startAddr;
    private String endAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initFvb();
        setStartEndTv();


        mBoSheetBehavior = BottomSheetBehavior.from(bottom_Detail);
        mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        mBoSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });


    }

    public void setStartEndTv(){
        Intent intent = getIntent();
        startAddr = intent.getStringExtra("startAddress");
        endAddr = intent.getStringExtra("endAddress");
        detail_geton_tv.setText(startAddr);
        detail_getoff_tv.setText(endAddr);
    }

    public void initFvb(){
        bottom_Detail = findViewById(R.id.bottom_detail);
        detail_geton_tv = findViewById(R.id.detail_geton_tv);
//        detail_geton_tv.setOnClickListener(this);
        detail_getoff_tv = findViewById(R.id.detail_getoff_tv);
//        detail_getoff_tv.setOnClickListener(this);
        Fog = findViewById(R.id.fog);
        Fog.setOnClickListener(this);
        start_time = findViewById(R.id.start_time);
        start_time.setOnClickListener(this);
        person_num = findViewById(R.id.person_num);
        person_num.setOnClickListener(this);
        publish_bt = findViewById(R.id.publish_bt);
        publish_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_time:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Fog.setVisibility(View.VISIBLE);
                break;
            case R.id.person_num:
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Fog.setVisibility(View.VISIBLE);
                break;
            case R.id.fog:
                Fog.setVisibility(View.INVISIBLE);
                mBoSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }
}
