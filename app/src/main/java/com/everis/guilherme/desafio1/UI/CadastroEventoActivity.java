package com.everis.guilherme.desafio1.UI;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
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
import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CadastroEventoActivity extends AppCompatActivity {

    Button btnCECadastrar;
    Button btnCECancelar;
    EditText edtCEEvento;
    EditText edtCELocal;
    TextView txtCEData;
    TextView txtCEHoraPicker;
    EditText edtCEVagas;
    private long idUsuarioAtivo;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Spinner spinner;

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
        String sEvento = edtCEEvento.getText().toString();
        String sLocal = edtCELocal.getText().toString();
        String sData = txtCEData.getText().toString();
        String sHora = txtCEHoraPicker.getText().toString();
        String sVagas = edtCEVagas.getText().toString();

        if(sEvento.equals("") || sLocal.equals("") || sData.equals("") ||
                sHora.equals("") || sVagas.equals("")){
            btnCECadastrar.setEnabled(false);
            btnCECadastrar.setAlpha(.5f);
        } else {
            btnCECadastrar.setEnabled(true);
            btnCECadastrar.setAlpha(1.0f);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuarioAtivo = extras.getLong("idUsuarioAtivo");
        }

        edtCEEvento = findViewById(R.id.edtCEEvento);
        edtCELocal = findViewById(R.id.edtCELocal);
        txtCEData = findViewById(R.id.txtCEDataPicker);
        txtCEHoraPicker = findViewById(R.id.txtCEHoraPicker);
        edtCEVagas = findViewById(R.id.edtCEVagas);
        btnCECadastrar = findViewById(R.id.btnCECadastrar);
        btnCECancelar = findViewById(R.id.btnCECancelar);

        edtCEEvento.addTextChangedListener(textWatcher);
        edtCELocal.addTextChangedListener(textWatcher);
        txtCEData.addTextChangedListener(textWatcher);
        txtCEHoraPicker.addTextChangedListener(textWatcher);
        edtCEVagas.addTextChangedListener(textWatcher);

        btnCECadastrar.setEnabled(false);
        btnCECadastrar.setAlpha(.5f);

        spinner = findViewById(R.id.spnCECidades);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_cidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
                String data = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month) + "/" + year;
                txtCEData.setText(data);
            }
        };

        txtCEHoraPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = 0;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CadastroEventoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String strHora = selectedHour + ":" + String.format("%02d", selectedMinute);
                        txtCEHoraPicker.setText(strHora);
                    }
                }, hour, minute, true);
                mTimePicker.show();
            }
        });

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
                String cidade = spinner.getSelectedItem().toString();
                String local = edtCELocal.getText().toString();
                String[] string = txtCEData.getText().toString().split("/");
                String dataFormatada = string[2] + "-" + string[1] + "-" + string[0];
                String hora = txtCEHoraPicker.getText().toString();
                int vagas = Integer.parseInt(edtCEVagas.getText().toString());
                int imagem = R.drawable.logo_everis; // HARDCODED!!!!
                evento = new Evento(nomeEvento, local, cidade, dataFormatada, hora, imagem, vagas, idUsuarioAtivo);

                if(eventoDAO.salvar(evento)){
                    Toast.makeText(getApplicationContext(), "Evento cadastrado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Evento não foi cadastrado!", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(CadastroEventoActivity.this, ListActivity.class);
                intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
                startActivity(intent);
            }
        });
    }

//    MÉTODO PARA UTILIZAR O UP NAVIGATION
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Intent intent = new Intent(CadastroEventoActivity.this, ListActivity.class);
//                intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
//                startActivity(intent);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastroEventoActivity.this, ListActivity.class);
        intent.putExtra("idUsuarioAtivo", idUsuarioAtivo);
        startActivity(intent);
    }
}
