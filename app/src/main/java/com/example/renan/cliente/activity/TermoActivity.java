package com.example.renan.cliente.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.renan.cliente.R;
import com.example.renan.cliente.Util.JSONfunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Hermanos 04 on 10/11/2017.
 */

public class TermoActivity extends BaseApplication {

    TextView termo;
    ProgressBar progress;
    String msgTratada = "isso é um teste";

    JSONObject jsonObject;
    JSONArray jsonArray;
    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termo);

        Intent intent = getIntent();
        this.URL = intent.getStringExtra("url");
        if(URL== null || URL.equals("")){
            URL = "http://siga.grupohermanos.com.br:8056/valuetravel/webservice/termouso.jsp";
        }
        Log.i("URL",URL);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Botao de Retorno
        if(toolbar != null){
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                }
            });
            toolbar.inflateMenu(R.menu.main1);
        }

        new Termo().execute();
    }

    //JSON enviado para cada alteração no botão
    private class Termo extends AsyncTask<Object, Object, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            termo =(TextView) findViewById(R.id.texto);
            progress = (ProgressBar)findViewById(R.id.progress);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... params) {

            Log.w("aux",URL);

            String url = JSONfunctions.getJSONfromURL(URL);

            try {
                jsonObject = new JSONObject(url);

            } catch (Throwable t) {
                Log.e("aux", "Má formação no JSON:" + url);
            }
            try {
                jsonArray = jsonObject.getJSONArray("dados");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    msgTratada = c.getString("texto");
                    //Recebimendo das informações do JSON
                }
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //startActivity(new Intent(getBaseContext(),Tela_Inicial.class));
                        //finish();
                        //Toast.makeText(TelaCarregando.this, "Erro ao conectar, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                        Log.e("aux", "Erro na leitura do campo: " + e.getMessage());
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            progress.setVisibility(View.GONE);
            try {
                msgTratada = URLDecoder.decode(msgTratada, "UTF-8");

                termo.setText(Html.fromHtml(msgTratada));


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }
    }


}
