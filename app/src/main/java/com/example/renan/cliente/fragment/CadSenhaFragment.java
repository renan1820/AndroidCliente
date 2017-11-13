package com.example.renan.cliente.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renan.cliente.R;
import com.example.renan.cliente.Util.CheckNetworkConnection;
import com.example.renan.cliente.Util.JSONfunctions;
import com.example.renan.cliente.Util.TratamentoImagem;
import com.example.renan.cliente.activity.TermoActivity;
import com.example.renan.cliente.domain.Ids;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermanos 04 on 10/11/2017.
 */

public class CadSenhaFragment extends BaseFragment {

    private List<String> estados_spinner = new ArrayList<>();
    private List<String> cidades_spinner = new ArrayList<>();
    private Spinner spinner1, spinner2;
    private String uf, cidade , endereco, numero, complemento, bairro, cep, nasc, sexo, cidadeEnd,ufEnd, coderro ,mensagem, nome, apelido
            ,cpf,email,celular, senha , confsenha , URL = "http://siga.grupohermanos.com.br:8056/valuetravel/webservice/termoapp.jsp";
    private ArrayAdapter<String> adapter2;
    private Button concluir;
    private EditText campo_senha, campo_confsenha;
    private CheckBox termo_check;
    private TextView textTermo;
    private ImageView img_cad;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            Log.i(">>"," Recebendo os dados anteriores "+getArguments().toString());

