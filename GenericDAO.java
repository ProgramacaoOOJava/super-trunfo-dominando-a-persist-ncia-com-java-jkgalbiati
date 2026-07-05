import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public abstract class GenericDAO<E, K> {
    protected static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SuperTrunfoPU");
    protected Class<E> classeEntidade;

    public GenericDAO(Class<E> classeEntidade) {
        this.classeEntidade = classeEntidade;
    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Operação 1: Incluir
    public void incluir(E entidade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Operação 2: Alterar
    public void alterar(E entidade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Operação 3: Excluir
    public void excluir(K chave) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            E entidade = em.find(classeEntidade, chave);
            if (entidade != null) {
                em.remove(entidade);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Operação 4: Obter por Id
    public E obter(K chave) {
        EntityManager em = getEntityManager();
        try {
            return em.find(classeEntidade, chave);
        } finally {
            em.close();
        }
    }

    // Operação 5: Obter todos
    public List<E> obterTodos() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT e FROM " + classeEntidade.getSimpleName() + " e";
            return em.createQuery(jpql, classeEntidade).getResultList();
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