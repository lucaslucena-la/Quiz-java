// Define o pacote onde essa classe está localizada
package com.QuizApp.dao;

// Importa a classe de modelo que será manipulada
import com.QuizApp.model.*;
// Importações do Hibernate para trabalhar com sessões e fábrica de sessões
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*; // Importa utilitários, como List

// Classe responsável pelas operações de acesso ao banco de dados (DAO = Data Access Object)
public class QuizAppDAO {

    // Cria uma fábrica de sessões do Hibernate como um campo estático (compartilhado por todas as instâncias)
    private static final SessionFactory factory;

    // Bloco estático que é executado uma vez quando a classe é carregada
    static {
        // Configura a SessionFactory com base no arquivo de configuração e classe anotada
        factory = new Configuration()
                .configure("hibernate.cfg.xml") // Lê as configurações do Hibernate no XML
                .addAnnotatedClass(QuizModel.class) // Informa qual classe será mapeada (entidade)
                .buildSessionFactory(); // Cria a SessionFactory
    }

    // Método para salvar um novo objeto QuizModel no banco de dados
    public void salvar(QuizModel quiz) {
        // Cria uma sessão do Hibernate e garante que será fechada automaticamente
        try (Session session = factory.openSession()) {
            session.beginTransaction(); // Inicia a transação
            session.persist(quiz);  // Persiste o objeto no banco de dados
            session.getTransaction().commit(); // Confirma a transação
        }
    }

    // Método para atualizar um objeto existente no banco de dados
    public void atualizar(QuizModel quiz) {
        try (Session session = factory.openSession()) {
            session.beginTransaction(); // Inicia a transação
            session.merge(quiz);     // Atualiza o objeto com base no ID
            session.getTransaction().commit(); // Confirma a transação
        }
    }

    // Método para excluir um objeto do banco de dados
    public void excluir(QuizModel quiz) {
        try (Session session = factory.openSession()) {
            session.beginTransaction(); // Inicia a transação
            session.remove(quiz);    // Remove o objeto
            session.getTransaction().commit(); // Confirma a transação
        }
    }

    // Método que retorna uma lista com todos os registros da tabela QuizModel
    public List<QuizModel> listarTodos() {
        try (Session session = factory.openSession()) {
            // Executa uma query HQL (parecida com SQL) para buscar todos os objetos da entidade
            return session.createQuery("FROM QuizModel", QuizModel.class).list();
        }
    }

    // Método para buscar um objeto pelo seu ID
    public QuizModel buscarPorId(Long id) {
        try (Session session = factory.openSession()) {
            return session.get(QuizModel.class, id); // Retorna o objeto com o ID informado
        }
    }

    // Fecha a SessionFactory quando não for mais necessária (libera recursos)
    public void close() {
        factory.close();
    }
    
    public QuizModel buscarPorNomeESenha(String nome, String senha) {
        try (Session session = factory.openSession()) {
        	List<QuizModel> resultados = session.createQuery("FROM QuizModel WHERE nome = :nome AND senha = :senha", QuizModel.class)
        	        .setParameter("nome", nome)
        	        .setParameter("senha", senha)
        	        .list();

        	return resultados.isEmpty() ? null : resultados.get(0);

        }
    }

}
