package com.example.renan.cliente.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.renan.cliente.R;
import com.example.renan.cliente.Util.TratamentoImagem;
import com.example.renan.cliente.domain.Ids;

import livroandroid.lib.utils.PermissionUtils;

/**
 * Created by Hermanos 04 on 09/11/2017.
 */

public class EntrarFragment extends BaseFragment {
    private String CAMPOS_CADASTRO = "CADASTRO";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container,false);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(CAMPOS_CADASTRO, Context.MODE_PRIVATE).edit();
        editor.clear().apply();

        TratamentoImagem carregar = new TratamentoImagem();

        final ImageView loding =(ImageView)view.findViewById(R.id.imageView4);
        final ImageView img_fundo =(ImageView)view.findViewById(R.id.imageView10);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            loding.setImageBitmap(carregar.carregarBitmap(loding.getMaxHeight(),loding.getMaxWidth(),R.drawable.teste_logo2, getResources()));
            img_fundo.setImageBitmap(carregar.carregarBitmap(img_fundo.getMaxHeight(),img_fundo.getMaxWidth(),R.drawable.alou_alou, getResources()));
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            TextView textView6 = (TextView) view.findViewById(R.id.textView6);
            textView6.setTextSize(14);
        }


        try {
            loding.clearAnimation();
            loding.destroyDrawingCache();
            img_fundo.clearAnimation();
            img_fundo.destroyDrawingCache();
            Log.i("destroindo  imagem","Tela Inicial");
        } catch (Exception e) {
            Log.i("Erro imagem", "Tela Inicial" + e);
        }

        AlphaAnimation fadeIn = new AlphaAnimation(1.0f , 0.5f ) ;
        AlphaAnimation fadeOut = new AlphaAnimation(0.5f , 1.0f ) ;

        fadeIn.setDuration(2000);
        fadeOut.setDuration(2000);

        fadeIn.setRepeatCount(Animation.INFINITE);


        loding.startAnimation(fadeIn);
        loding.startAnimation(fadeOut);

        Button btn_entrar = (Button) view.findViewById(R.id.btn_entrar);
        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Tela_Inicial.this,Tela_Verificacao_CPF.class));

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                LogarFragment f = new LogarFragment();
                t.replace(R.id.container,f,Ids.TELA_LOGAR);
                t.addToBackStack(Ids.TELA_ENTRAR);
                t.commit();

                try {
                    loding.clearAnimation();
                    loding.destroyDrawingCache();
                    img_fundo.clearAnimation();
                    img_fundo.destroyDrawingCache();
                    Log.i("destroindo  imagem","Tela Inicial");
                } catch (Exception e) {
                    Log.i("Erro imagem", "Tela Inicial" + e);
                }


            }
        });

        Button btn_cad = (Button) view.findViewById(R.id.btn_cad);
        btn_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Tela_Inicial.this,Tela_Cadastro.class));

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                CadCadastroFragment f = new CadCadastroFragment();
                t.replace(R.id.container,f,Ids.TELA_CADASTRO_DADOS);
                t.addToBackStack(Ids.TELA_ENTRAR);
                t.commit();

                try {
                    loding.clearAnimation();
                    loding.destroyDrawingCache();
                    img_fundo.clearAnimation();
                    img_fundo.destroyDrawingCache();
                    Log.i("destroindo  imagem","Tela Inicial");
                } catch (Exception e) {
                    Log.i("Erro imagem", "Tela Inicial" + e);
                }

            }
        });


        //Permissões necessária caso celular for Android 6.0 ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissoes = new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
            PermissionUtils.validate(getActivity(),0,permissoes);
//
        }
        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        for (int result: grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) {
                // Negou a permissão. Mostra alerta e fecha.

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setTitle(R.string.app_name).setMessage(R.string.msg_alerta_permissao);
                // Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                });
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();

                return;
            }
        }


    }
}
