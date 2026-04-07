package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.Persona;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class PersonaDAO implements InterfaceDAO<Persona> {

    protected final SessionFactory sessionFactory;

    public PersonaDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Persona persona) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(persona);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar persona", e);
        }
    }

    @Override
    public void actualizar(Persona persona) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(persona);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar persona", e);
        }
    }

    @Override
    public List<Persona> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Persona", Persona.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar personas", e);
        }
    }

    public Optional<Persona> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Persona.class, id));
        }
    }

    public List<Persona> buscarPorRegistro(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery(
                            "Persona.buscarPorRegistro", Persona.class)
                    .setParameter("numero", numeroRegistro)
                    .list();
        }
    }

    public Optional<Persona> buscarJefeFamilia(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery(
                            "Persona.buscarJefeFamilia", Persona.class)
                    .setParameter("numero", numeroRegistro)
                    .uniqueResultOptional();
        }
    }

    public List<Persona> buscarConIngresosPorRegistro(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery(
                            "Persona.buscarConIngresosPorRegistro", Persona.class)
                    .setParameter("numero", numeroRegistro)
                    .list();
        }
    }
}