package com.everis.guilherme.desafio1.Domain;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;

public class Evento implements Serializable, Comparable<Evento>{
    private long id;
    private String nome;
    private String local;
    private String cidade;
    private LocalDate data;
    private LocalTime horario;
    private int vagas;
    private int imagem;
    private long idCriador;
    private boolean ativo;

    public Evento(){}

    public Evento(String nome, String local, String cidade, String data, String horario, int imagem, int vagas, long idCriador) {
        this.nome = nome;
        this.local = local;
        this.cidade = cidade;
        this.data = new LocalDate(data);
        this.horario = new LocalTime(horario);
        this.imagem = imagem;
        this.vagas = vagas;
        this.idCriador = idCriador;
        this.ativo = true;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public long getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(long idCriador) {
        this.idCriador = idCriador;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public String getData() {
        return data.toString("dd/MM/yyyy");
    }

    public void setData(String data) {
        String[] dataArray = data.split("/");
        String dataFormatada = dataArray[2] + "-" + dataArray[1] + "-" + dataArray[0];
        this.data = new LocalDate(dataFormatada);
    }

    public String getHorario() {
        return horario.toString("HH:mm");
    }

    public void setHorario(String horario) {
        this.horario = new LocalTime(horario);
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    private LocalDate getDataFormatoData(){
        return this.data;
    }

    @Override
    public int compareTo(Evento o) {
        return getDataFormatoData().compareTo(o.getDataFormatoData());
    }

}
