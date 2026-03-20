package main.proyecto_pastoral.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Persona")
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

    @Column(name = "PrimerApellido", nullable = false, length = 100)
    private String primerApellido;

    @Column(name = "SegundoApellido", length = 100)
    private String segundoApellido;

    @Column(name = "Sexo", length = 20)
    private String sexo;

    @Column(name = "Jefatura")
    private boolean jefatura;

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
                   String nombre, String primerApellido, String segundoApellido,
                   String sexo, boolean jefatura, String relacion, int edad,
                   String pais, String migracion, String educacion,
                   String salud, String seguro, Registro registro) {
        this.tipoDocumento = tipoDocumento;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
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

    public String getNombreCompleto() {
        return nombre + " " + primerApellido + " " + (segundoApellido != null ? segundoApellido : "");
    }

    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getnumeroIdentificacion() { return numeroIdentificacion; }
    public void setnumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public boolean isJefatura() { return jefatura; }
    public void setJefatura(boolean jefatura) { this.jefatura = jefatura; }

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
}