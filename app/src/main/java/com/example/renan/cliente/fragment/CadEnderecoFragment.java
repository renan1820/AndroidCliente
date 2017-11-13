package com.example.renan.cliente.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.renan.cliente.R;
import com.example.renan.cliente.Util.CheckNetworkConnection;
import com.example.renan.cliente.Util.JSONfunctions;
import com.example.renan.cliente.Util.Mask;
import com.example.renan.cliente.Util.TratamentoImagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;



/**
 * Created by Hermanos 04 on 10/11/2017.
 */

public class CadEnderecoFragment extends BaseFragment {
    private Spinner spinner_uf;
    private EditText campo_cep,campo_end, campo_num, campo_compl, campo_bairro, campo_cidade;
    private Button icon_cep, proximo;
    private String cep , coderro ,numero,complemento, endereco, bairro, cidade, uf;
    private ProgressDialog dialog;
    private String estados[]= {"Escolha seu estado","SP","MG","PB","AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO" };
    private ImageView img_cad;
    private String sexo, nome, apelido, cpf, email, nasc, celular;


    private String CAMPOS_CADASTRO = "CADASTRO";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){

            sexo = getArguments().getString("sexo");
            nome = getArguments().getString("nome");
            apelido = getArguments().getString("apelido");
            cpf = getArguments().getString("cpf");
            email = getArguments().getString("email");
            nasc = getArguments().getString("nasc");
            celular = getArguments().getString("celular");

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CAMPOS_CADASTRO, Context.MODE_PRIVATE);

            cep = sharedPreferences.getString("cep",null);
            endereco = sharedPreferences.getString("endereco",null);
            complemento = sharedPreferences.getString("complemento",null);
            bairro = sharedPreferences.getString("bairro",null);
            cidade = sharedPreferences.getString("cidade",null);

            if(cep != null){
                Log.i("cep CACHE: ",cep);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cad_endereco,container,false);
        findViewById(view);


        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item,estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_uf.setAdapter(adapter);


        TratamentoImagem imagem = new TratamentoImagem();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            img_cad.setImageBitmap(imagem.carregarBitmap(img_cad.getMaxHeight(),img_cad.getMaxWidth(),R.drawable.fundo_endereco, getResources()));
        }else{
            toast("Impossivel carregar imagens, versão de android muito antiga.");
        }

        campo_cep.addTextChangedListener(Mask.insert("#####-###", campo_cep));
        icon_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cep = campo_cep.getText().toString();
                if(CheckNetworkConnection.isConnectionAvailable(getContext())){
                    new PegarCep().execute();
                }else{
                    snack(getView(),"Verifique sua conexão á Internet");
                }
            }
        });

        regastaCampo();


        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cep = campo_cep.getText().toString();
                endereco = campo_end.getText().toString();
                numero = campo_num.getText().toString();
                complemento = campo_compl.getText().toString();
                bairro = campo_bairro.getText().toString();
                cidade = campo_cidade.getText().toString();

                if(cep.length()>6 && endereco.length()>3 && numero.length()>0 && bairro.length()>2 && cidade.length()>2){
                    Log.i("Enviando..."," "+getArguments().toString());

                    Bundle args = new Bundle();
                    args.putString("cep",cep);
                    args.putString("endereco",endereco);
                    args.putString("numero",numero);
                    args.putString("complemento",complemento);
                    args.putString("bairro",bairro);
                    args.putString("cidadeEnd",cidade);
                    args.putString("ufEnd",uf);

                    args.putString("sexo",sexo);
                    args.putString("nome",nome);
                    args.putString("apelido",apelido);
                    args.putString("cpf",cpf);
                    args.putString("email",email);
                    args.putString("nasc",nasc);
                    args.putString("celular",celular);


                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(CAMPOS_CADASTRO, getContext().MODE_PRIVATE).edit();
                    //Passa tudo em CACHE os dados recebidos do JSON
                    editor.putString("cep", cep).putString("endereco",endereco).putString("numero",numero)
                            .putString("complemento",complemento).putString("bairro",bairro).putString("cidade",cidade).apply();

                    Log.i("Enviando..."," Novo: "+getArguments().toString());

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction t = fragmentManager.beginTransaction();
                    CadSenhaFragment f = new CadSenhaFragment();

                    f.setArguments(args);
                    t.addToBackStack("CEP");
                    t.replace(R.id.container,f,"SENHA");
                    t.commit();

                }else{
                    if(cep == null || cep.equals("") || cep.length()<7){
                        campo_cep.setError("Complete o Campo");
                        campo_cep.setFocusable(true);
                    }
                    if(endereco == null || endereco.equals("")){
                        campo_end.setError("Campo obrigatório");
                        campo_end.setFocusable(true);
                    }
                    if(numero == null || numero.equals("")){
                        campo_num.setError("Campo obrigatório");
                        campo_num.setFocusable(true);
                    }
                    if(bairro == null || bairro.equals("")){
                        campo_bairro.setError("Campo obrigatório");
                        campo_bairro.setFocusable(true);
                    }
                    if(cidade == null || cidade.equals("")){
                        campo_cidade.setError("Campo obrigatório");
                        campo_cidade.setFocusable(true);
                    }
                    if(cep.length()<8 || endereco.length()<3 || numero.length()<0  || bairro.length()<2 || cidade.length()<2){
                        Toast.makeText(getContext(), "Campo muito curto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    private void findViewById(View view){
        spinner_uf = (Spinner) view.findViewById(R.id.spinner_uf);
        campo_cep = (EditText)view.findViewById(R.id.campo_cep);
        campo_end = (EditText)view.findViewById(R.id.campo_end);
        campo_num = (EditText)view.findViewById(R.id.campo_num);
        campo_compl = (EditText)view.findViewById(R.id.campo_compl);
        campo_bairro = (EditText)view.findViewById(R.id.campo_bairro);
        campo_cidade = (EditText)view.findViewById(R.id.campo_cidade);
        icon_cep = (Button)view.findViewById(R.id.icon_cep);
        proximo = (Button)view.findViewById(R.id.proximo);
        img_cad = (ImageView) view.findViewById(R.id.img_cad);

        //img_cad.setImageBitmap(imagem.carregarBitmap(img_cad.getMaxHeight(),img_cad.getMaxWidth(),R.drawable.fundo_cadastro, getResources()));

    }

    private void regastaCampo(){
        if(cep != null){
            campo_cep.setText(cep);
            Log.i("cep",cep);
        }
        if(endereco != null){
            //campo_end.setText(endereco);
        }
        if(numero != null){
            //campo_num.setText(numero);
        }
        if(complemento != null){
            //campo_compl.setText(complemento);
        }
        if (bairro != null){
            //campo_bairro.setText(bairro);
        }
        if(cidade != null){
            //campo_cidade.setText(cidade);
        }
    }
    private class PegarCep extends AsyncTask<Void, Void, Void> {
        private JSONArray jsonArray;
        private JSONObject jsonObject;
        private String mensagem;

        @Override
        protected void onPreExecute() {

            dialog = ProgressDialog.show(getContext(),"Carregando","Aguarde ...",false,true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Log.d("aux","http://siga.grupohermanos.com.br:8056/valuetravel/webservice/pegarcep.jsp?cep="
                    + cep);

            String url = JSONfunctions.getJSONfromURL("http://siga.grupohermanos.com.br:8056/valuetravel/webservice/pegarcep.jsp?cep="
                    + cep);

            try {
                jsonObject = new JSONObject(url);
            } catch (Throwable t) {
                Log.e("oi", "Má formação no JSON: " + url);
            }
            try {

                jsonArray = jsonObject.getJSONArray("dados");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    endereco = c.getString("endereco");
                    bairro = c.getString("bairro");
                    cidade = c.getString("cidade");
                    uf = c.getString("uf");
                    coderro = c.getString("coderro");
                }
            } catch (final JSONException e) {
                Log.e("oi", "Erro na leitura do campo: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            validacaoSenha();
        }
    }

    public void validacaoSenha(){
        try{

            try{
                endereco = URLDecoder.decode(endereco, "UTF-8");
                bairro = URLDecoder.decode(bairro, "UTF-8");
                cidade = URLDecoder.decode(cidade, "UTF-8");
                uf = URLDecoder.decode(uf, "UTF-8");


                if(endereco != null){
                    campo_end.setText(endereco);
                }
                if(bairro != null){
                    campo_bairro.setText(bairro);
                }
                if(cidade != null){
                    campo_cidade.setText(cidade);
                }if(uf != null){
                    for(int i = 0; i < estados.length; i++){
                        if(uf.equals(estados[i])){
                            spinner_uf.setSelection(i);
                        }
                    }
                }
                dialog.dismiss();

            }catch (NullPointerException e){

                campo_cep.setError("CEP nao Localizado");
                campo_cep.setFocusable(true);
                dialog.dismiss();
            }

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
}
