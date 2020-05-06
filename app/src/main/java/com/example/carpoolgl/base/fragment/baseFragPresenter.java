package com.example.carpoolgl.base.fragment;

public abstract class baseFragPresenter<V extends baseFragView> {
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
