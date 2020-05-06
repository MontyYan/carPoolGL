package com.example.carpoolgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carpoolgl.R;
import com.example.carpoolgl.base.fragment.baseFragment;
import com.example.carpoolgl.fragment.driver.driverPresenter;
import com.example.carpoolgl.fragment.driver.driverView;

public class DriverFragment extends baseFragment<driverView, driverPresenter> implements driverView {
//    private TextView driver_tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        driver_tv = view.findViewById(R.id.driver_tv);
    }
    @Override
    public driverPresenter createPresenter() {
        return new driverPresenter();
    }

    @Override
    public driverView createView() {
        return this;
    }
}
