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
    
    public QuizModel() {}

    public QuizModel(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

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