package com.example.renan.cliente.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.renan.cliente.R;
import com.example.renan.cliente.Util.CheckNetworkConnection;
import com.example.renan.cliente.Util.JSONfunctions;
import com.example.renan.cliente.Util.TratamentoImagem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by Hermanos 04 on 09/11/2017.
 */

public class InicioFragment extends BaseFragment {
    private ImageView intro_value,back_intro;
    private TextView bemvindo, versao;
    private static int SPLASH_TIME_OUT = 5000;
    private ProgressBar progress;

    String cpf, senha, CAMPOS_LOGIN = "LOGIN" ,mensagem, coderro,version;
    String codcliente,saldopendente,saldoefetivado,apelido,nome, notificacao,foto;


    JSONObject jsonObject;
    JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.activity_carregar,container,false);
        cpf = getArguments().getString("cpf");
        senha = getArguments().getString("senha");
        version = getArguments().getString("version");



        Log.i("Login Automatico"," Cpf: "+cpf+ " senha: "+senha);
        //Log.i("Token",FirebaseInstanceId.getInstance().getToken());

        TratamentoImagem carregar = new TratamentoImagem();

        intro_value = (ImageView) view.findViewById(R.id.intro_value);
        intro_value.setImageBitmap(carregar.carregarBitmap(intro_value.getMaxHeight(),intro_value.getMaxWidth(),R.drawable.teste_logo2, getResources()));


        progress = (ProgressBar) view.findViewById(R.id.progress);
        //intro_cdl = (ImageView) findViewById(R.id.intro_cdl);
        bemvindo =(TextView) view.findViewById(R.id.bemvindo);

        versao = (TextView) view.findViewById(R.id.versao);
        versao.setText("Versão "+ version);

        back_intro = (ImageView) view.findViewById(R.id.back_intro);

        back_intro.setImageBitmap(carregar.carregarBitmap(back_intro.getMaxHeight(),back_intro.getMaxWidth(),R.drawable.fundo_inicio, getResources()));

        try {
            intro_value.clearAnimation();
            intro_value.destroyDrawingCache();
            back_intro.clearAnimation();
            back_intro.destroyDrawingCache();
            Log.i("destroindo  imagem","TelaCarregando");
        } catch (Exception e) {
            Log.i("Erro imagem", "TelaCarregando" + e);
        }

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(4000);
        animation1.setFillAfter(true);
        AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
        animation2.setDuration(4000);
        animation2.setFillAfter(true);
        AlphaAnimation animation3 = new AlphaAnimation(0.0f, 1.0f);
        animation3.setDuration(2500);
        animation3.setFillAfter(true);


        intro_value.startAnimation(animation1);
        bemvindo.startAnimation(animation2);
        back_intro.startAnimation(animation3);

        if(CheckNetworkConnection.isConnectionAvailable(getContext())){
            //new Login().execute();
        }else{
            snack(getView(),"Verifique sua conexão a Internet");
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction t = fragmentManager.beginTransaction();
            //EntrarFragment f = new EntrarFragment();
            //t.replace(R.id.container,f,"Entrar");
            //t.commit();
        }

        return view;
    }


    private class Login extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.w("aux","http://siga.grupohermanos.com.br:8056/valuetravel/webservice/acessocliente.jsp?cpf="+cpf+"&senha="+senha);

            String url = JSONfunctions.getJSONfromURL("http://siga.grupohermanos.com.br:8056/valuetravel/webservice/acessocliente.jsp?cpf="+cpf+"&senha="+senha);

            try {
                jsonObject = new JSONObject(url);

            } catch (Throwable t) {
                Log.e("aux", "Má formação no JSON:" + url);
            }
            try {
                jsonArray = jsonObject.getJSONArray("dados");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    //Recebimendo das informações do JSON
                    mensagem            =c.getString("mensagem");
                    coderro             =c.getString("coderro");
                    codcliente          =c.getString("codcliente");
                    saldopendente       =c.getString("saldopendente");
                    saldoefetivado      =c.getString("saldoefetivado");
                    apelido             =c.getString("apelido");
                    nome                =c.getString("nome");
                    notificacao         =c.getString("notificacao");
                    foto         =c.getString("foto");


                    mensagem = URLDecoder.decode(mensagem, "UTF-8");
                    nome = URLDecoder.decode(nome, "UTF-8");
                    apelido = URLDecoder.decode(apelido, "UTF-8");
                }
            }catch (Exception e){
                Log.i("Erro", "JSON: "+e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {



            progress.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction t = fragmentManager.beginTransaction();


                    EntrarFragment f = new EntrarFragment();

                    Log.i("coderro"," "+coderro);
                    if(coderro.equals("1")){
                        t.replace(R.id.container,f,"Entrar");
                        t.commit();
                    }else{

                        //Intent it = new Intent(getActivity(), AnuncioPager.class);
                        //it.putExtra("mensagem", mensagem);
                        //it.putExtra("codcliente", codcliente);
                        //it.putExtra("saldopendente", saldopendente);
                        //it.putExtra("saldoefetivado", saldoefetivado);
                        //it.putExtra("apelido", apelido);
                        //it.putExtra("nome", nome);
                        //it.putExtra("cpf", cpf);
                        //it.putExtra("notificacao", notificacao);
                        //it.putExtra("foto", foto);
                        //startActivity(it);

                        getActivity().finish();
                    }
                }
            }, SPLASH_TIME_OUT);

        }


    }
}