            sexo = getArguments().getString("sexo");
            celular = getArguments().getString("celular");
            cidadeEnd = getArguments().getString("cidadeEnd");
            ufEnd = getArguments().getString("ufEnd");
            nome = getArguments().getString("nome");
            apelido = getArguments().getString("apelido");
            cpf = getArguments().getString("cpf");
            email = getArguments().getString("email");
            nasc = getArguments().getString("nasc");
            endereco = getArguments().getString("endereco");
            numero = getArguments().getString("numero");
            complemento = getArguments().getString("complemento");
            bairro = getArguments().getString("bairro");
            cep = getArguments().getString("cep");



        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cad_senha,container,false);
        findViewById(view);

        SpannableString content = new SpannableString("termo de uso");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        TratamentoImagem imagem = new TratamentoImagem();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            img_cad.setImageBitmap(imagem.carregarBitmap(img_cad.getMaxHeight(),img_cad.getMaxWidth(),R.drawable.fundo_senha, getResources()));
        }else{
            toast("Erro no carregamento das imagens, Android com versão inferior a suportada.");
        }


        textTermo.setText(content);

        textTermo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(getActivity(), TermoActivity.class);
                it.putExtra("url", URL);
                startActivity(it);
            }
        });


        estados_spinner.add("Escolha o Estado");
        cidades_spinner.add("Escolha a Cidade");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estados_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cidades_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        if(CheckNetworkConnection.isConnectionAvailable(getContext())){
            new listarUF().execute();
        }else{

            Toast.makeText(getContext(), "Sem conexão, tentando efetuar conexão novamente, Aguarde...", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(CheckNetworkConnection.isConnectionAvailable(getContext())){
                        new listarUF().execute();
                    }else{
                        Toast.makeText(getContext(), "Impossível estabelecer conexão tente novamente mais tarde", Toast.LENGTH_SHORT).show();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction t = fragmentManager.beginTransaction();
                        EntrarFragment f = new EntrarFragment();
                        t.replace(R.id.container,f,Ids.TELA_CADASTRO_DADOS);
                        t.addToBackStack(Ids.TELA_ENTRAR);
                        t.commit();
                    }

                }
            }, 5000);
        }

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getAdapter().getItem(i);


                if (i != 0) {

                    uf = s;
                    Log.i("uf",": "+s);

                    new listarCidade().execute(s);
                }else{
                    spinner2.setSelection(0);
                    uf = s;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String s = (String) adapterView.getAdapter().getItem(i);
                //Toast.makeText(getContext(),""+i , Toast.LENGTH_SHORT).show();

                if (i != 0) {

                    cidade  = s;
                    Log.i("cidade",": "+s);


                }else{
                    cidade = s;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senha  = campo_senha.getText().toString();
                confsenha = campo_confsenha.getText().toString();

                if(senha.length()>3 && confsenha.length()>3){
                    if(senha.equals(confsenha)){
                        if (spinner1.getSelectedItemPosition() != 0 && spinner2.getSelectedItemId() != 0) {
                            if(termo_check.isChecked()){
                                if(CheckNetworkConnection.isConnectionAvailable(getContext())){
                                    new CriarCadastro().execute();
                                }else{
                                    snack(getView(),"Verifique sua conexão á Internet");
                                }
                            }else{
                                Toast.makeText(getContext(), "Aceite os termos de uso", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "Selecione sua Região", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        snack(getView(),"Senhas não são iguais");
                    }
                }else{
                    Toast.makeText(getContext(), "Senha muito curta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void findViewById(View view){
        textTermo = (TextView) view.findViewById(R.id.textTermo);
        spinner1 = (Spinner) view.findViewById(R.id.spinner);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        campo_senha = (EditText) view.findViewById(R.id.campo_senha);
        campo_confsenha = (EditText) view.findViewById(R.id.campo_confsenha);
        concluir = (Button) view.findViewById(R.id.concluir);
        termo_check = (CheckBox) view.findViewById(R.id.termo_check);
        img_cad = (ImageView) view.findViewById(R.id.img_cad);


    }

    private class listarUF extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject;
        JSONArray jsonarray;
        String url;

        @Override
        protected Void doInBackground(Void... arg0) {
            url = JSONfunctions.getJSONfromURL("http://siga.grupohermanos.com.br:8056/valuetravel/webservice/pegarestado.jsp");
            try {
                jsonObject = new JSONObject(url);
                Log.e("aux", "jsonObject" + jsonObject);
            } catch (Throwable t) {
                Log.e("aux", "Má formação no JSON: " + url);
            }
            estados_spinner.clear();
            estados_spinner.add("Escolha o Estado");
            try {
                jsonarray = jsonObject.getJSONArray("dados");
                Log.e("aux", "JsonResult: " + jsonarray);
                for (int i = 0; i < jsonarray.length(); i++) {
                    //Log.e("aux", "JsonResult: "+ jsonarray.get(i).toString());
                    String msgTratada = URLDecoder.decode(jsonarray.get(i).toString(), "UTF-8");
                    estados_spinner.add(msgTratada);

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }

    private class listarCidade extends AsyncTask<String, Void, Void> {
        private JSONObject jsonObject;
        private JSONArray jsonarray;

        @Override
        protected Void doInBackground(String... arg0) {
            String url = JSONfunctions.getJSONfromURL("http://siga.grupohermanos.com.br:8056/valuetravel/webservice/pegarcidade.jsp?uf=" + uf);
            Log.e("aux", "http://siga.grupohermanos.com.br:8056/valuetravel/webservice/pegarcidade.jsp?uf=" + uf);
            try {
                jsonObject = new JSONObject(url);
                Log.e("aux", "jsonObject" + jsonObject);
            } catch (Throwable t) {
                Log.e("aux", "Má formação no JSON: " + url);
            }
            try {
                jsonarray = jsonObject.getJSONArray("dados");
                Log.e("aux", "JsonResult: " + jsonarray);
                cidades_spinner.clear();
                cidades_spinner.add("Escolha a Cidade");
                for (int i = 0; i < jsonarray.length(); i++) {
                    //Log.e("aux", "JsonResult: "+ jsonarray.get(i).toString());
                    String msgTratada = URLDecoder.decode(jsonarray.get(i).toString(), "UTF-8");
                    cidades_spinner.add(msgTratada);
                    cidade = msgTratada;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(cidades_spinner.toArray().length > 1){

                spinner2.setSelection(adapter2.getPosition(cidades_spinner.get(0)));

                cidade = cidades_spinner.get(0);


            }else{
                spinner2.setSelection(0);
            }


        }
    }

    private class CriarCadastro extends AsyncTask<Void,Void,Void> {

        private JSONArray jsonArray;
        private JSONObject jsonObject;
        private String imei = "1234";

        @Override
        protected Void doInBackground(Void... params) {


            String cpfE, celularE, senhaE,apelidoE,nomeE,emailE,enderecoE,complementoE,bairroE,cidadeEndE,cepE,nascE,numeroE,cidadeE;

            try {

                Log.w("TODOS OS CAMPOS: ",cpf+" "+celular+" "+senha+" "+apelido+" "+nome+" "+email+" "+endereco+" "+complemento+" "+bairro+" "+cidadeEnd+" "+cep+" "+nasc+""+numero+""+cidade);

                cpfE = URLEncoder.encode(cpf,"UTF-8");
                celularE = URLEncoder.encode(celular,"UTF-8");
                senhaE = URLEncoder.encode(senha,"UTF-8");
                apelidoE = URLEncoder.encode(apelido,"UTF-8");
                nomeE = URLEncoder.encode(nome,"UTF-8");
                emailE = URLEncoder.encode(email,"UTF-8");
                enderecoE = URLEncoder.encode(endereco,"UTF-8");
                complementoE = URLEncoder.encode(complemento,"UTF-8");
                bairroE = URLEncoder.encode(bairro,"UTF-8");
                cidadeEndE = URLEncoder.encode(cidadeEnd,"UTF-8");
                cepE = URLEncoder.encode(cep,"UTF-8");
                nascE = URLEncoder.encode(nasc,"UTF-8");
                numeroE = URLEncoder.encode(numero,"UTF-8");
                cidadeE = URLEncoder.encode(cidade,"UTF-8");

                Log.w("TODOS ENCODER: ",cpf+" "+celular+" "+senha+" "+apelido+" "+nome+" "+email+" "+endereco+" "+complemento+" "+bairro+" "+cidadeEnd+" "+cep+" "+nasc+""+numero+""+cidade);
                cpf = URLEncoder.encode(cpf,"UTF-8");


                Log.w("aux","http://siga.grupohermanos.com.br:8056/valuetravel/webservice/criarlogincliente.jsp?chip="+celularE+
                        "&cpf="+cpfE+"&imei="+imei+"&token="+ FirebaseInstanceId.getInstance().getToken()+"&senha="+senhaE+"&apelido="+apelidoE+"&nome="+nomeE+
                        "&email="+emailE+"&endereco="+enderecoE+"&endnum="+numeroE+"&complemento="+complementoE+"&bairro="+bairroE+"&cidade="+cidadeEndE+"&uf="+ufEnd+
                        "&cep="+cepE+"&dtnasc="+nascE+"&sexo="+sexo+"&uf_programa="+uf+"&cidade_programa="+cidadeE);

                String url = JSONfunctions.getJSONfromURL("http://siga.grupohermanos.com.br:8056/valuetravel/webservice/criarlogincliente.jsp?chip="+celularE+
                        "&cpf="+cpfE+"&imei="+imei+"&token="+ FirebaseInstanceId.getInstance().getToken()+"&senha="+senhaE+"&apelido="+apelidoE+"&nome="+nomeE+
                        "&email="+emailE+"&endereco="+enderecoE+"&endnum="+numeroE+"&complemento="+complementoE+"&bairro="+bairroE+"&cidade="+cidadeEndE+"&uf="+ufEnd+
                        "&cep="+cepE+"&dtnasc="+nascE+"&sexo="+sexo+"&uf_programa="+uf+"&cidade_programa="+cidadeE);
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
            verificaDados(mensagem);


        }
    }

    public void verificaDados(String mensagem){
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
                alerta.setCancelable(false);
                alerta.setMessage(trata);
                alerta.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        if(fragmentManager.findFragmentByTag(Ids.TELA_CADASTRO_SENHA) != null){
                            FragmentTransaction t = fragmentManager.beginTransaction();
                            CadCadastroFragment c = new CadCadastroFragment();
                            t.replace(R.id.container,c,Ids.TELA_CADASTRO_DADOS);
                            t.commit();
                        }
                    }
                });
                AlertDialog meualerta = alerta.create();
                meualerta.show();
//
            }else{
//
                @SuppressLint("RestrictedApi") LayoutInflater inflater = getLayoutInflater(Bundle.EMPTY);
                View alertTitle = inflater.inflate(R.layout.alerta_mensagem_titulo, null);
                RelativeLayout fundo_alerta =(RelativeLayout) alertTitle.findViewById(R.id.fundo_alerta);
                fundo_alerta.setBackgroundResource(R.color.verde_alerta);
                TextView text_titulo = (TextView) alertTitle.findViewById(R.id.text_titulo);
                text_titulo.setText(R.string.alerta);
                ImageView  img_titulo =(ImageView) alertTitle.findViewById(R.id.img_titulo);
                img_titulo.setImageResource(R.drawable.ic_certo);
//
                AlertDialog.Builder alerta;
                alerta = new AlertDialog.Builder(getActivity());
                alerta.setCustomTitle(alertTitle);
                alerta.setCancelable(false);
                alerta.setMessage(R.string.cadastro_sucesso);
                alerta.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String CAMPOS_CADASTRO = "CADASTRO";
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(CAMPOS_CADASTRO, Context.MODE_PRIVATE).edit();
                        editor.clear().apply();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction t = fragmentManager.beginTransaction();
                        LogarFragment f = new LogarFragment();
                        t.replace(R.id.container,f,Ids.TELA_LOGAR);
                        t.commit();
                    }
                });
                AlertDialog meualerta = alerta.create();
                meualerta.show();
//
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Log.e("AUX","Erro no tratamento da mensagem:"+ e);
        }
//
    }
}
