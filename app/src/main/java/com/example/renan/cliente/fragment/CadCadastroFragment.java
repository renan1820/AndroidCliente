package com.example.renan.cliente.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.renan.cliente.R;
import com.example.renan.cliente.Util.DataValidador;
import com.example.renan.cliente.Util.Mask;
import com.example.renan.cliente.Util.TratamentoImagem;
import com.example.renan.cliente.Util.ValidaCPF;
import com.example.renan.cliente.domain.Ids;

/**
 * Created by Hermanos 04 on 10/11/2017.
 */

public class CadCadastroFragment extends BaseFragment {
    private Spinner spinner_sexo;
    private EditText campo_nome, campo_apelido, campo_cpf, campo_email, campo_celular,campo_nasc;
    private String sexo, nome, apelido, cpf, email, nasc, celular;
    private Button enviar;
    private ImageView img_cad;

    private String pattern = "dd/MM/yyyy";
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.[a-z]{2,4}$";
    private String expression = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String textoPattern = "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ðZ0-9._-]";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cad_dados,container,false);

        findViewById(view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sexo.setAdapter(adapter);

        campo_cpf.addTextChangedListener(Mask.insert("###.###.###-##", campo_cpf));
        campo_celular.addTextChangedListener(Mask.insert("(##)#####-####", campo_celular));
        campo_nasc.addTextChangedListener(Mask.insert("##/##/####", campo_nasc));


        TratamentoImagem imagem = new TratamentoImagem();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            img_cad.setImageBitmap(imagem.carregarBitmap(img_cad.getMaxHeight(),img_cad.getMaxWidth(),R.drawable.fundo_cadastro, getResources()));
        }else{
            toast("Não é posivel carregar as imagens, devido a versão de seu android.");
        }

        spinner_sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    if(adapterView.getItemAtPosition(i).toString().equals("Masculino")){
                        sexo = "M";
                    }else{
                        sexo = "F";
                    }
                }else{
                    sexo = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(campo_email.getText().toString().trim().matches(emailPattern) || campo_email.getText().toString().trim().matches(expression)){

                    celular = campo_celular.getText().toString();
                    nome = campo_nome.getText().toString();
                    apelido = campo_apelido.getText().toString();
                    cpf = campo_cpf.getText().toString();
                    nasc = campo_nasc.getText().toString();
                    email = campo_email.getText().toString();

                    String cpfV = cpf;

                    if(sexo.equals("")){
                        snack(view,"Escolha seu sexo");

                    }else{
                        Log.i("celular","celular.length()"+celular.length());



                        if (DataValidador.isValidBirthday(nasc)) {

                            if(celular.length()>2 && nome.length()>2 && apelido.length()>2 && cpf.length()>2){

                                if(ValidaCPF.isCPF(Mask.unmask(cpfV))){

                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction t = fragmentManager.beginTransaction();
                                    CadEnderecoFragment f = new CadEnderecoFragment();
                                    Bundle args = new Bundle();

                                    args.putString("celular",celular);
                                    args.putString("nome",nome);
                                    args.putString("apelido",apelido);
                                    args.putString("cpf",cpf);
                                    args.putString("nasc",nasc);
                                    args.putString("email",email);
                                    args.putString("sexo",sexo);

                                    f.setArguments(args);
                                    t.addToBackStack(Ids.TELA_ENTRAR);
                                    t.replace(R.id.container,f,Ids.TELA_CADASTRO_ENDERECO);
                                    t.commit();
                                }else{
                                    campo_cpf.setError("CPF inválido");
                                }
                            }else{
                                if(celular == null || celular.equals("")){
                                    campo_celular.setError("Campo obrigatório");
                                    campo_celular.setFocusable(true);
                                }
                                if(nome == null || nome.equals("")){
                                    campo_nome.setError("Campo obrigatório");
                                    campo_nome.setFocusable(true);
                                }
                                if(apelido == null || apelido.equals("") ){
                                    campo_apelido.setError("Campo obrigatório");
                                    campo_apelido.setFocusable(true);
                                }
                                if(cpf == null || cpf.equals("")){
                                    campo_cpf.setError("Campo obrigatório");
                                    campo_cpf.setFocusable(true);
                                }
                                if(nasc == null || nasc.equals("")){
                                    campo_nasc.setError("Campo obrigatório");
                                    campo_nasc.setFocusable(true);
                                }
                                if(celular.length()<2 || nome.length()<2 || apelido.length()<2 || cpf.length()<2 || nasc.length()<2){
                                    Toast.makeText(getContext(), "Campo muito curto", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }else{
                            campo_nasc.setError("Data Inválida");
                        }
                    }


                }else{
                    campo_email.setFocusable(true);
                    campo_email.setError("Verifique seu e-mail");
                }

            }
        });
        return view;
    }

    private void findViewById(View view){
        spinner_sexo = (Spinner) view.findViewById(R.id.spinner_sexo);
        campo_nome = (EditText)view.findViewById(R.id.campo_nome);
        campo_apelido = (EditText)view.findViewById(R.id.campo_apelido);
        campo_cpf = (EditText)view.findViewById(R.id.campo_cpf);
        campo_email = (EditText)view.findViewById(R.id.campo_email);
        campo_nasc = (EditText)view.findViewById(R.id.campo_nasc);
        campo_celular = (EditText)view.findViewById(R.id.campo_celular);
        enviar = (Button)view.findViewById(R.id.enviar);
        img_cad = (ImageView) view.findViewById(R.id.img_cad);

    }
}
