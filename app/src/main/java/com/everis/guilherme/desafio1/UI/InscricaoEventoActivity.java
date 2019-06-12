package com.everis.guilherme.desafio1.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.everis.guilherme.desafio1.DAO.ParticipanteDAO;
import com.everis.guilherme.desafio1.DAO.RegistroDAO;
import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.Domain.Participante;
import com.everis.guilherme.desafio1.Domain.Registro;
import com.everis.guilherme.desafio1.Mask.Mask;
import com.everis.guilherme.desafio1.R;

public class InscricaoEventoActivity extends AppCompatActivity {
    TextView txtNomeEvento;
    TextView txtCidadeEData;
    TextView txtLocalEHorario;
    TextView qntdVagas;
    EditText edtNome;
    EditText edtEmail;
    EditText edtTelefone;
    Switch swConhece;
    Button btnCadastrar;
    long idUsuarioAtivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscricao_evento);

        Bundle extras = getIntent().getExtras();
        idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
        Evento evento = (Evento) extras.get("eventoSelec");

        txtNomeEvento = findViewById(R.id.txtIENomeEvento);
        txtCidadeEData = findViewById(R.id.txtIECidadeEData);
        txtLocalEHorario = findViewById(R.id.txtIELocalEHorario);
        qntdVagas = findViewById(R.id.txtIEQtdVagas);

        String cidadeEData = evento.getCidade() + " - " + evento.getData();
        String localEHorario = evento.getLocal() + " - " + evento.getHorario();
        String descVagas = "Total de vagas: " + evento.getVagas();

        txtNomeEvento.setText(evento.getNome());
        txtCidadeEData.setText(cidadeEData);
        txtLocalEHorario.setText(localEHorario);
        qntdVagas.setText(descVagas);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        final Evento evento = (Evento) intent.getSerializableExtra("eventoSelec");
        idUsuarioAtivo = (long) intent.getSerializableExtra("idUsuarioAtivo");

        final ParticipanteDAO participanteDAO = new ParticipanteDAO(getBaseContext());
        final RegistroDAO registroDAO = new RegistroDAO(getBaseContext());

        edtNome = findViewById(R.id.edtIENome);
        edtEmail = findViewById(R.id.edtIEEmail);
        edtTelefone = findViewById(R.id.edtIETelefone);
        swConhece = findViewById(R.id.swIEConhecimento);
        edtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", edtTelefone));

        btnCadastrar = findViewById(R.id.btnIECadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edtNome.getText().toString();
                String email = edtEmail.getText().toString();
                String telefone = edtTelefone.getText().toString();
                Boolean conhecimento = swConhece.isChecked();

                Participante participante = new Participante(nome, email, telefone, conhecimento, idUsuarioAtivo);

                if(!participanteDAO.verificarSeParticipanteExiste(participante.getEmail())){
                    participanteDAO.salvar(participante);
                }

                Registro registro = new Registro(evento.getId(), participanteDAO.buscarIdPorEmail(email));
                if(registroDAO.salvar(registro)){
                    Toast.makeText(getApplicationContext(), "PARTICIPANTE REGISTRADO!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "PARTICIPANTE NÃO FOI REGISTRADO!", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(InscricaoEventoActivity.this, ListActivity.class);
                intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                startActivity(intent);
            }
        });
    }
}
