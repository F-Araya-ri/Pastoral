package main.proyecto_pastoral.Model;



import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Entrevistador")
@NamedQueries({
    @NamedQuery(
        name = "Entrevistador.listarTodos",
        query = "FROM Entrevistador"
    ),
    @NamedQuery(
        name = "Entrevistador.buscarPorNombre",
        query = "FROM Entrevistador e WHERE LOWER(e.nombre) LIKE LOWER(:nombre)"
    )
})
public class Entrevistador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEntrevistador")
    private Integer idEntrevistador;

    @Column(name = "Nombre_Completo")
    private String nombre;

    @Column(name = "Telefono")
    private String telefono;
    @Column(name = "Email")
    private String email;
    @Column(name = "Estado_admin")
    private String estadoAdmin;



    @OneToMany(mappedBy = "entrevistador")
    private List<Registro> registros;

    public Entrevistador() {}

    public Integer getIdEntrevistador() { return idEntrevistador; }
    public void setIdEntrevistador(Integer idEntrevistador) { this.idEntrevistador = idEntrevistador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Registro> getRegistros() { return registros; }
    public void setRegistros(List<Registro> registros) { this.registros = registros; }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstadoAdmin() {
        return estadoAdmin;
    }

    public void setEstadoAdmin(String estadoAdmin) {
        this.estadoAdmin = estadoAdmin;
    }
}