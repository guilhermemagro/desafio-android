package com.everis.guilherme.desafio1.Domain;

public class Participante implements Comparable<Participante> {
    private long id;
    private String nome;
    private String email;
    private String telefone;
    private boolean conheceTema;
    private long idUsuario;

    public Participante(String nome, String email, String telefone, boolean conheceTema, long idUsuario) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.conheceTema = conheceTema;
        this.idUsuario = idUsuario;
    }

    public Participante(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isConheceTema() {
        return conheceTema;
    }

    public void setConheceTema(boolean conheceTema) {
        this.conheceTema = conheceTema;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int compareTo(Participante o) {
        return getNome().compareTo(o.getNome());
    }
}
