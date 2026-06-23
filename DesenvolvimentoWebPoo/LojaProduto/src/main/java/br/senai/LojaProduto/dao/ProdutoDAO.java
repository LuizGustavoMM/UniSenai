package br.senai.LojaProduto.dao;

import br.senai.LojaProduto.model.Produto;
import br.senai.LojaProduto.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoDAO {

    public void salvar(Produto produto) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(produto);
            transaction.commit();
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception rollbackEx) {
                System.err.println("Erro ao realizar rollback: " + rollbackEx.getMessage());
            }

            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage(), e);
        }
    }

    public void atualizar(Produto produto) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(produto);
            transaction.commit();
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception rollbackEx) {
                System.err.println("Erro ao realizar rollback: " + rollbackEx.getMessage());
            }

            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    public void excluir(Long codigo) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Produto produto = session.get(Produto.class, codigo);
            if (produto != null) {
                session.remove(produto);
            }

            transaction.commit();
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception rollbackEx) {
                System.err.println("Erro ao realizar rollback: " + rollbackEx.getMessage());
            }

            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage(), e);
        }
    }

    public Produto buscarPorCodigo(Long codigo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produto.class, codigo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produto por código: " + e.getMessage(), e);
        }
    }

    public List<Produto> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produto", Produto.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
    }

}
