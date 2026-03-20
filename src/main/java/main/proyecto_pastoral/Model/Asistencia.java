package main.proyecto_pastoral.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAsistencia")
    private Integer idAsistencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NumeroRegistro", nullable = false)
    private Registro registro;

    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "Modalidad")
    private String modalidad;

    @Column(name = "Frecuencia")
    private String frecuencia;

    @Column(name = "Duracion")
    private String duracion;

    @Column(name = "Valor", precision = 10, scale = 2)
    private BigDecimal valor;

    public Asistencia() {}

    public Integer getIdAsistencia() { return idAsistencia; }
    public void setIdAsistencia(Integer idAsistencia) { this.idAsistencia = idAsistencia; }

    public Registro getRegistro() { return registro; }
    public void setRegistro(Registro registro) { this.registro = registro; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}