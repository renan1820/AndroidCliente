package com.example.renan.cliente.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.renan.cliente.R;
import com.example.renan.cliente.fragment.InicioFragment;


public class MainActivity extends BaseApplication {

    private SharedPreferences sharedPreferences;
    private String CAMPOS_LOGIN = "LOGIN", cpf, senha;
    InicioFragment inicioFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pInfo != null;
        String version = pInfo.versionName;

        if(!verificaConexao()){
            Toast.makeText(this, "Verifique sua conexão", Toast.LENGTH_SHORT).show();
            finish();
        }

        sharedPreferences = getSharedPreferences(CAMPOS_LOGIN, Context.MODE_PRIVATE);
        cpf = sharedPreferences.getString("cpf",null);
        senha = sharedPreferences.getString("senha",null);
        Log.i("cpf"," cpf "+cpf+"senha"+senha);

        if (savedInstanceState == null) {
            InicioFragment frag = new InicioFragment();
            Bundle args = new Bundle();
            args.putString("version",version);
            args.putString("cpf",cpf);
            args.putString("senha",senha);
            frag.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.container, frag).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f instanceof InicioFragment) {//the fragment on which you want to handle your back press
            Log.i("BACK PRESSED", "BACK PRESSED");
        }else{
            super.onBackPressed();
        }

    }


}
