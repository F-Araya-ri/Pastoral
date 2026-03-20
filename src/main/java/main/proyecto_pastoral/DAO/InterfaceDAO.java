package main.proyecto_pastoral.DAO;

import java.util.List;

public interface InterfaceDAO<T> {
    void guardar(T entidad);
    void actualizar(T entidad);
    List<T> listarTodos();
}
