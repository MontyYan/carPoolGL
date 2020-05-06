package com.example.carpoolgl.base.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class baseFragment<V extends baseFragView, P extends baseFragPresenter<V>> extends Fragment {
    private P presenter;
    private V view;

    public abstract P createPresenter();
    public abstract V createView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.view ==null){
            this.view = createView();
        }
        if(this.presenter==null){
            this.presenter = createPresenter();
        }
        if(this.presenter!=null && this.view!=null){
            this.presenter.attachView(this.view);
        }
    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.presenter!=null && this.view!=null){
            this.presenter.detachView();
        }
    }
}
