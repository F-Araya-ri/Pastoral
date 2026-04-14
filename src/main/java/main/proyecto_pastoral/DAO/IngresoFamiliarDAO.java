package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.IngresoFamiliar;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class IngresoFamiliarDAO implements InterfaceDAO<IngresoFamiliar> {

    private final SessionFactory sessionFactory;

    public IngresoFamiliarDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(IngresoFamiliar ingreso) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(ingreso);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar ingreso familiar", e);
        }
    }

    @Override
    public void actualizar(IngresoFamiliar ingreso) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(ingreso);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar ingreso familiar", e);
        }
    }

    @Override
    public List<IngresoFamiliar> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("IngresoFamiliar.listarTodos", IngresoFamiliar.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar ingresos familiares", e);
        }
    }

    public Optional<IngresoFamiliar> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(IngresoFamiliar.class, id));
        }
    }

    public List<IngresoFamiliar> buscarPorPersona(int idPersona) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery(
                    "IngresoFamiliar.buscarPorPersona", IngresoFamiliar.class)
                    .setParameter("id", idPersona)
                    .list();
        }
    }

    public BigDecimal sumarIngresosPorRegistro(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            BigDecimal total = session.createNamedQuery(
                    "IngresoFamiliar.sumarIngresosPorRegistro", BigDecimal.class)
                    .setParameter("numero", numeroRegistro)
                    .uniqueResult();
            return total != null ? total : BigDecimal.ZERO;
        }
    }
}
