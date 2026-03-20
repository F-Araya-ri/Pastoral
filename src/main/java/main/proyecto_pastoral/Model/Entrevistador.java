package main.proyecto_pastoral.Model;



import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Entrevistador")
public class Entrevistador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEntrevistador")
    private Integer idEntrevistador;

    @Column(name = "Nombre")
    private String nombre;

    @OneToMany(mappedBy = "entrevistador")
    private List<Registro> registros;

    public Entrevistador() {}

    public Integer getIdEntrevistador() { return idEntrevistador; }
    public void setIdEntrevistador(Integer idEntrevistador) { this.idEntrevistador = idEntrevistador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Registro> getRegistros() { return registros; }
    public void setRegistros(List<Registro> registros) { this.registros = registros; }
}