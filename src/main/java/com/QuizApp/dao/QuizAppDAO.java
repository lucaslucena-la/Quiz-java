package com.QuizApp.dao;

import com.QuizApp.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;


public class QuizAppDAO {
    private static final SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(QuizModel.class)
                .buildSessionFactory();
    }

    public void salvar(QuizModel usuario) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.persist(usuario);
            session.getTransaction().commit();
        }
    }
    public void atualizar(QuizModel usuario) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.merge(usuario);
            session.getTransaction().commit();
        }
    }

    public void excluir(QuizModel usuario) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.remove(usuario);
            session.getTransaction().commit();
        }
    }

    public List<QuizModel> listarTodos() {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM QuizModel", QuizModel.class).list();
        }
    }

    public QuizModel buscarPorId(Long id) {
        try (Session session = factory.openSession()) {
            return session.get(QuizModel.class, id);
        }
    }

    public void close() {
        factory.close();
    }
}