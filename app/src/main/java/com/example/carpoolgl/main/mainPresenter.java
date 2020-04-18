package com.example.carpoolgl.main;

import android.content.Context;

import com.example.carpoolgl.base.basePresenter;

public class mainPresenter extends basePresenter<mainView> {

    private mainModel model;
    private Context context;

    public mainPresenter(Context context){
        this.model = new mainModel();
        this.context = context;
    }

    public void findLocUInfo(){
        this.model.findUInfo(context,getView());
    }

}
