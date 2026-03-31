package main.proyecto_pastoral.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AdendumAyuda")
public class AdendumAyuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAdendum")
    private Integer idAdendum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NumeroRegistro", nullable = false, unique = true)
    private Registro registro;

    @Column(name = "DescripcionAdicional", length = 255)
    private String descripcionAdicional;

    @Column(name = "ElectricidadMonto", length = 120)
    private String electricidadMonto;
    @Column(name = "ElectricidadFecha", length = 80)
    private String electricidadFecha;
    @Column(name = "ElectricidadObs", length = 255)
    private String electricidadObservacion;

    @Column(name = "AguaMonto", length = 120)
    private String aguaMonto;
    @Column(name = "AguaFecha", length = 80)
    private String aguaFecha;
    @Column(name = "AguaObs", length = 255)
    private String aguaObservacion;

    @Column(name = "TelefonoMonto", length = 120)
    private String telefonoMonto;
    @Column(name = "TelefonoFecha", length = 80)
    private String telefonoFecha;
    @Column(name = "TelefonoObs", length = 255)
    private String telefonoObservacion;

    @Column(name = "InternetMonto", length = 120)
    private String internetMonto;
    @Column(name = "InternetFecha", length = 80)
    private String internetFecha;
    @Column(name = "InternetObs", length = 255)
    private String internetObservacion;

    @Column(name = "CableMonto", length = 120)
    private String cableMonto;
    @Column(name = "CableFecha", length = 80)
    private String cableFecha;
    @Column(name = "CableObs", length = 255)
    private String cableObservacion;

    @Column(name = "AlquilerPrestamo", length = 255)
    private String alquilerPrestamo;

    @Column(name = "Alimentacion", length = 255)
    private String alimentacion;

    @Column(name = "MedicamentoMensual", length = 255)
    private String medicamentoMensual;

    @Column(name = "OtroGastoUno", length = 255)
    private String otroGastoUno;

    @Column(name = "OtroGastoDos", length = 255)
    private String otroGastoDos;

    @Column(name = "Observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "RecomendacionUno", length = 5)
    private String recomendacionUno;
    @Column(name = "FechaRecomendacionUno", length = 80)
    private String fechaRecomendacionUno;

    @Column(name = "RecomendacionDos", length = 5)
    private String recomendacionDos;
    @Column(name = "FechaRecomendacionDos", length = 80)
    private String fechaRecomendacionDos;

    @Column(name = "RecomendacionTres", length = 5)
    private String recomendacionTres;
    @Column(name = "FechaRecomendacionTres", length = 80)
    private String fechaRecomendacionTres;

    @Column(name = "ActualizadoEn")
    private LocalDateTime actualizadoEn;

    public Integer getIdAdendum() { return idAdendum; }
    public void setIdAdendum(Integer idAdendum) { this.idAdendum = idAdendum; }

    public Registro getRegistro() { return registro; }
    public void setRegistro(Registro registro) { this.registro = registro; }

    public String getDescripcionAdicional() { return descripcionAdicional; }
    public void setDescripcionAdicional(String descripcionAdicional) { this.descripcionAdicional = descripcionAdicional; }

    public String getElectricidadMonto() { return electricidadMonto; }
    public void setElectricidadMonto(String electricidadMonto) { this.electricidadMonto = electricidadMonto; }
    public String getElectricidadFecha() { return electricidadFecha; }
    public void setElectricidadFecha(String electricidadFecha) { this.electricidadFecha = electricidadFecha; }
    public String getElectricidadObservacion() { return electricidadObservacion; }
    public void setElectricidadObservacion(String electricidadObservacion) { this.electricidadObservacion = electricidadObservacion; }

    public String getAguaMonto() { return aguaMonto; }
    public void setAguaMonto(String aguaMonto) { this.aguaMonto = aguaMonto; }
    public String getAguaFecha() { return aguaFecha; }
    public void setAguaFecha(String aguaFecha) { this.aguaFecha = aguaFecha; }
    public String getAguaObservacion() { return aguaObservacion; }
    public void setAguaObservacion(String aguaObservacion) { this.aguaObservacion = aguaObservacion; }

    public String getTelefonoMonto() { return telefonoMonto; }
    public void setTelefonoMonto(String telefonoMonto) { this.telefonoMonto = telefonoMonto; }
    public String getTelefonoFecha() { return telefonoFecha; }
    public void setTelefonoFecha(String telefonoFecha) { this.telefonoFecha = telefonoFecha; }
    public String getTelefonoObservacion() { return telefonoObservacion; }
    public void setTelefonoObservacion(String telefonoObservacion) { this.telefonoObservacion = telefonoObservacion; }

    public String getInternetMonto() { return internetMonto; }
    public void setInternetMonto(String internetMonto) { this.internetMonto = internetMonto; }
    public String getInternetFecha() { return internetFecha; }
    public void setInternetFecha(String internetFecha) { this.internetFecha = internetFecha; }
    public String getInternetObservacion() { return internetObservacion; }
    public void setInternetObservacion(String internetObservacion) { this.internetObservacion = internetObservacion; }

    public String getCableMonto() { return cableMonto; }
    public void setCableMonto(String cableMonto) { this.cableMonto = cableMonto; }
    public String getCableFecha() { return cableFecha; }
    public void setCableFecha(String cableFecha) { this.cableFecha = cableFecha; }
    public String getCableObservacion() { return cableObservacion; }
    public void setCableObservacion(String cableObservacion) { this.cableObservacion = cableObservacion; }

    public String getAlquilerPrestamo() { return alquilerPrestamo; }
    public void setAlquilerPrestamo(String alquilerPrestamo) { this.alquilerPrestamo = alquilerPrestamo; }
    public String getAlimentacion() { return alimentacion; }
    public void setAlimentacion(String alimentacion) { this.alimentacion = alimentacion; }
    public String getMedicamentoMensual() { return medicamentoMensual; }
    public void setMedicamentoMensual(String medicamentoMensual) { this.medicamentoMensual = medicamentoMensual; }
    public String getOtroGastoUno() { return otroGastoUno; }
    public void setOtroGastoUno(String otroGastoUno) { this.otroGastoUno = otroGastoUno; }
    public String getOtroGastoDos() { return otroGastoDos; }
    public void setOtroGastoDos(String otroGastoDos) { this.otroGastoDos = otroGastoDos; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getRecomendacionUno() { return recomendacionUno; }
    public void setRecomendacionUno(String recomendacionUno) { this.recomendacionUno = recomendacionUno; }
    public String getFechaRecomendacionUno() { return fechaRecomendacionUno; }
    public void setFechaRecomendacionUno(String fechaRecomendacionUno) { this.fechaRecomendacionUno = fechaRecomendacionUno; }
    public String getRecomendacionDos() { return recomendacionDos; }
    public void setRecomendacionDos(String recomendacionDos) { this.recomendacionDos = recomendacionDos; }
    public String getFechaRecomendacionDos() { return fechaRecomendacionDos; }
    public void setFechaRecomendacionDos(String fechaRecomendacionDos) { this.fechaRecomendacionDos = fechaRecomendacionDos; }
    public String getRecomendacionTres() { return recomendacionTres; }
    public void setRecomendacionTres(String recomendacionTres) { this.recomendacionTres = recomendacionTres; }
    public String getFechaRecomendacionTres() { return fechaRecomendacionTres; }
    public void setFechaRecomendacionTres(String fechaRecomendacionTres) { this.fechaRecomendacionTres = fechaRecomendacionTres; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
