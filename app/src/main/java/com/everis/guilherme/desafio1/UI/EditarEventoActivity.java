package com.everis.guilherme.desafio1.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.everis.guilherme.desafio1.DAO.EventoDAO;
import com.everis.guilherme.desafio1.DAO.RegistroDAO;
import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.R;

import java.util.Calendar;

public class EditarEventoActivity extends AppCompatActivity {

    TextView txtNomeEvento;
    TextView txtCidadeEData;
    TextView txtLocalEHorario;
    TextView txtQtdVagas;
    EditText editarNome;
    EditText editarLocal;
    TextView editarData;
    TextView editarHora;
    EditText editarVagas;
    Button btnCancelar;
    Button btnEditar;
    Button btnDeletar;
    long idUsuarioAtivo;
    RegistroDAO registroDAO;
    EventoDAO eventoDAO;
    Evento eventoSelec;
    AlertDialog alerta;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Spinner spnCidade;

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
        String sEvento = editarNome.getText().toString();
        String sLocal = editarLocal.getText().toString();
        String sData = editarHora.getText().toString();
        String sHora = editarHora.getText().toString();
        String sVagas = editarVagas.getText().toString();

        if(sEvento.equals("") || sLocal.equals("") || sData.equals("") ||
                sHora.equals("") || sVagas.equals("")){
            btnEditar.setEnabled(false);
            btnEditar.setAlpha(.5f);
        } else {
            btnEditar.setEnabled(true);
            btnEditar.setAlpha(1.0f);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);

        eventoDAO = new EventoDAO(getBaseContext());
        registroDAO = new RegistroDAO(getBaseContext());
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
            eventoSelec = (Evento) extras.get("eventoSelec");
        }

        txtNomeEvento = findViewById(R.id.txtEENomeEvento);
        txtCidadeEData = findViewById(R.id.txtEECidadeEData);
        txtLocalEHorario = findViewById(R.id.txtEELocalEHorario);
        txtQtdVagas = findViewById(R.id.txtEEQtdVagas);
        editarNome = findViewById(R.id.edtEEEvento);
        editarLocal = findViewById(R.id.edtEELocal);
        editarData = findViewById(R.id.txtEEDataPicker);
        editarHora = findViewById(R.id.txtEEHoraPicker);
        editarVagas = findViewById(R.id.edtEEVagas);
        btnCancelar = findViewById(R.id.btnEECancelar);
        btnEditar = findViewById(R.id.btnEEEditar);
        btnDeletar = findViewById(R.id.btnEEDeletarEvento);

        editarNome.addTextChangedListener(textWatcher);
        editarLocal.addTextChangedListener(textWatcher);
        editarData.addTextChangedListener(textWatcher);
        editarHora.addTextChangedListener(textWatcher);
        editarVagas.addTextChangedListener(textWatcher);

        spnCidade = findViewById(R.id.spnEECidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_cidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCidade.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(eventoSelec.getCidade());
        spnCidade.setSelection(spinnerPosition);

        editarNome.setText(eventoSelec.getNome());

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

        editarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = Integer.parseInt(String.valueOf(editarData.getText()).substring(6));
                int mes = Integer.parseInt(String.valueOf(editarData.getText()).substring(3, 5)) - 1;
                int dia = Integer.parseInt(String.valueOf(editarData.getText()).substring(0, 2));

                DatePickerDialog dialog = new DatePickerDialog(
                        EditarEventoActivity.this,
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
                String data = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month) + "/" + year;
                editarData.setText(data);
            }
        };

        editarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = Integer.parseInt(String.valueOf(editarHora.getText()).substring(0, 2));
                int minute = Integer.parseInt(String.valueOf(editarHora.getText()).substring(3));

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditarEventoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String strHora = selectedHour + ":" + String.format("%02d", selectedMinute);
                        editarHora.setText(strHora);
                    }
                }, hour, minute, true);
                mTimePicker.show();
            }
        });

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
                eventoAtual.setCidade(spnCidade.getSelectedItem().toString());
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
