package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.Parroquia;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class ParroquiaDAO implements InterfaceDAO<Parroquia> {

    private final SessionFactory sessionFactory;

    public ParroquiaDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Parroquia parroquia) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(parroquia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar parroquia", e);
        }
    }

    @Override
    public void actualizar(Parroquia parroquia) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(parroquia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar parroquia", e);
        }
    }

    @Override
    public List<Parroquia> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Parroquia.listarTodos", Parroquia.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar parroquias", e);
        }
    }

    public Optional<Parroquia> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Parroquia.class, id));
        }
    }

    public Optional<Parroquia> buscarPorNombre(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Parroquia.buscarPorNombre", Parroquia.class)
                    .setParameter("nombre", nombre)
                    .uniqueResultOptional();
        }
    }

    public Optional<Parroquia> buscarConSectores(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Parroquia.buscarConSectores", Parroquia.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }
}