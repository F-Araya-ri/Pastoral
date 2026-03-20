package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.Sector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class SectorDAO implements InterfaceDAO<Sector> {

    private final SessionFactory sessionFactory;

    public SectorDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Sector sector) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(sector);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar sector", e);
        }
    }

    @Override
    public void actualizar(Sector sector) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(sector);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar sector", e);
        }
    }

    @Override
    public List<Sector> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Sector s LEFT JOIN FETCH s.parroquia", Sector.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar sectores", e);
        }
    }

    public Optional<Sector> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Sector.class, id));
        }
    }

    public List<Sector> buscarPorParroquia(int idParroquia) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Sector s WHERE s.parroquia.idParroquia = :id ORDER BY s.nombreSector", Sector.class)
                    .setParameter("id", idParroquia)
                    .list();
        }
    }
}