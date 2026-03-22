package main.proyecto_pastoral.Controller.ControllersRegistro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.proyecto_pastoral.DAO.AsistenciaDAO;
import main.proyecto_pastoral.Model.Asistencia;
import main.proyecto_pastoral.Model.Registro;
import main.proyecto_pastoral.Util.HibernateUtil;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class Paso4Controller implements Initializable {

    @FXML private ComboBox<String> cmbTipoAsistencia;
    @FXML private TextField txtModalidad, txtFrecuencia, txtDuracion, txtValor;
    @FXML private TableView<Asistencia>  tablaAsistencias;
    @FXML private TableColumn<Asistencia, String>     colTipo, colModalidad, colFrecuencia, colDuracion;
    @FXML private TableColumn<Asistencia, BigDecimal> colValor;

    private AsistenciaDAO asistenciaDAO;
    private Registro registro;
    private ObservableList<Asistencia> listaAsistencias = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        asistenciaDAO = new AsistenciaDAO(HibernateUtil.getSessionFactory());

        cmbTipoAsistencia.setItems(FXCollections.observableArrayList(
                "Alimentos", "Arts. higiene y limpieza", "Indumentaria",
                "Alquiler", "Servicios (luz, agua, otros)",
                "Medicamentos", "Aparatos ortopédicos",
                "Otros (especifique)"));

        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colModalidad.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        colFrecuencia.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tablaAsistencias.setItems(listaAsistencias);
    }

    public void setRegistro(Registro registro) { this.registro = registro; }

    @FXML
    private void agregarAsistencia() {
        if (cmbTipoAsistencia.getValue() == null) return;

        Asistencia asistencia = new Asistencia();
        asistencia.setRegistro(registro);
        asistencia.setTipo(cmbTipoAsistencia.getValue());
        asistencia.setModalidad(txtModalidad.getText().trim());
        asistencia.setFrecuencia(txtFrecuencia.getText().trim());
        asistencia.setDuracion(txtDuracion.getText().trim());
        if (!txtValor.getText().isEmpty()) {
            asistencia.setValor(new BigDecimal(txtValor.getText().trim()));
        }
        asistenciaDAO.guardar(asistencia);
        listaAsistencias.add(asistencia);

        cmbTipoAsistencia.setValue(null);
        txtModalidad.clear(); txtFrecuencia.clear();
        txtDuracion.clear(); txtValor.clear();
    }

    public int getCantidadAsistencias() { return listaAsistencias.size(); }
}
