package com.example.renan.cliente.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.renan.cliente.R;
import com.example.renan.cliente.Util.CheckNetworkConnection;
import com.example.renan.cliente.Util.JSONfunctions;
import com.example.renan.cliente.Util.Mask;
import com.example.renan.cliente.Util.TratamentoImagem;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Hermanos 04 on 10/11/2017.
 */

public class LogarFragment extends BaseFragment {
    String cpf, mensagem ,coderro, senha, chip, imei, cpfsenha, CAMPOS_LOGIN = "LOGIN", notificacao, foto;

    Button b;
    ProgressBar progressEntrar;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ImageView back_intro,image_topo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logar,container,false);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        progressEntrar = (ProgressBar) view.findViewById(R.id.progressEntrar);

        TratamentoImagem carregar = new TratamentoImagem();

        back_intro = (ImageView) view.findViewById(R.id.back_intro);
        image_topo = (ImageView) view.findViewById(R.id.imageView3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_cad);
            if(toolbar != null){

                toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();

                    }
                });
                toolbar.inflateMenu(R.menu.main1);

            }
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            back_intro.setImageBitmap(carregar.carregarBitmap(back_intro.getMaxHeight(),back_intro.getMaxWidth(),R.drawable.intro_logar, getResources()));
            image_topo.setImageBitmap(carregar.carregarBitmap(image_topo.getMaxHeight(),image_topo.getMaxWidth(),R.drawable.teste_logo2, getResources()));
        }else{
            toast("Smartphone não consegue abrir imagens nesta versão de celular");
        }


        try {
            back_intro.clearAnimation();
            back_intro.destroyDrawingCache();
            image_topo.clearAnimation();
            image_topo.destroyDrawingCache();
            Log.i("destroindo imagem","Tela Verificacao");
        } catch (Exception e) {
            Log.i("Erro imagem","Tela Verificacao" + e);
        }
        SpannableString content = new SpannableString("Esqueci minha senha");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                @SuppressLint("RestrictedApi") LayoutInflater inflater = getLayoutInflater(Bundle.EMPTY);
                View alertLayout = inflater.inflate(R.layout.esqueci_campos_dialog, null);
                View alertTitle = inflater.inflate(R.layout.alerta_mensagem_titulo, null);
                RelativeLayout fundo_alerta =(RelativeLayout) alertTitle.findViewById(R.id.fundo_alerta);
                fundo_alerta.setBackgroundResource(R.color.alert_esqueci);
                TextView text_titulo = (TextView) alertTitle.findViewById(R.id.text_titulo);
                text_titulo.setText(R.string.alerta);
                ImageView  img_titulo =(ImageView) alertTitle.findViewById(R.id.img_titulo);
                img_titulo.setImageResource(R.drawable.ic_alterarsenha);

                final EditText rec_celular = (EditText) alertLayout.findViewById(R.id.rec_celular);
                rec_celular.addTextChangedListener(Mask.insert("(##)#####-####", rec_celular));

                final EditText rec_cnpj = (EditText) alertLayout.findViewById(R.id.rec_cnpj);
                rec_cnpj.addTextChangedListener(Mask.insert("###.###.###-##", rec_cnpj));

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setCustomTitle(alertTitle);
                alert.setView(alertLayout);
                alert.setCancelable(true);
                alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setPositiveButton(R.string.enviar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        chip = rec_celular.getText().toString();
                        cpfsenha = rec_cnpj.getText().toString();
                        imei = "1234";

                        //Envia para Json
                        if(CheckNetworkConnection.isConnectionAvailable(getContext())){
                            new esqueciSenhaCliente().execute();
                        }else{
                            snack(getView(),"Verifique sua conexão á Internet");
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });


        final EditText campo_cpf = (EditText) view.findViewById(R.id.campo_cpf);
        final EditText campo_senha = (EditText) view.findViewById(R.id.campo_senha);

        campo_cpf.addTextChangedListener(Mask.insert("###.###.###-##", campo_cpf));



        b = (Button) view.findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cpf = campo_cpf.getText().toString();

                senha = campo_senha.getText().toString();

                if(cpf.equals("") || senha.equals("")){

                    campo_cpf.setFocusable(true);
                    campo_cpf.setError("Verifique CPF");

                    campo_senha.setFocusable(true);
                    campo_senha.setError("Verifique Senha");

                }else{
                    if(CheckNetworkConnection.isConnectionAvailable(getContext())) {
                        new Login().execute();
                    }else{
                        snack(getView(),"Verifique sua conexão á Internet");
                    }

                }

            }
        });
        return view;
    }

    //JSON enviado para cada alteração no botão
    private class Login extends AsyncTask<Void,Void,Void> {

        String codcliente,saldopendente,saldoefetivado,apelido,nome;

        @Override
        protected void onPreExecute() {
            progressEntrar.setVisibility(View.VISIBLE);
            b.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                senha = URLEncoder.encode(senha,"UTF-8");


                Log.w("aux","http://siga.grupohermanos.com.br:8056/valuetravel/webservice/acessocliente.jsp?cpf="+cpf+"&senha="+senha+"&token="+ FirebaseInstanceId.getInstance().getToken());

                String url = JSONfunctions.getJSONfromURL("http://siga.grupohermanos.com.br:8056/valuetravel/webservice/acessocliente.jsp?cpf="+cpf+"&senha="+senha+"&token="+ FirebaseInstanceId.getInstance().getToken());

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

                        mensagem = URLDecoder.decode(mensagem,"UTF-8");
                        apelido = URLDecoder.decode(apelido,"UTF-8");
                        nome = URLDecoder.decode(nome,"UTF-8");
                    }
                } catch (final JSONException e) {
                    Log.e("aux", "Erro na leitura do campo: " + e.getMessage());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            verificaDados(mensagem,codcliente,saldopendente,saldoefetivado,apelido,nome,notificacao, foto);


        }
    }

    public void verificaDados(String mensagem, String codcliente,String saldopendente , String saldoefetivado, String apelido, String nome, String notificacao, String foto){
        try{
            String trata = URLDecoder.decode(mensagem, "UTF-8");
//
            if(coderro.equals("1")){
//
                @SuppressLint("RestrictedApi") LayoutInflater inflater = getLayoutInflater(Bundle.EMPTY);
                View alertTitle = inflater.inflate(R.layout.alerta_mensagem_titulo, null);
                RelativeLayout fundo_alerta =(RelativeLayout) alertTitle.findViewById(R.id.fundo_alerta);
                fundo_alerta.setBackgroundResource(R.color.vermelho_alerta);
                TextView text_titulo = (TextView) alertTitle.findViewById(R.id.text_titulo);
                text_titulo.setText(R.string.alerta);
                ImageView  img_titulo =(ImageView) alertTitle.findViewById(R.id.img_titulo);
                img_titulo.setImageResource(R.drawable.ic_errado);
//
                AlertDialog.Builder alerta;
                alerta = new AlertDialog.Builder(getActivity());
                alerta.setCustomTitle(alertTitle);
                alerta.setCancelable(true);
                alerta.setMessage(trata);
                alerta.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.setVisibility(View.VISIBLE);
                        progressEntrar.setVisibility(View.GONE);
                    }
                });
                AlertDialog meualerta = alerta.create();
                meualerta.show();
