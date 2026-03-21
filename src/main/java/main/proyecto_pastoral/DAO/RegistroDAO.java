package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.Registro;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RegistroDAO implements InterfaceDAO<Registro> {

    private final SessionFactory sessionFactory;

    public RegistroDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Registro registro) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(registro);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar registro", e);
        }
    }

    public int guardarYRetornarId(Registro registro) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(registro);
            session.flush();
            int numeroRegistro = registro.getNumeroRegistro();
            transaction.commit();
            return numeroRegistro;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar registro", e);
        }
    }

    @Override
    public void actualizar(Registro registro) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(registro);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar registro", e);
        }
    }

    @Override
    public List<Registro> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Registro", Registro.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar registros", e);
        }
    }

    public Optional<Registro> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Registro.class, id));
        }
    }

    public Optional<Registro> buscarCompleto(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Registro r " +
                                    "LEFT JOIN FETCH r.vivienda " +
                                    "LEFT JOIN FETCH r.personas " +
                                    "LEFT JOIN FETCH r.asistencias " +
                                    "WHERE r.numeroRegistro = :id", Registro.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }

    public List<Registro> buscarPorSector(int idSector) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Registro r WHERE r.sector.idSector = :id ORDER BY r.fechaInicio DESC", Registro.class)
                    .setParameter("id", idSector)
                    .list();
        }
    }

    public List<Registro> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Registro r WHERE r.fechaInicio BETWEEN :desde AND :hasta ORDER BY r.fechaInicio DESC", Registro.class)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .list();
        }
    }

    public List<Registro> buscarAbiertos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Registro r WHERE r.fechaConclusion IS NULL ORDER BY r.fechaInicio", Registro.class)
                    .list();
        }
    }

    public List<Registro> buscarPorEntrevistador(int idEntrevistador) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Registro r WHERE r.entrevistador.idEntrevistador = :id ORDER BY r.fechaInicio DESC", Registro.class)
                    .setParameter("id", idEntrevistador)
                    .list();
        }
    }
}