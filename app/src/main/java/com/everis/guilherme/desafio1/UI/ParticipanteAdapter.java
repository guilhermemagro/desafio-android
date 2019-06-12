package com.everis.guilherme.desafio1.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.everis.guilherme.desafio1.DAO.UsuarioDAO;
import com.everis.guilherme.desafio1.Domain.Participante;
import com.everis.guilherme.desafio1.R;

import java.util.ArrayList;

public class ParticipanteAdapter extends ArrayAdapter<Participante> {
    private final Context context;
    private final ArrayList<Participante> elementos;

    public ParticipanteAdapter(Context context, ArrayList<Participante> elementos){
        super(context, R.layout.linha_participante, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Participante participante = elementos.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha_participante, null);

        UsuarioDAO usuarioDAO = new UsuarioDAO(getContext());

        TextView nomePart = (TextView) rowView.findViewById(R.id.txtPartNome);
        TextView emailPart = (TextView) rowView.findViewById(R.id.txtPartEmail);
        TextView telefonePart = (TextView) rowView.findViewById(R.id.txtPartTelefone);
        TextView conhecePart = (TextView) rowView.findViewById(R.id.txtPartConhece);
        TextView criadorPart = (TextView) rowView.findViewById(R.id.txtPartCriador);

        String descEmail = "Email: " + elementos.get(position).getEmail();
        String descTel = "Tel.: " + elementos.get(position).getTelefone();
        String descConhece = "Conhecimento sobre o assunto: " +
                (elementos.get(position).isConheceTema() ? "Sim" : "Não");
        String descCriador = "Matricula do registrador: " +
                usuarioDAO.buscarPorId(elementos.get(position).getIdUsuario()).getMatricula();

        nomePart.setText(elementos.get(position).getNome());
        emailPart.setText(descEmail);
        telefonePart.setText(descTel);
        conhecePart.setText(descConhece);
        criadorPart.setText(descCriador);

        return rowView;
    }
}
