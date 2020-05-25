package com.example.carpoolgl.base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class baseActivity<V extends baseView, P extends basePresenter<V>> extends AppCompatActivity {
    private P presenter;
    private V view;

    public abstract P createPresenter();
    public abstract V createView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
    protected void onDestroy() {
        super.onDestroy();
        if(this.presenter!=null && this.view!=null){
            this.presenter.detachView();
        }
    }
}
