package com.example.carpoolgl.base.activity;

public abstract class basePresenter<V extends baseView> {
    private V view;

    public V getView(){
        return view;
    }
    public void attachView(V view){
        this.view = view;
    }

    public void detachView(){
        this.view = null;
    }
}
