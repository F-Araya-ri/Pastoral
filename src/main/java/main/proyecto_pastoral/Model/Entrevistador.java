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

    @Column(name = "primerApeliido")
    private String primerApeliido;

    @Column(name = "segundoApeliido")
    private String segundoApeliido;

    @Column(name = "Telefono")
    private String telefono;



    @OneToMany(mappedBy = "entrevistador")
    private List<Registro> registros;

    public Entrevistador() {}

    public Integer getIdEntrevistador() { return idEntrevistador; }
    public void setIdEntrevistador(Integer idEntrevistador) { this.idEntrevistador = idEntrevistador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Registro> getRegistros() { return registros; }
    public void setRegistros(List<Registro> registros) { this.registros = registros; }

    public String getPrimerApeliido() {
        return primerApeliido;
    }

    public void setPrimerApeliido(String primerApeliido) {
        this.primerApeliido = primerApeliido;
    }

    public String getSegundoApeliido() {
        return segundoApeliido;
    }

    public void setSegundoApeliido(String segundoApeliido) {
        this.segundoApeliido = segundoApeliido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getNombreCompleto(){
        return nombre + " " + primerApeliido + " " + segundoApeliido;
    }
}