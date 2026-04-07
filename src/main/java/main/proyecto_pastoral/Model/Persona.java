package main.proyecto_pastoral.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Persona")
@NamedQueries(value = {
        @NamedQuery(name = "Persona.buscarConIngresosPorRegistro",query = "SELECT p FROM Persona p LEFT JOIN FETCH p.ingresos WHERE p.registro.numeroRegistro = :numero")})
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Persona", nullable = false)
    private int idPersona;

    @Column(name = "TipoDocumento", length = 100)
    private String tipoDocumento;

    @Column(name = "numeroIdentificacion", length = 50)
    private String numeroIdentificacion;

    @Column(name = "Nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "PrimerApellido", nullable = false, length = 150)
    private String PrimerApellido;

    @Column(name = "SegundoApellido", nullable = false, length = 150)
    private String SegundoApellido;

    @Column(name = "Telefono")
    private String telefono;

    @Column(name = "Sexo", length = 20)
    private String sexo;

    @Column(name = "Jefatura")
    private String jefatura;

    @Column(name = "Relacion", length = 100)
    private String relacion;

    @Column(name = "Edad")
    private int edad;

    @Column(name = "Pais", length = 100)
    private String pais;

    @Column(name = "Migracion", length = 100)
    private String migracion;

    @Column(name = "Educacion", length = 100)
    private String educacion;

    @Column(name = "Salud", length = 100)
    private String salud;

    @Column(name = "Seguro", length = 100)
    private String seguro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdRegistro", nullable = false)
    private Registro registro;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IngresoFamiliar> ingresos = new ArrayList<>();

    public Persona() {}

    public Persona(String tipoDocumento, String numeroIdentificacion,
                   String nombre,String primerApellido,String segundoApellido,String telefono,String sexo, String jefatura, String relacion, int edad,
                   String pais, String migracion, String educacion,
                   String salud, String seguro, Registro registro) {
        this.tipoDocumento = tipoDocumento;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.PrimerApellido = primerApellido;
        this.SegundoApellido =segundoApellido;
        this.telefono = telefono;
        this.sexo = sexo;
        this.jefatura = jefatura;
        this.relacion = relacion;
        this.edad = edad;
        this.pais = pais;
        this.migracion = migracion;
        this.educacion = educacion;
        this.salud = salud;
        this.seguro = seguro;
        this.registro = registro;
    }

    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String isJefatura() { return jefatura; }
    public void setJefatura(String jefatura) { this.jefatura = jefatura; }

    public String getRelacion() { return relacion; }
    public void setRelacion(String relacion) { this.relacion = relacion; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getMigracion() { return migracion; }
    public void setMigracion(String migracion) { this.migracion = migracion; }

    public String getEducacion() { return educacion; }
    public void setEducacion(String educacion) { this.educacion = educacion; }

    public String getSalud() { return salud; }
    public void setSalud(String salud) { this.salud = salud; }

    public String getSeguro() { return seguro; }
    public void setSeguro(String seguro) { this.seguro = seguro; }

    public Registro getRegistro() { return registro; }
    public void setRegistro(Registro registro) { this.registro = registro; }

    public List<IngresoFamiliar> getIngresos() { return ingresos; }
    public void setIngresos(List<IngresoFamiliar> ingresos) { this.ingresos = ingresos; }

    public String getSegundoApellido() {
        return SegundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        SegundoApellido = segundoApellido;
    }

    public String getPrimerApellido() {
        return PrimerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        PrimerApellido = primerApellido;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto(){
        return nombre + " " + PrimerApellido + " " + SegundoApellido;
    }

}