//
            }else{

//
                try {
                    back_intro.clearAnimation();
                    back_intro.destroyDrawingCache();
                    image_topo.clearAnimation();
                    image_topo.destroyDrawingCache();
                    Log.i("destroindo imagem","Tela Verificacao");
                } catch (Exception e) {
                    Log.i("Erro imagem","Tela Verificacao" + e);
                }

                try {
                    //Intent it = new Intent(getActivity(),AnuncioPager.class);
//
                    //it.putExtra("coderro","0");
                    //it.putExtra("codcliente",codcliente);
                    //it.putExtra("saldopendente",saldopendente);
                    //it.putExtra("saldoefetivado",saldoefetivado);
                    //it.putExtra("apelido",apelido);
                    //it.putExtra("nome",nome);
                    //it.putExtra("cpf",cpf);
                    //it.putExtra("senha",senha);
                    //it.putExtra("notificacao",notificacao);
                    //it.putExtra("foto",foto);
//
                    //startActivity(it);
                } catch (Exception e) {
                    Log.i("Erro imagem","Tela Verificacao" + e);
                }

                getActivity().finish();

            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Log.e("AUX","Erro no tratamento da mensagem:"+ e);
        }
//
    }

    private class esqueciSenhaCliente extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            Log.d("aux","http://siga.grupohermanos.com.br:8056/valuetravel/webservice/esquecisenhacliente.jsp?chip="
                    + chip + "&cpf=" + cpfsenha +"&imei="+ imei);

            String url = JSONfunctions.getJSONfromURL("http://siga.grupohermanos.com.br:8056/valuetravel/webservice/esquecisenhacliente.jsp?chip="
                    + chip + "&cpf=" + cpfsenha +"&imei="+ imei);

            try {
                jsonObject = new JSONObject(url);
            } catch (Throwable t) {
                Log.e("oi", "Má formação no JSON: " + url);
            }
            try {
                jsonArray = jsonObject.getJSONArray("dados");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    mensagem = c.getString("mensagem");
                    coderro = c.getString("coderro");
                }
            } catch (final JSONException e) {
                Log.e("oi", "Erro na leitura do campo: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            validacaoSenha(mensagem);
        }
    }

    public void validacaoSenha(String mensagem){
        try{
            String msgTratada = URLDecoder.decode(mensagem, "UTF-8");

            if(coderro.equals("1")){

                @SuppressLint("RestrictedApi") LayoutInflater inflater = getLayoutInflater(Bundle.EMPTY);
                View alertTitle = inflater.inflate(R.layout.alerta_mensagem_titulo, null);
                RelativeLayout fundo_alerta =(RelativeLayout) alertTitle.findViewById(R.id.fundo_alerta);
                fundo_alerta.setBackgroundResource(R.color.vermelho_alerta);
                TextView text_titulo = (TextView) alertTitle.findViewById(R.id.text_titulo);
                text_titulo.setText(R.string.alerta);
                ImageView  img_titulo =(ImageView) alertTitle.findViewById(R.id.img_titulo);
                img_titulo.setImageResource(R.drawable.ic_errado);

                //Mensagem de Erro ao Gerar senha
                AlertDialog.Builder alerta;
                alerta = new AlertDialog.Builder(getActivity());
                alerta.setCustomTitle(alertTitle);
                alerta.setCancelable(true);
                alerta.setMessage(msgTratada);
                alerta.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog meualerta = alerta.create();
                meualerta.show();
            }else{
                @SuppressLint("RestrictedApi") LayoutInflater inflater = getLayoutInflater(Bundle.EMPTY);
                View alertTitle = inflater.inflate(R.layout.alerta_mensagem_titulo, null);
                RelativeLayout fundo_alerta =(RelativeLayout) alertTitle.findViewById(R.id.fundo_alerta);
                fundo_alerta.setBackgroundResource(R.color.verde_alerta);
                TextView text_titulo = (TextView) alertTitle.findViewById(R.id.text_titulo);
                text_titulo.setText(R.string.alerta);
                ImageView  img_titulo =(ImageView) alertTitle.findViewById(R.id.img_titulo);
                img_titulo.setImageResource(R.drawable.ic_certo);

                //Mensagem de Sucesso ao Gerar senha
                AlertDialog.Builder alerta;
                alerta = new AlertDialog.Builder(getActivity());
                alerta.setCustomTitle(alertTitle);
                alerta.setCancelable(false);
                alerta.setMessage(msgTratada);
                alerta.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog meualerta = alerta.create();
                meualerta.show();
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
}
