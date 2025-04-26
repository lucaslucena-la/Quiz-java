// Define o pacote onde a classe Pontuacao está localizada
package com.QuizApp.model;

// Importa anotações do Jakarta Persistence (JPA) para mapeamento da entidade
import jakarta.persistence.*;
// Importa a classe LocalDateTime para armazenar data e hora
import java.time.LocalDateTime;

// Indica que esta classe é uma entidade JPA (será mapeada para uma tabela no banco de dados)
@Entity
public class Pontuacao {

    // Define o atributo 'id' como chave primária da tabela
    @Id
    // Configura a geração automática do ID usando a estratégia de auto-incremento do banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único da pontuação

    private String usuario; // Nome do usuário que realizou a pontuação
    private int pontos; // Número de pontos obtidos
    private LocalDateTime data; // Data e hora da pontuação

    // Construtor padrão (obrigatório para o JPA)
    public Pontuacao() {
    }

    // Construtor com parâmetros para facilitar a criação de instâncias
    public Pontuacao(String usuario, int pontos, LocalDateTime data) {
        this.usuario = usuario;
        this.pontos = pontos;
        this.data = data;
    }

    // Métodos getters e setters

    public Long getId() { // Retorna o ID da pontuação
        return id;
    }

    public String getUsuario() { // Retorna o nome do usuário
        return usuario;
    }

    public void setUsuario(String usuario) { // Define o nome do usuário
        this.usuario = usuario;
    }

    public int getPontos() { // Retorna o número de pontos
        return pontos;
    }

    public void setPontos(int pontos) { // Define o número de pontos
        this.pontos = pontos;
    }

    public LocalDateTime getData() { // Retorna a data da pontuação
        return data;
    }

    public void setData(LocalDateTime data) { // Define a data da pontuação
        this.data = data;
    }

    // Sobrescreve o método toString para retornar uma representação textual da pontuação
    @Override
    public String toString() {
        return usuario + " - " + pontos + " pontos em " + data;
    }
}
