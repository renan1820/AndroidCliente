package com.example.renan.cliente.activity;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.renan.cliente.R;


/**
 * Created by Hermanos 04 on 09/11/2017.
 */

public class BaseApplication extends livroandroid.lib.activity.BaseActivity {

    // Configura a Toolbar
    protected void setUpToolbar(String titulo) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titulo_toolbar = (TextView) findViewById(R.id.text_toolbar);
        //Botao de Retorno
        if(toolbar != null){
            setSupportActionBar(toolbar);
            toolbar.setTitle(" ");
            titulo_toolbar.setText(titulo);
            //toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
            //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        finish();
//
            //    }
            //});
            //toolbar.inflateMenu(R.menu.main);
        }
    }
}
