package com.everis.guilherme.desafio1.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.everis.guilherme.desafio1.DAO.RegistroDAO;
import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.Domain.Participante;
import com.everis.guilherme.desafio1.R;

import java.util.ArrayList;
import java.util.Collections;

public class ListarParticipantesActivity extends AppCompatActivity {
    TextView txtNomeEvento;
    TextView txtCidadeEData;
    TextView txtLocalEHorario;
    TextView txtVagas;
    long idUsuarioAtivo;
    private ListView listViewParticipantes;
    RegistroDAO registroDAO;
    Evento eventoSelec;
    Button btnVoltar;
    Button btnEditar;
    AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_participantes);

        registroDAO = new RegistroDAO(getBaseContext());
        Bundle extras = getIntent().getExtras();
        idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
        eventoSelec = (Evento) extras.get("eventoSelec");

        btnVoltar = findViewById(R.id.btnLPVoltar);
        btnEditar = findViewById(R.id.btnLPEditar);
        listViewParticipantes = findViewById(R.id.lvLPParticipantes);
        txtNomeEvento = findViewById(R.id.txtLPNomeEvento);
        txtCidadeEData = findViewById(R.id.txtLPCidadeEData);
        txtLocalEHorario = findViewById(R.id.txtLPLocalEHorario);
        txtVagas = findViewById(R.id.txtLPVagas);

        String cidadeEData = eventoSelec.getCidade() + " - " + eventoSelec.getData();
        String localEHorario = eventoSelec.getLocal() + " - " + eventoSelec.getHorario();
        String descVagas = "Vagas: " + registroDAO.contParticipantesPorIdEvento(eventoSelec.getId()) + "/" + eventoSelec.getVagas();

        txtNomeEvento.setText(eventoSelec.getNome());
        txtCidadeEData.setText(cidadeEData);
        txtLocalEHorario.setText(localEHorario);
        txtVagas.setText(descVagas);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarParticipantesActivity.this, ListActivity.class);
                intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                startActivity(intent);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarParticipantesActivity.this, EditarEventoActivity.class);
                intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                intent.putExtra("eventoSelec", eventoSelec);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
        eventoSelec = (Evento) extras.get("eventoSelec");

        registroDAO = new RegistroDAO(getBaseContext());
        ArrayList<Participante> listaDeParticipantes = registroDAO.listarParticipantesDoEvento(eventoSelec.getId());
        Collections.sort(listaDeParticipantes);

        if(!listaDeParticipantes.isEmpty()){
            ArrayAdapter adapter = new ParticipanteAdapter(this, listaDeParticipantes);
            listViewParticipantes.setAdapter(adapter);
            listViewParticipantes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Participante participanteSelecionado = (Participante) listViewParticipantes.getItemAtPosition(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListarParticipantesActivity.this);

                    builder.setTitle("Deletar");
                    builder.setMessage("Deletar este usuário?");

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            registroDAO.inativarParticipante(registroDAO.buscarPorIdParticipante(participanteSelecionado.getId()));
                            Toast.makeText(getApplicationContext(), "Participante deletado!", Toast.LENGTH_SHORT).show();

                            Intent intent1 = new Intent(ListarParticipantesActivity.this, ListarParticipantesActivity.class);
                            intent1.putExtra("eventoSelec", eventoSelec);
                            intent1.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                            startActivity(intent1);
                        }
                    });
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    alerta = builder.create();
                    alerta.show();
                    return true;
                }
            });
        } else {
            listViewParticipantes.setAdapter(null);
            Toast.makeText(getApplicationContext(), "Não há participantes cadastrados neste evento!", Toast.LENGTH_SHORT).show();
        }
    }
}
