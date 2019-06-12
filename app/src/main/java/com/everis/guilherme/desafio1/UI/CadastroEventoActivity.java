package com.everis.guilherme.desafio1.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.everis.guilherme.desafio1.DAO.EventoDAO;
import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.Mask.Mask;
import com.everis.guilherme.desafio1.R;

import java.util.Calendar;

public class CadastroEventoActivity extends AppCompatActivity {

    Button btnCECadastrar;
    Button btnCECancelar;
    EditText edtCEEvento;
    EditText edtCECidade;
    EditText edtCELocal;
    TextView txtCEData;
    EditText edtCEHora;
    EditText edtCEVagas;
    private long idUsuarioAtivo;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
        }

        edtCEEvento = findViewById(R.id.edtCEEvento);
        edtCECidade = findViewById(R.id.edtCECidade);
        edtCELocal = findViewById(R.id.edtCELocal);
        txtCEData = findViewById(R.id.txtCEDataPicker);
        edtCEHora = findViewById(R.id.edtCEHora);
        edtCEVagas = findViewById(R.id.edtCEVagas);
        btnCECadastrar = findViewById(R.id.btnCECadastrar);
        btnCECancelar = findViewById(R.id.btnCECancelar);

        edtCEHora.addTextChangedListener(Mask.insert("##:##", edtCEHora));

        txtCEData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CadastroEventoActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog,
                        mDateSetListener,
                        ano, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String data = dayOfMonth + "/" + month + "/" + year;
                txtCEData.setText(data);
            }
        };

        btnCECancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroEventoActivity.this, ListActivity.class);
                intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                startActivity(intent);
            }
        });

        btnCECadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventoDAO eventoDAO = new EventoDAO(getBaseContext());
                Evento evento;
                String nomeEvento = edtCEEvento.getText().toString();
                String cidade = edtCECidade.getText().toString();
                String local = edtCELocal.getText().toString();
                String[] string = txtCEData.getText().toString().split("/");
                String dataFormatada = string[2] + "-" + string[1] + "-" + string[0];
                String hora = edtCEHora.getText().toString();
                int vagas = Integer.parseInt(edtCEVagas.getText().toString());
                int imagem = R.drawable.logo_everis; // HARDCODED!!!!

                evento = new Evento(nomeEvento, local, cidade, dataFormatada, hora, imagem, vagas, idUsuarioAtivo);

                if(eventoDAO.salvar(evento)){
                    Toast.makeText(getApplicationContext(), "EVENTO CADASTRADO!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "EVENTO NÃO FOI CADASTRADO!", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(CadastroEventoActivity.this, ListActivity.class);
                intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                startActivity(intent);
            }
        });
    }
}
