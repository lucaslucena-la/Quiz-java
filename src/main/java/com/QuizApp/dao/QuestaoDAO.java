package com.QuizApp.dao;

import com.QuizApp.model.Questao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
public class QuestaoDAO {
    private static final SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Questao.class)
                .buildSessionFactory();
    }
    
    public void atualizar(Questao questao) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.merge(questao);
            session.getTransaction().commit();
        }
    }


    public void salvar(Questao questao) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.persist(questao);
            session.getTransaction().commit();
        }
    }

    public List<Questao> listarTodas() {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Questao", Questao.class).list();
        }
    }
    
    public List<Questao> listarPorDificuldade(String dificuldade) {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Questao WHERE dificuldade = :dif", Questao.class)
                          .setParameter("dif", dificuldade)
                          .list();
        }
    }


    public void excluir(Questao questao) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.remove(session.contains(questao) ? questao : session.merge(questao));
            session.getTransaction().commit();
        }
    }
    
    //tempórário
    public void salvarEmLote(List<Questao> lista) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            for (int i = 0; i < lista.size(); i++) {
                session.persist(lista.get(i));
                if (i % 20 == 0) { // flush a cada 20 para não estourar memória
                    session.flush();
                    session.clear();
                }
            }

            session.getTransaction().commit();
        }
    }

    public void close() {
        factory.close(); // só usar se for no final da aplicação
    }
}
