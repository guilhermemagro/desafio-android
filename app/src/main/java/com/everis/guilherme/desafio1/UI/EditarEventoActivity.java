package com.everis.guilherme.desafio1.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.everis.guilherme.desafio1.DAO.EventoDAO;
import com.everis.guilherme.desafio1.DAO.RegistroDAO;
import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.R;

public class EditarEventoActivity extends AppCompatActivity {

    TextView txtNomeEvento;
    TextView txtCidadeEData;
    TextView txtLocalEHorario;
    TextView txtQtdVagas;
    EditText editarNome;
    EditText editarCidade;
    EditText editarLocal;
    EditText editarData;
    EditText editarHora;
    EditText editarVagas;
    Button btnCancelar;
    Button btnEditar;
    Button btnDeletar;
    long idUsuarioAtivo;
    RegistroDAO registroDAO;
    EventoDAO eventoDAO;
    Evento eventoSelec;
    AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);

        txtNomeEvento = (TextView) findViewById(R.id.txtEENomeEvento);
        txtCidadeEData = (TextView) findViewById(R.id.txtEECidadeEData);
        txtLocalEHorario = (TextView) findViewById(R.id.txtEELocalEHorario);
        txtQtdVagas = (TextView) findViewById(R.id.txtEEQtdVagas);
        editarNome = (EditText) findViewById(R.id.edtEEEvento);
        editarCidade = (EditText) findViewById(R.id.edtEECidade);
        editarLocal = (EditText) findViewById(R.id.edtEELocal);
        editarData = (EditText) findViewById(R.id.edtEEData);
        editarHora = (EditText) findViewById(R.id.edtEEHora);
        editarVagas = (EditText) findViewById(R.id.edtEEVagas);
        btnCancelar = (Button) findViewById(R.id.btnEECancelar);
        btnEditar = (Button) findViewById(R.id.btnEEEditar);
        btnDeletar = (Button) findViewById(R.id.btnEEDeletarEvento);

        eventoDAO = new EventoDAO(getBaseContext());
        registroDAO = new RegistroDAO(getBaseContext());
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
            eventoSelec = (Evento) extras.get("eventoSelec");
        }

        editarNome.setText(eventoSelec.getNome());
        editarCidade.setText(eventoSelec.getCidade());
        editarLocal.setText(eventoSelec.getLocal());
        editarData.setText(eventoSelec.getData());
        editarHora.setText(eventoSelec.getHorario());
        String vagasConcat = eventoSelec.getVagas() + "";
        editarVagas.setText(vagasConcat);

        String strCidadeEData = eventoSelec.getCidade() + " - " + eventoSelec.getData();
        String strLocalEHorario = eventoSelec.getLocal() + " - " + eventoSelec.getHorario();
        String strQtdVagas = getBaseContext().getString(R.string.total_de_vagas) + ": " + eventoSelec.getVagas();

        txtNomeEvento.setText(eventoSelec.getNome());
        txtCidadeEData.setText(strCidadeEData);
        txtLocalEHorario.setText(strLocalEHorario);
        txtQtdVagas.setText(strQtdVagas);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelar = new Intent(EditarEventoActivity.this, ListarParticipantesActivity.class);
                intentCancelar.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                intentCancelar.putExtra("eventoSelec", eventoSelec);
                startActivity(intentCancelar);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Evento eventoAtual = new Evento();
                eventoAtual.setId(eventoSelec.getId());
                eventoAtual.setNome(editarNome.getText().toString());
                eventoAtual.setLocal(editarLocal.getText().toString());
                eventoAtual.setCidade(editarCidade.getText().toString());
                eventoAtual.setData(editarData.getText().toString());
                eventoAtual.setHorario(editarHora.getText().toString());
                eventoAtual.setVagas(Integer.parseInt(editarVagas.getText().toString()));
                eventoAtual.setImagem(R.drawable.logo_everis);   // HARDCODED!!!!
                eventoAtual.setIdCriador(idUsuarioAtivo);

                EventoDAO eventoDAO = new EventoDAO(getBaseContext());
                if(eventoDAO.atualizarEvento(eventoAtual)){
                    Toast.makeText(getApplicationContext(), "Evento editado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Evento não foi editado!", Toast.LENGTH_SHORT).show();
                }

                Intent intentEditar = new Intent(EditarEventoActivity.this, ListarParticipantesActivity.class);
                intentEditar.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                intentEditar.putExtra("eventoSelec", eventoAtual);
                startActivity(intentEditar);
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarEventoActivity.this);

                builder.setTitle("Deletar");
                builder.setMessage("Deletar este evento?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        eventoDAO.inativarEvento(eventoSelec);
                        Toast.makeText(getApplicationContext(), "Evento deletado!", Toast.LENGTH_SHORT).show();
                        Intent intentDel = new Intent(EditarEventoActivity.this, ListActivity.class);
                        intentDel.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                        startActivity(intentDel);
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alerta = builder.create();
                alerta.show();
            }
        });
    }
}
