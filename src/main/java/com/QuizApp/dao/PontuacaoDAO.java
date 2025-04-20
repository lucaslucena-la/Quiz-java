package com.QuizApp.dao;

import com.QuizApp.model.Pontuacao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class PontuacaoDAO {
    private static final SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Pontuacao.class)
                .buildSessionFactory();
    }

    public void salvar(Pontuacao p) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.persist(p);
            session.getTransaction().commit();
        }
    }

    public List<Pontuacao> listarTodas() {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Pontuacao ORDER BY pontos DESC", Pontuacao.class).list();
        }
    }
}
