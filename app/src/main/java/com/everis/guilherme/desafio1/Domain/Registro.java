package com.everis.guilherme.desafio1.Domain;

public class Registro {
    private long id;
    private long idEvento;
    private long idParticipante;
    private boolean participanteAtivo;

    public Registro(long idEvento, long idParticipante) {
        this.idEvento = idEvento;
        this.idParticipante = idParticipante;
        this.participanteAtivo = true;
    }

    public Registro(){}

    public boolean isParticipanteAtivo() {
        return participanteAtivo;
    }

    public void setParticipanteAtivo(boolean participanteAtivo) {
        this.participanteAtivo = participanteAtivo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(long idParticipante) {
        this.idParticipante = idParticipante;
    }

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }
}
