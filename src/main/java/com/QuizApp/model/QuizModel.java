// Define o pacote onde a classe QuizModel está localizada
package com.QuizApp.model;

// Importa anotações do Jakarta Persistence (JPA) para mapeamento da entidade
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Indica que esta classe é uma entidade JPA (será mapeada para uma tabela no banco de dados)
@Entity
public class QuizModel {

    // Define o atributo 'id' como chave primária da tabela
    @Id
    // Configura a geração automática do ID usando a estratégia de auto-incremento do banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único do usuário

    private String nome; // Nome do usuário
    private String senha; // Senha do usuário
    private boolean admin; // Indica se o usuário é administrador (true/false)
    
    // Construtor padrão (obrigatório para o JPA)
    public QuizModel() {}

    // Construtor com parâmetros para facilitar a criação de instâncias
    public QuizModel(String nome, String senha, boolean admin) {
        this.nome = nome; // Define o nome
        this.senha = senha; // Define a senha
        this.admin = admin; // Define se o usuário é administrador
    }
    
    // Métodos getters e setters

    public boolean getAdmin() { // Retorna se o usuário é administrador
        return admin;
    }
    public void setAdmin(boolean admin) { // Define se o usuário é administrador
        this.admin = admin;
    }

    public Long getId() { // Retorna o ID do usuário
        return id;
    }
    public void setId(Long id) { // Define o ID do usuário
        this.id = id;
    }
    
    public String getNome() { // Retorna o nome do usuário
        return nome;
    }
    public void setNome(String nome) { // Define o nome do usuário
        this.nome = nome;
    }
    
    public String getSenha() { // Retorna a senha do usuário
        return senha;
    }
    public void setSenha(String senha) { // Define a senha do usuário
        this.senha = senha;
    }
    
    // Sobrescreve o método toString para retornar uma representação textual do usuário
    @Override
    public String toString() {
        return id + " - " + "Nome: " + nome;
    }
}
