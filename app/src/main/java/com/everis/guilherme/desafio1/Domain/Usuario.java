package com.everis.guilherme.desafio1.Domain;

import java.io.Serializable;

public class Usuario implements Serializable {
    private long id;
    private int matricula;
    private String email;
    private String senha;
    private boolean admin;

    public Usuario(int matricula, String email, String senha) {
        this.matricula = matricula;
        this.email = email;
        this.senha = senha;
        // ENQUANTO NAO TEM ACESSO AO DB DA EVERIS
        if(matricula >= 100000){
            this.admin = true;
        } else {
            this.admin = false;
        }
    }

    public Usuario(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}


