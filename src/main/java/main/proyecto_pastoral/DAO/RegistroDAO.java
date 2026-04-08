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
            return session.createNamedQuery("Registro.listarTodos", Registro.class)
                    .getResultList();
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
            return session.createNamedQuery("Registro.buscarCompleto", Registro.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }

    // ✅ Named Query
    public List<Registro> buscarPorSector(int idSector) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Registro.buscarPorSector", Registro.class)
                    .setParameter("id", idSector)
                    .getResultList();
        }
    }


    public List<Registro> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Registro.buscarPorRangoFechas", Registro.class)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .getResultList();
        }
    }


    public List<Registro> buscarAbiertos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Registro.buscarAbiertos", Registro.class)
                    .getResultList();
        }
    }


    public List<Registro> buscarPorEntrevistador(int idEntrevistador) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("Registro.buscarPorEntrevistador", Registro.class)
                    .setParameter("id", idEntrevistador)
                    .getResultList();
        }
    }
}