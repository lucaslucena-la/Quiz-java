package com.QuizApp.model;

import jakarta.persistence.*;

@Entity
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String enunciado;

    private String opcaoA;
    private String opcaoB;
    private String opcaoC;
    private String opcaoD;

    private String correta;      // A, B, C ou D
    private String dificuldade;  // Fácil, Médio ou Difícil

    public Questao() {}

    public Questao(String enunciado, String opcaoA, String opcaoB, String opcaoC, String opcaoD, String correta, String dificuldade) {
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.correta = correta;
        this.dificuldade = dificuldade;
    }

    // Getters e setters...

    public Long getId() { return id; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getOpcaoA() { return opcaoA; }
    public void setOpcaoA(String opcaoA) { this.opcaoA = opcaoA; }

    public String getOpcaoB() { return opcaoB; }
    public void setOpcaoB(String opcaoB) { this.opcaoB = opcaoB; }

    public String getOpcaoC() { return opcaoC; }
    public void setOpcaoC(String opcaoC) { this.opcaoC = opcaoC; }

    public String getOpcaoD() { return opcaoD; }
    public void setOpcaoD(String opcaoD) { this.opcaoD = opcaoD; }

    public String getCorreta() { return correta; }
    public void setCorreta(String correta) { this.correta = correta; }

    public String getDificuldade() { return dificuldade; }
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }

    @Override
    public String toString() {
        return "Questão: " + enunciado + " [Dificuldade: " + dificuldade + "]";
    }
}
