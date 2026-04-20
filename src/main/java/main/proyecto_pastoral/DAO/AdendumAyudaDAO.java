package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.AdendumAyuda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class AdendumAyudaDAO implements InterfaceDAO<AdendumAyuda> {

    private final SessionFactory sessionFactory;

    public AdendumAyudaDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(AdendumAyuda entidad) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entidad);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar adendum de ayuda", e);
        }
    }

    @Override
    public void actualizar(AdendumAyuda entidad) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(entidad);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar adendum de ayuda", e);
        }
    }

    @Override
    public List<AdendumAyuda> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("AdendumAyuda.listarTodos", AdendumAyuda.class).list();
        }
    }

    public Optional<AdendumAyuda> buscarPorRegistro(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery(
                            "AdendumAyuda.buscarPorRegistro",
                            AdendumAyuda.class)
                    .setParameter("numero", numeroRegistro)
                    .uniqueResultOptional();
        }
    }
}
