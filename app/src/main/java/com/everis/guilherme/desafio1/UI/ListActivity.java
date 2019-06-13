package com.everis.guilherme.desafio1.UI;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.everis.guilherme.desafio1.DAO.EventoDAO;
import com.everis.guilherme.desafio1.DAO.UsuarioDAO;
import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.R;

import java.util.ArrayList;
import java.util.Collections;


public class ListActivity extends AppCompatActivity {

    private EventoDAO eventoDAO = null;
    private UsuarioDAO usuarioDAO = null;
    private ListView listEventos;
    private long idUsuarioAtivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        eventoDAO = new EventoDAO(getBaseContext());
        usuarioDAO = new UsuarioDAO(getBaseContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
        }

//        idUsuarioAtivo = getIntent().getLongExtra("idUsuarioAtivo", -1);

        listEventos = findViewById(R.id.lvLIEventos);

        listEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent2 = new Intent(ListActivity.this, InscricaoEventoActivity.class);

                Evento eventoSelecionado = (Evento) listEventos.getItemAtPosition(position);
                intent2.putExtra("eventoSelec", eventoSelecionado);
                intent2.putExtra("idUsuarioAtivo", idUsuarioAtivo);

                startActivity(intent2);
            }
        });

        Button btnNovoEvento = findViewById(R.id.btnLINovoEvento);

        if(!usuarioDAO.buscarPorId(idUsuarioAtivo).isAdmin()){
            btnNovoEvento.setVisibility(View.INVISIBLE);
        } else {
            listEventos.setLongClickable(true);
            listEventos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intentLong = new Intent(ListActivity.this, ListarParticipantesActivity.class);

                    Evento eventoSelecionado = (Evento) listEventos.getItemAtPosition(position);
                    intentLong.putExtra("eventoSelec", eventoSelecionado);
                    intentLong.putExtra("idUsuarioAtivo", idUsuarioAtivo);

                    startActivity(intentLong);
                    return true;
                }
            });
        }

        btnNovoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ListActivity.this, CadastroEventoActivity.class);
                intent3.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                startActivity(intent3);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Evento> listaDeEventos = eventoDAO.buscarTodos();
        Collections.sort(listaDeEventos);

        if(!listaDeEventos.isEmpty()){
            ArrayAdapter adapter = new EventoAdapter(this, listaDeEventos);
            listEventos.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Não há eventos cadastrados!", Toast.LENGTH_LONG).show();
        }
    }
}
