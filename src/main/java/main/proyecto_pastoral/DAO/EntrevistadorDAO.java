package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.Entrevistador;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class EntrevistadorDAO implements InterfaceDAO<Entrevistador> {

    private final SessionFactory sessionFactory;

    public EntrevistadorDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Entrevistador entrevistador) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entrevistador);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar entrevistador", e);
        }
    }

    @Override
    public void actualizar(Entrevistador entrevistador) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(entrevistador);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar entrevistador", e);
        }
    }

    @Override
    public List<Entrevistador> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Entrevistador", Entrevistador.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar entrevistadores", e);
        }
    }

    public Optional<Entrevistador> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Entrevistador.class, id));
        }
    }

    public Optional<Entrevistador> buscarPorNombre(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Entrevistador e WHERE LOWER(e.nombre) LIKE LOWER(:nombre)", Entrevistador.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .uniqueResultOptional();
        }
    }

}