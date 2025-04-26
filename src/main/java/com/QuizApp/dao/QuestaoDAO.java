// Define o pacote onde esta classe está localizada
package com.QuizApp.dao;

// Importa a classe Questao do pacote model
import com.QuizApp.model.Questao;
// Importa classes do Hibernate necessárias para sessões e configuração
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Importa a classe List para trabalhar com listas de objetos
import java.util.List;

// Declara a classe pública QuestaoDAO
public class QuestaoDAO {
    // Declara a fábrica de sessões Hibernate como constante
    private static final SessionFactory factory;

    // Bloco estático para inicializar a fábrica de sessões
    static {
        factory = new Configuration() // Cria uma configuração Hibernate
                .configure("hibernate.cfg.xml") // Usa o arquivo de configuração XML
                .addAnnotatedClass(Questao.class) // Informa que a classe Questao será gerenciada pelo Hibernate
                .buildSessionFactory(); // Constrói a fábrica de sessões
    }
    
    // Método para atualizar uma questão existente no banco
    public void atualizar(Questao questao) {
        try (Session session = factory.openSession()) { // Abre uma nova sessão
            session.beginTransaction(); // Inicia uma transação
            session.merge(questao); // Atualiza o objeto questao no banco
            session.getTransaction().commit(); // Finaliza (confirma) a transação
        }
    }

    // Método para salvar uma nova questão no banco
    public void salvar(Questao questao) {
        try (Session session = factory.openSession()) { // Abre uma nova sessão
            session.beginTransaction(); // Inicia uma transação
            session.persist(questao); // Persiste (salva) a nova questão no banco
            session.getTransaction().commit(); // Finaliza (confirma) a transação
        }
    }

    // Método para listar todas as questões cadastradas no banco
    public List<Questao> listarTodas() {
        try (Session session = factory.openSession()) { // Abre uma nova sessão
            // Executa a consulta para buscar todas as questões
            return session.createQuery("FROM Questao", Questao.class).list();
        }
    }
    
    // Método para listar questões filtradas por dificuldade
    public List<Questao> listarPorDificuldade(String dificuldade) {
        try (Session session = factory.openSession()) { // Abre uma nova sessão
            // Executa a consulta para buscar questões com a dificuldade especificada
            return session.createQuery("FROM Questao WHERE dificuldade = :dif", Questao.class)
                          .setParameter("dif", dificuldade) // Define o parâmetro da dificuldade
                          .list(); // Retorna a lista de resultados
        }
    }

    // Método para excluir uma questão do banco
    public void excluir(Questao questao) {
        try (Session session = factory.openSession()) { // Abre uma nova sessão
            session.beginTransaction(); // Inicia uma transação
            // Remove a questão, verificando se ela já está anexada à sessão
            session.remove(session.contains(questao) ? questao : session.merge(questao));
            session.getTransaction().commit(); // Finaliza (confirma) a transação
        }
    }
    
    // Método temporário para salvar várias questões de uma vez (em lote)
    public void salvarEmLote(List<Questao> lista) {
        try (Session session = factory.openSession()) { // Abre uma nova sessão
            session.beginTransaction(); // Inicia uma transação

            // Percorre a lista de questões para salvar cada uma
            for (int i = 0; i < lista.size(); i++) {
                session.persist(lista.get(i)); // Salva a questão atual
                if (i % 20 == 0) { // A cada 20 inserções
                    session.flush(); // Força o envio das operações pendentes ao banco
                    session.clear(); // Limpa o cache da sessão para liberar memória
                }
            }

            session.getTransaction().commit(); // Finaliza (confirma) a transação
        }
    }

    // Método para fechar a fábrica de sessões (usar apenas no final da aplicação)
    public void close() {
        factory.close(); // Fecha a SessionFactory
    }
}
