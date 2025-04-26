// Define o pacote onde a classe Questao está localizada
package com.QuizApp.model;

// Importa anotações do Jakarta Persistence (JPA) para mapeamento da entidade
import jakarta.persistence.*;

// Indica que esta classe é uma entidade JPA (será mapeada para uma tabela no banco de dados)
@Entity
public class Questao {

    // Define o atributo 'id' como chave primária da tabela
    @Id
    // Configura a geração automática do ID usando a estratégia de auto-incremento do banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único da questão

    // Define o campo 'enunciado' com um tamanho máximo de 1000 caracteres
    @Column(length = 1000)
    private String enunciado; // Texto do enunciado da questão

    private String opcaoA; // Alternativa A
    private String opcaoB; // Alternativa B
    private String opcaoC; // Alternativa C
    private String opcaoD; // Alternativa D

    private String correta;      // Indica qual alternativa é correta (A, B, C ou D)
    private String dificuldade;  // Define o nível de dificuldade (Fácil, Médio ou Difícil)

    // Construtor padrão (obrigatório para o JPA)
    public Questao() {}

    // Construtor com parâmetros para facilitar a criação de instâncias
    public Questao(String enunciado, String opcaoA, String opcaoB, String opcaoC, String opcaoD, String correta, String dificuldade) {
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.correta = correta;
        this.dificuldade = dificuldade;
    }

    // Métodos getters e setters

    public Long getId() { // Retorna o ID da questão
        return id;
    }

    public String getEnunciado() { // Retorna o enunciado da questão
        return enunciado;
    }
    public void setEnunciado(String enunciado) { // Define o enunciado da questão
        this.enunciado = enunciado;
    }

    public String getOpcaoA() { // Retorna a alternativa A
        return opcaoA;
    }
    public void setOpcaoA(String opcaoA) { // Define a alternativa A
        this.opcaoA = opcaoA;
    }

    public String getOpcaoB() { // Retorna a alternativa B
        return opcaoB;
    }
    public void setOpcaoB(String opcaoB) { // Define a alternativa B
        this.opcaoB = opcaoB;
    }

    public String getOpcaoC() { // Retorna a alternativa C
        return opcaoC;
    }
    public void setOpcaoC(String opcaoC) { // Define a alternativa C
        this.opcaoC = opcaoC;
    }

    public String getOpcaoD() { // Retorna a alternativa D
        return opcaoD;
    }
    public void setOpcaoD(String opcaoD) { // Define a alternativa D
        this.opcaoD = opcaoD;
    }

    public String getCorreta() { // Retorna qual alternativa é correta
        return correta;
    }
    public void setCorreta(String correta) { // Define qual alternativa é correta
        this.correta = correta;
    }

    public String getDificuldade() { // Retorna o nível de dificuldade da questão
        return dificuldade;
    }
    public void setDificuldade(String dificuldade) { // Define o nível de dificuldade
        this.dificuldade = dificuldade;
    }

    // Sobrescreve o método toString para retornar uma representação textual da questão
    @Override
    public String toString() {
        return "Questão: " + enunciado + " [Dificuldade: " + dificuldade + "]";
    }
}
