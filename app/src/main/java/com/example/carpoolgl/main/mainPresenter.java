package com.example.carpoolgl.main;

import android.content.Context;

import com.example.carpoolgl.base.activity.basePresenter;

public class mainPresenter extends basePresenter<mainView> {

    private mainModel model;
//    private Context context;

    public mainPresenter(){
        this.model = new mainModel();
//        this.context = context;
    }

    public void findLocUInfo(Context context){
        mainView view = getView();
        this.model.findUInfo(context,view);
        this.model.findOrder(context,view);
//        this.model.findOrder(context,view);
    }




}
