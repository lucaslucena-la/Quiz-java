package com.QuizApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class QuizModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String senha;
    private boolean admin;
    
    public QuizModel() {}

    public QuizModel(String nome, String senha, boolean admin) {
        this.nome = nome;
        this.senha = senha;
        this.admin = admin;//por padrão
    }
    
    public boolean getAdmin(){return admin;}
    public void setAdmin(boolean admin) {this.admin = admin;}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return senha; }
    public void setEmail(String senha) { this.senha = senha; }
    
    @Override
    public String toString() {
        return id + " - " + "Nome: " + nome ;
    }
}