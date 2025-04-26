// Define o pacote onde esta classe está localizada
package com.QuizApp.dao;

// Importa a classe Pontuacao do pacote model
import com.QuizApp.model.Pontuacao;
// Importa a classe QuizModel do pacote model (não usada nesse arquivo, mas importada)
import com.QuizApp.model.QuizModel;

// Importa classes do Hibernate necessárias para conexão e sessões
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Importa a classe LocalDateTime para trabalhar com data e hora
import java.time.LocalDateTime;
// Importa a classe List para trabalhar com listas de objetos
import java.util.List;

// Declara a classe pública PontuacaoDAO
public class PontuacaoDAO {

    // Cria uma variável factory do tipo SessionFactory como constante da classe
    private static final SessionFactory factory;

    // Bloco estático que inicializa a factory uma única vez
    static {
        // Cria uma SessionFactory configurando o Hibernate com o arquivo de configuração e mapeando a classe Pontuacao
        factory = new Configuration()
                .configure("hibernate.cfg.xml") // Carrega as configurações do Hibernate
                .addAnnotatedClass(Pontuacao.class) // Adiciona a entidade Pontuacao para o Hibernate gerenciar
                .buildSessionFactory(); // Constrói a fábrica de sessões
    }

    // Método para salvar uma nova pontuação no banco de dados
    public void salvar(Pontuacao p) {
        // Abre uma nova sessão usando a factory (try-with-resources garante fechamento automático)
        try (Session session = factory.openSession()) {
            session.beginTransaction(); // Inicia uma nova transação
            session.persist(p); // Persiste (salva) o objeto pontuacao no banco
            session.getTransaction().commit(); // Finaliza (confirma) a transação
        }
    }

    // Método para listar todas as pontuações do banco, ordenadas da maior para a menor
    public List<Pontuacao> listarTodas() {
        // Abre uma nova sessão
        try (Session session = factory.openSession()) {
            // Executa uma consulta HQL para listar pontuações ordenadas por pontos em ordem decrescente
            return session.createQuery("FROM Pontuacao ORDER BY pontos DESC", Pontuacao.class).list();
        }
    }

    // Método para salvar uma nova pontuação ou atualizar uma existente para um determinado usuário
    public void salvarOuAtualizarPontuacao(String nomeUsuario, int pontosGanhos) {
        // Abre uma nova sessão
        try (Session session = factory.openSession()) {
            session.beginTransaction(); // Inicia uma nova transação

            // Executa uma consulta para buscar a pontuação existente para o usuário pelo nome
            Pontuacao existente = session.createQuery(
                    "FROM Pontuacao WHERE usuario = :nome", Pontuacao.class) // Define a consulta HQL
                    .setParameter("nome", nomeUsuario) // Define o parâmetro da consulta
                    .uniqueResult(); // Obtém um único resultado (ou null)

            // Verifica se a pontuação do usuário já existe
            if (existente != null) {
                // Se existir, adiciona os novos pontos à pontuação atual
                existente.setPontos(existente.getPontos() + pontosGanhos);
                // Atualiza a data para o momento atual
                existente.setData(LocalDateTime.now());
                // Atualiza o objeto no banco
                session.merge(existente);
            } else {
                // Se não existir, cria uma nova pontuação
                Pontuacao nova = new Pontuacao(nomeUsuario, pontosGanhos, LocalDateTime.now());
                session.persist(nova); // Salva a nova pontuação no banco
            }

            session.getTransaction().commit(); // Finaliza (confirma) a transação
        }
    }
    
    // Método para excluir uma pontuação do banco de dados
    public void excluir(Pontuacao pontuacao) {
        // Abre uma nova sessão
        try (Session session = factory.openSession()) {
            session.beginTransaction(); // Inicia uma nova transação
            // Remove o objeto se já estiver na sessão, ou primeiro faz o merge e depois remove
            session.remove(session.contains(pontuacao) ? pontuacao : session.merge(pontuacao));
            session.getTransaction().commit(); // Finaliza (confirma) a transação
        }
    }
}
