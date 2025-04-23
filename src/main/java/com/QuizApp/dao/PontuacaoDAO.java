package com.QuizApp.dao;

import com.QuizApp.model.Pontuacao;
import com.QuizApp.model.QuizModel; // ✅ Import necessário

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime; // ✅ Import necessário
import java.util.List;

public class PontuacaoDAO {
    private static final SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Pontuacao.class)
                .buildSessionFactory();
    }

    // Salva uma nova pontuação (se você quiser usar esse método manualmente)
    public void salvar(Pontuacao p) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.persist(p);
            session.getTransaction().commit();
        }
    }

    // Lista todas as pontuações, ordenadas por maior pontuação
    public List<Pontuacao> listarTodas() {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Pontuacao ORDER BY pontos DESC", Pontuacao.class).list();
        }
    }

    // ✅ Salva ou atualiza a pontuação acumulada de um usuário
    public void salvarOuAtualizarPontuacao(String nomeUsuario, int pontosGanhos) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            // Tenta buscar a pontuação existente do usuário
            Pontuacao existente = session.createQuery(
                    "FROM Pontuacao WHERE usuario = :nome", Pontuacao.class)
                    .setParameter("nome", nomeUsuario)
                    .uniqueResult();

            if (existente != null) {
                existente.setPontos(existente.getPontos() + pontosGanhos);
                existente.setData(LocalDateTime.now());
                session.merge(existente);
            } else {
                Pontuacao nova = new Pontuacao(nomeUsuario, pontosGanhos, LocalDateTime.now());
                session.persist(nova);
            }

            session.getTransaction().commit();
        }
    }
    
    public void excluir(Pontuacao pontuacao) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.remove(session.contains(pontuacao) ? pontuacao : session.merge(pontuacao));
            session.getTransaction().commit();
        }
    }

    

}
