package main.proyecto_pastoral.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "Vivienda")
public class Vivienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVivienda")
    private Integer idVivienda;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NumeroRegistro", nullable = false)
    private Registro registro;

    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "Tenencia")
    private String tenencia;

    @Column(name = "Condicion")
    private String condicion;
    @Column(name = "Direccion", columnDefinition = "TEXT")
    private String direccion;


    public Vivienda() {}

    public Integer getIdVivienda() { return idVivienda; }
    public void setIdVivienda(Integer idVivienda) { this.idVivienda = idVivienda; }

    public Registro getRegistro() { return registro; }
    public void setRegistro(Registro registro) { this.registro = registro; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTenencia() { return tenencia; }
    public void setTenencia(String tenencia) { this.tenencia = tenencia; }

    public String getCondicion() { return condicion; }
    public void setCondicion(String condicion) { this.condicion = condicion; }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}