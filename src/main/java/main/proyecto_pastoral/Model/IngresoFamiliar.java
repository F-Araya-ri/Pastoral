package main.proyecto_pastoral.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "IngresoFamiliar")
public class IngresoFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdIngreso")
    private Integer idIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdPersona", nullable = false)
    private Persona persona;

    @Column(name = "Ocupacion")
    private String ocupacion;

    @Column(name = "Trabaja")
    private Boolean trabaja;

    @Column(name = "IngresoMensual", precision = 10, scale = 2)
    private BigDecimal ingresoMensual;

    public IngresoFamiliar() {}

    public Integer getIdIngreso() { return idIngreso; }
    public void setIdIngreso(Integer idIngreso) { this.idIngreso = idIngreso; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public Boolean getTrabaja() { return trabaja; }
    public void setTrabaja(Boolean trabaja) { this.trabaja = trabaja; }

    public BigDecimal getIngresoMensual() { return ingresoMensual; }
    public void setIngresoMensual(BigDecimal ingresoMensual) { this.ingresoMensual = ingresoMensual; }
}