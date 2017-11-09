package com.example.renan.cliente.activity;

import android.os.Bundle;

import com.example.renan.cliente.R;


public class MainActivity extends BaseApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar("Início");
    }
}
