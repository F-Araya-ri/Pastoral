package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.Asistencia;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AsistenciaDAO implements InterfaceDAO<Asistencia> {

    private final SessionFactory sessionFactory;

    public AsistenciaDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Asistencia asistencia) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(asistencia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar asistencia", e);
        }
    }

    @Override
    public void actualizar(Asistencia asistencia) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(asistencia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar asistencia", e);
        }
    }

    @Override
    public List<Asistencia> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Asistencia.listarTodos", Asistencia.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar asistencias", e);
        }
    }

    public Optional<Asistencia> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Asistencia.class, id));
        }
    }

    public List<Asistencia> buscarPorRegistro(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery(
                    "Asistencia.buscarPorRegistro", Asistencia.class)
                    .setParameter("numero", numeroRegistro)
                    .list();
        }
    }

    public List<Asistencia> buscarPorTipo(String tipo) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery(
                    "Asistencia.buscarPorTipo", Asistencia.class)
                    .setParameter("tipo", tipo)
                    .list();
        }
    }

    public BigDecimal sumarValorPorRegistro(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            BigDecimal total = session.createNamedQuery(
                    "Asistencia.sumarValorPorRegistro", BigDecimal.class)
                    .setParameter("numero", numeroRegistro)
                    .uniqueResult();
            return total != null ? total : BigDecimal.ZERO;
        }
    }
}
