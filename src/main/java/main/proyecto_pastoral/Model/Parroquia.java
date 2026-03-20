package main.proyecto_pastoral.Model;


import jakarta.persistence.*;
import main.proyecto_pastoral.Model.Sector;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Parroquia")
public class Parroquia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Parroquia", nullable = false)
    private int idParroquia;
    @Column(name = "Nombre_Parroquia", nullable = false,length = 200)
    private String nombreParroquia;

    @OneToMany(mappedBy = "parroquia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sector> sectores = new ArrayList<>();

    public Parroquia() {
    }

    public Parroquia(String nombreParroquia, List<Sector> sectores) {
        this.nombreParroquia = nombreParroquia;
        this.sectores = sectores;
    }

    public Parroquia(String nombreParroquia) {
        this.nombreParroquia = nombreParroquia;
    }

    public int getIdParroquia() {
        return idParroquia;
    }

    public void setIdParroquia(int idParroquia) {
        this.idParroquia = idParroquia;
    }

    public String getNombreParroquia() {
        return nombreParroquia;
    }

    public void setNombreParroquia(String nombreParroquia) {
        this.nombreParroquia = nombreParroquia;
    }

    public List<Sector> getSectores() {
        return sectores;
    }

    public void setSectores(List<Sector> sectores) {
        this.sectores = sectores;
    }
}
