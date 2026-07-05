package manager;

import model.Aluno;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class AlunoJpaController {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SuperTrunfoPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Requisito: create(Aluno aluno)
    public void create(Aluno aluno) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(aluno);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // Requisito: destroy(String matricula)
    public void destroy(String matricula) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Aluno aluno = em.find(Aluno.class, matricula);
            if (aluno != null) {
                em.remove(aluno);
            } else {
                throw new Exception("Aluno com matrícula " + matricula + " não existe.");
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // Requisito: findAlunoEntities() usando NamedQuery
    public List<Aluno> findAlunoEntities() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Aluno.buscarTodos", Aluno.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static void fecharFabrica() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}