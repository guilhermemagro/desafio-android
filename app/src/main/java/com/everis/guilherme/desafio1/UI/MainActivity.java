package com.everis.guilherme.desafio1.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.everis.guilherme.desafio1.DAO.UsuarioDAO;
import com.everis.guilherme.desafio1.Domain.Usuario;
import com.everis.guilherme.desafio1.R;


public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private EditText edtMatricula;
    private EditText edtEmail;
    private EditText edtSenha;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checarCamposVazios();
        }
    };

    private void checarCamposVazios(){
        String sMatricula = (String) edtMatricula.getText().toString();
        String sEmail = (String) edtEmail.getText().toString();
        String sSenha = (String) edtSenha.getText().toString();

        if(sMatricula.equals("") || sEmail.equals("") || sSenha.equals("")){
            btnEntrar.setEnabled(false);
            btnEntrar.setAlpha(.5f);
        } else {
            btnEntrar.setEnabled(true);
            btnEntrar.setAlpha(1.0f);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEntrar = findViewById(R.id.btnEntrar);
        edtMatricula = findViewById(R.id.edtMatricula);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

        edtMatricula.addTextChangedListener(textWatcher);
        edtEmail.addTextChangedListener(textWatcher);
        edtSenha.addTextChangedListener(textWatcher);

        btnEntrar.setEnabled(false);
        btnEntrar.setAlpha(.5f);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UsuarioDAO usuarioDAO = new UsuarioDAO(getBaseContext());

                int matricula = Integer.parseInt(edtMatricula.getText().toString());
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();

                Usuario usuario = new Usuario(matricula, email, senha);

                if(!usuarioDAO.verificarSeUsuarioExiste(email)){
                    usuarioDAO.salvar(usuario);
                    Toast.makeText(getApplicationContext(), "Novo usuário cadastrado!", Toast.LENGTH_LONG).show();
                }

                usuario = usuarioDAO.buscarPorEmail(email);

                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("idUsuarioAtivo", usuario.getId());
                startActivity(intent);
            }
        });
    }
}
