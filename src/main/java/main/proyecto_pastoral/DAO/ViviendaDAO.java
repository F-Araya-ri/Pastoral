package main.proyecto_pastoral.DAO;

import main.proyecto_pastoral.Model.Vivienda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class ViviendaDAO implements InterfaceDAO<Vivienda> {

    private final SessionFactory sessionFactory;

    public ViviendaDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Vivienda vivienda) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(vivienda);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al guardar vivienda", e);
        }
    }

    @Override
    public void actualizar(Vivienda vivienda) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(vivienda);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar vivienda", e);
        }
    }

    @Override
    public List<Vivienda> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Vivienda", Vivienda.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar viviendas", e);
        }
    }

    public Optional<Vivienda> buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Vivienda.class, id));
        }
    }

    public Optional<Vivienda> buscarPorRegistro(int numeroRegistro) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Vivienda v WHERE v.registro.numeroRegistro = :numero", Vivienda.class)
                    .setParameter("numero", numeroRegistro)
                    .uniqueResultOptional();
        }
    }

    public List<Vivienda> buscarPorTipo(String tipo) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Vivienda v WHERE LOWER(v.tipo) = LOWER(:tipo)", Vivienda.class)
                    .setParameter("tipo", tipo)
                    .list();
        }
    }
}