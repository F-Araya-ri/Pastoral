package main.proyecto_pastoral.Controller.ControllersRegistro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.proyecto_pastoral.DAO.RegistroDAO;
import main.proyecto_pastoral.Util.HibernateUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class Paso5CierreController implements Initializable {

    @FXML
    private Label lblResumenRegistro;
    @FXML
    private Label lblResumenVivienda;
    @FXML
    private Label lblResumenPersonas;
    @FXML
    private Label lblResumenAsistencias;


    private Paso1Controller paso1;
    private Paso2Controller paso2;
    private Paso3Controller paso3;
    private Paso4Controller paso4;
    private RegistroDAO registroDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registroDAO = new RegistroDAO(HibernateUtil.getSessionFactory());
    }

    public void setControllers(Paso1Controller p1, Paso2Controller p2,
                               Paso3Controller p3, Paso4Controller p4) {
        this.paso1 = p1;
        this.paso2 = p2;
        this.paso3 = p3;
        this.paso4 = p4;
    }

    public void cargarResumen() {
        if (paso1 != null) {
            lblResumenRegistro.setText(
                    "Parroquia: " + paso1.getParroquiaSeleccionada().getNombreParroquia() +
                            " | Sector: " + paso1.getSectorSeleccionado().getNombreSector() +
                            " | Entrevistador: " + paso1.getEntrevistadorSeleccionado().getNombre() +
                            " | Fecha inicio: " + paso1.getFechaInicio());
        }
        if (paso2 != null) {
            lblResumenVivienda.setText(
                    "Vivienda: " + paso2.getTipo() +
                            " | Tenencia: " + paso2.getTenencia() +
                            " | Condición: " + paso2.getCondicion());
        }
        if (paso3 != null) {
            lblResumenPersonas.setText("Personas registradas: " + paso3.getCantidadPersonas());
        }
        if (paso4 != null) {
            lblResumenAsistencias.setText("Asistencias registradas: " + paso4.getCantidadAsistencias());
        }
    }

    @FXML
    private void guardarFicha() {
        try {
            if (paso1 != null) {
                var registro = paso1.getRegistroGuardado();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ficha guardada");
            alert.setHeaderText(null);
            alert.setContentText("La ficha de registro fue completada exitosamente.");
            alert.showAndWait();

            javafx.stage.Stage stage = (javafx.stage.Stage) lblResumenRegistro.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al finalizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
