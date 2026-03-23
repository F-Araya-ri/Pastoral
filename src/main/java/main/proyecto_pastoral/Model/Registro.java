package main.proyecto_pastoral.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Registro")
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

    @Column(name = "Direccion", columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "Telefono")
    private String telefono;

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

    public Registro() {}

    public Registro(LocalDate fechaInicio, Sector sector, Entrevistador entrevistador) {
        this.fechaInicio = fechaInicio;
        this.sector = sector;
        this.entrevistador = entrevistador;
    }

    public int getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(int numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaConclusion() { return fechaConclusion; }
    public void setFechaConclusion(LocalDate fechaConclusion) { this.fechaConclusion = fechaConclusion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Sector getSector() { return sector; }
    public void setSector(Sector sector) { this.sector = sector; }

    public Entrevistador getEntrevistador() { return entrevistador; }
    public void setEntrevistador(Entrevistador entrevistador) { this.entrevistador = entrevistador; }

    public Vivienda getVivienda() { return vivienda; }
    public void setVivienda(Vivienda vivienda) { this.vivienda = vivienda; }

    public List<Persona> getPersonas() { return personas; }
    public void setPersonas(List<Persona> personas) { this.personas = personas; }

    public List<Asistencia> getAsistencias() { return asistencias; }
    public void setAsistencias(List<Asistencia> asistencias) { this.asistencias = asistencias; }
}