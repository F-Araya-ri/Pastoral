package main.proyecto_pastoral.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Sectores")
@NamedQueries({
    @NamedQuery(
        name = "Sector.listarTodos",
        query = "FROM Sector s LEFT JOIN FETCH s.parroquia"
    ),
    @NamedQuery(
        name = "Sector.buscarPorParroquia",
        query = "FROM Sector s WHERE s.parroquia.idParroquia = :id ORDER BY s.nombreSector"
    )
})
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Sector",nullable = false)
    private int idSector;
    @Column(name = "Nombre_Sector", nullable = false,length = 300)
    private String nombreSector;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdParroquia", nullable = false)
    private Parroquia parroquia;
    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Registro> registros = new ArrayList<>();


    public Sector() {
    }

    public Sector( String nombreSector, Parroquia parroquia) {
        this.nombreSector = nombreSector;
        this.parroquia = parroquia;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
    }

    public String getNombreSector() {
        return nombreSector;
    }

    public void setNombreSector(String nombreSector) {
        this.nombreSector = nombreSector;
    }

    public int getIdSector() {
        return idSector;
    }

    public void setIdSector(int idSector) {
        this.idSector = idSector;
    }
}
