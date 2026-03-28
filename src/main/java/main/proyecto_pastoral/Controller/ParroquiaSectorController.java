package main.proyecto_pastoral.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.proyecto_pastoral.App;
import main.proyecto_pastoral.DAO.ParroquiaDAO;
import main.proyecto_pastoral.DAO.SectorDAO;
import main.proyecto_pastoral.Util.HibernateUtil;
import main.proyecto_pastoral.Model.Parroquia;
import main.proyecto_pastoral.Model.Sector;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ParroquiaSectorController implements Initializable {

    @FXML private Button btnRegistroForms;
    @FXML private Label lblMensaje;
    @FXML private TextField txtNombreParroquia;
    @FXML private TableView<Parroquia> tablaParroquias;
    @FXML private TableColumn<Parroquia, Integer> colIdParroquia;
    @FXML private TableColumn<Parroquia, String> colNombreParroquia;


    @FXML
    private ComboBox<Parroquia> cmbParroquia;
    @FXML
    private TextField txtNombreSector;
    @FXML
    private TableView<Sector> tablaSectores;
    @FXML
    private TableColumn<Sector, Integer> colIdSector;
    @FXML
    private TableColumn<Sector, String> colNombreSector;
    @FXML
    private TableColumn<Sector, String> colParroquiaSector;


    private ParroquiaDAO parroquiaDAO;
    private SectorDAO sectorDAO;


    private ObservableList<Parroquia> listaParroquias = FXCollections.observableArrayList();
    private ObservableList<Sector> listaSectores = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        parroquiaDAO = new ParroquiaDAO(sf);
        sectorDAO = new SectorDAO(sf);

        configurarTablaParroquias();
        configurarTablaSectores();
        cargarParroquias();
        cargarSectores();
    }

    private void configurarTablaParroquias() {
        colIdParroquia.setCellValueFactory(new PropertyValueFactory<>("idParroquia"));
        colNombreParroquia.setCellValueFactory(new PropertyValueFactory<>("nombreParroquia"));
        tablaParroquias.setItems(listaParroquias);
    }

    private void cargarParroquias() {
        listaParroquias.setAll(parroquiaDAO.listarTodos());
        cmbParroquia.setItems(listaParroquias);
    }

    @FXML
    private void guardarParroquia() {
        // Validación
        String nombre = txtNombreParroquia.getText().trim();
        if (nombre.isEmpty()) {
            mostrarError("El nombre de la parroquia es obligatorio.");
            return;
        }

        try {
            Parroquia parroquia = new Parroquia(nombre);
            parroquiaDAO.guardar(parroquia);
            cargarParroquias();
            limpiarParroquia();
            mostrarExito("Parroquia guardada correctamente.");
        } catch (Exception e) {
            mostrarError("Error al guardar la parroquia: " + e.getMessage());
        }
    }

    @FXML
    private void limpiarParroquia() {
        txtNombreParroquia.clear();
        lblMensaje.setText("");
    }


    private void configurarTablaSectores() {
        colIdSector.setCellValueFactory(new PropertyValueFactory<>("idSector"));
        colNombreSector.setCellValueFactory(new PropertyValueFactory<>("nombreSector"));


        colParroquiaSector.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getParroquia().getNombreParroquia()
                )
        );

        tablaSectores.setItems(listaSectores);


        cmbParroquia.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Parroquia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreParroquia());
            }
        });
        cmbParroquia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Parroquia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreParroquia());
            }
        });
    }

    private void cargarSectores() {
        listaSectores.setAll(sectorDAO.listarTodos());
    }

    @FXML
    private void guardarSector() {

        Parroquia parroquiaSeleccionada = cmbParroquia.getValue();
        String nombre = txtNombreSector.getText().trim();

        if (parroquiaSeleccionada == null) {
            mostrarError("Debe seleccionar una parroquia.");
            return;
        }
        if (nombre.isEmpty()) {
            mostrarError("El nombre del sector es obligatorio.");
            return;
        }

        try {
            Sector sector = new Sector(nombre, parroquiaSeleccionada);
            sectorDAO.guardar(sector);
            cargarSectores();
            limpiarSector();
            mostrarExito("Sector guardado correctamente.");
        } catch (Exception e) {
            mostrarError("Error al guardar el sector: " + e.getMessage());
        }
    }

    @FXML
    private void limpiarSector() {
        cmbParroquia.setValue(null);
        txtNombreSector.clear();
        lblMensaje.setText("");
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 13px; -fx-font-weight: bold;");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px; -fx-font-weight: bold;");
    }
    //---------------------------------------------------------------------------
    // CREANDO BOTON PARA ENTRAR AL INICIOREGISTRO y CREAR USUARIO CREADO POR FABIANA
    //------------------------------------------------------------------------


    @FXML
    private void abrirInicioRegistro() throws IOException {
        javafx.stage.Stage stage = (javafx.stage.Stage) btnRegistroForms .getScene().getWindow();
        stage.close();
    }
}
