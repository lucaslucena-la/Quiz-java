package com.QuizApp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pontuacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private int pontos;
    private LocalDateTime data;

    public Pontuacao() {
    }

    public Pontuacao(String usuario, int pontos, LocalDateTime data) {
        this.usuario = usuario;
        this.pontos = pontos;
        this.data = data;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return usuario + " - " + pontos + " pontos em " + data;
    }
}
