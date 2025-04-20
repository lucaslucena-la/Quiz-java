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

    public Pontuacao() {}

    public Pontuacao(String usuario, int pontos, LocalDateTime data) {
        this.usuario = usuario;
        this.pontos = pontos;
        this.data = data;
    }

    public String getUsuario() { return usuario; }
    public int getPontos() { return pontos; }
    public LocalDateTime getData() { return data; }
}
