package com.example.renan.cliente;

import android.support.multidex.MultiDexApplication;

public class Application extends MultiDexApplication {

    private static Application instance = null;
    public static Application getInstance(){ return instance;}

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
