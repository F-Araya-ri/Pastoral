package main.proyecto_pastoral.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Registro")

@NamedQueries({
        @NamedQuery(name = "Registro.listarTodos",
                query = "SELECT r FROM Registro r"),

        @NamedQuery(name = "Registro.buscarCompleto",
                query = "SELECT r FROM Registro r " +
                        "LEFT JOIN FETCH r.vivienda " +
                        "LEFT JOIN FETCH r.personas " +
                        "LEFT JOIN FETCH r.asistencias " +
                        "WHERE r.numeroRegistro = :id"),

        @NamedQuery(name = "Registro.buscarPorSector",
                query = "SELECT r FROM Registro r WHERE r.sector.idSector = :id ORDER BY r.fechaInicio DESC"),

        @NamedQuery(name = "Registro.buscarPorRangoFechas",
                query = "SELECT r FROM Registro r WHERE r.fechaInicio BETWEEN :desde AND :hasta ORDER BY r.fechaInicio DESC"),

        @NamedQuery(name = "Registro.buscarAbiertos",
                query = "SELECT r FROM Registro r WHERE r.fechaConclusion IS NULL ORDER BY r.fechaInicio"),

        @NamedQuery(name = "Registro.buscarPorEntrevistador",
                query = "SELECT r FROM Registro r WHERE r.entrevistador.idEntrevistador = :id ORDER BY r.fechaInicio DESC")
})
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Numero_Registro", nullable = false)
    private int numeroRegistro;

    @Column(name = "FechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FechaConclusion")
    private LocalDate fechaConclusion;

    @Column(name = "Observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdSector", nullable = false)
    private Sector sector;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdEntrevistador", nullable = false)
    private Entrevistador entrevistador;

    @OneToOne(mappedBy = "registro", cascade = CascadeType.ALL)
    private Vivienda vivienda;

    @OneToMany(mappedBy = "registro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Persona> personas = new ArrayList<>();

    @OneToMany(mappedBy = "registro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asistencia> asistencias = new ArrayList<>();

    @OneToOne(mappedBy = "registro", cascade = CascadeType.ALL)
    private AdendumAyuda adendumAyuda;

    public Registro() {
    }

    public Registro(LocalDate fechaInicio, Sector sector, Entrevistador entrevistador) {
        this.fechaInicio = fechaInicio;
        this.sector = sector;
        this.entrevistador = entrevistador;
    }

    public int getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(int numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaConclusion() {
        return fechaConclusion;
    }

    public void setFechaConclusion(LocalDate fechaConclusion) {
        this.fechaConclusion = fechaConclusion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Entrevistador getEntrevistador() {
        return entrevistador;
    }

    public void setEntrevistador(Entrevistador entrevistador) {
        this.entrevistador = entrevistador;
    }

    public Vivienda getVivienda() {
        return vivienda;
    }

    public void setVivienda(Vivienda vivienda) {
        this.vivienda = vivienda;
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    public AdendumAyuda getAdendumAyuda() {
        return adendumAyuda;
    }

    public void setAdendumAyuda(AdendumAyuda adendumAyuda) {
        this.adendumAyuda = adendumAyuda;
    }
}
