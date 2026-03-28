package main.proyecto_pastoral.Controller.ControllersRegistro;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.proyecto_pastoral.DAO.*;
import main.proyecto_pastoral.Model.*;
import main.proyecto_pastoral.Util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Paso1Controller implements Initializable {

    @FXML
    private ComboBox<Parroquia> cbParroquia;
    @FXML
    private ComboBox<Sector> cbSector;
    @FXML
    private ComboBox<Entrevistador> cbEntrevistador;
    @FXML
    private Label lblFechaInicio;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Label lblMensaje;

    private ParroquiaDAO parroquiaDAO;
    private SectorDAO sectorDAO;
    private EntrevistadorDAO entrevistadorDAO;
    private RegistroDAO registroDAO;
    private LocalDate fechaActual;
    private Registro registroGuardado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        parroquiaDAO = new ParroquiaDAO(sf);
        sectorDAO = new SectorDAO(sf);
        entrevistadorDAO = new EntrevistadorDAO(sf);
        registroDAO = new RegistroDAO(sf);

        // Fecha automática del sistema
        fechaActual = LocalDate.now();
        lblFechaInicio.setText(String.format("%02d/%02d/%d",
                fechaActual.getDayOfMonth(),
                fechaActual.getMonthValue(),
                fechaActual.getYear()));

        configurarCeldas();
        cargarParroquias();
        cargarEntrevistadores();

        // Sector deshabilitado hasta que se elija parroquia
        cbSector.setDisable(true);
        cbParroquia.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, nuevo) -> {
                    if (nuevo != null) {
                        cargarSectoresPorParroquia(nuevo.getIdParroquia());
                        cbSector.setDisable(false);
                    } else {
                        cbSector.getItems().clear();
                        cbSector.setDisable(true);
                    }
                });
    }

    //CONFIGURACIÓN DE CELDAS

    private void configurarCeldas() {
        cbParroquia.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Parroquia p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNombreParroquia());
            }
        });
        cbParroquia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Parroquia p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? "-- Seleccione Parroquia --" : p.getNombreParroquia());
            }
        });

        cbSector.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Sector s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? null : s.getNombreSector());
            }
        });
        cbSector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Sector s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? "-- Seleccione Sector --" : s.getNombreSector());
            }
        });

        cbEntrevistador.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Entrevistador e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? null : e.getNombre());
            }
        });
        cbEntrevistador.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Entrevistador e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? "-- Seleccione Entrevistador --" : e.getNombre());
            }
        });
    }

    // CARGA DE DATOS
    private void cargarParroquias() {
        try {
            List<Parroquia> lista = parroquiaDAO.listarTodos();
            if (lista.isEmpty()) {
                mostrarMensaje("No hay parroquias registradas en el sistema.", true);
                cbParroquia.setDisable(true);
                return;
            }
            cbParroquia.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            mostrarMensaje("Error al cargar parroquias: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void cargarSectoresPorParroquia(int idParroquia) {
        try {
            cbSector.getSelectionModel().clearSelection();
            cbSector.getItems().clear();
            List<Sector> lista = sectorDAO.buscarPorParroquia(idParroquia);
            if (lista.isEmpty()) {
                mostrarMensaje("Esta parroquia no tiene sectores registrados.", true);
                cbSector.setDisable(true);
                return;
            }
            cbSector.setItems(FXCollections.observableArrayList(lista));
            lblMensaje.setText("");
        } catch (Exception e) {
            mostrarMensaje("Error al cargar sectores: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void cargarEntrevistadores() {
        try {
            List<Entrevistador> lista = entrevistadorDAO.listarTodos();
            if (lista.isEmpty()) {
                mostrarMensaje("No hay entrevistadores registrados en el sistema.", true);
                cbEntrevistador.setDisable(true);
                return;
            }
            cbEntrevistador.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            mostrarMensaje("Error al cargar entrevistadores: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    //              VALIDACIÓN Y GUARDADO

    public boolean validar(Consumer<String> mostrarError) {
        if (cbParroquia.getValue() == null) {
            mostrarError.accept("Debe seleccionar una parroquia.");
            cbParroquia.requestFocus();
            return false;
        }
        if (cbSector.getValue() == null) {
            mostrarError.accept("Debe seleccionar un sector.");
            cbSector.requestFocus();
            return false;
        }
        if (cbEntrevistador.getValue() == null) {
            mostrarError.accept("Debe seleccionar un entrevistador.");
            cbEntrevistador.requestFocus();
            return false;
        }
        return true;
    }

    public Registro guardarYObtenerRegistro() {
        try {
            Registro registro = new Registro();
            registro.setFechaInicio(fechaActual);
            registro.setSector(cbSector.getValue());
            registro.setEntrevistador(cbEntrevistador.getValue());
            String observaciones = txtObservaciones.getText().trim();
            if (!observaciones.isEmpty()) {
                registro.setObservaciones(observaciones);
            }
            registroDAO.guardar(registro);
            this.registroGuardado = registro;
            return registro;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //GETTERS PARA EL RESUMEN

    public Parroquia getParroquiaSeleccionada() {
        return cbParroquia.getValue();
    }

    public Sector getSectorSeleccionado() {
        return cbSector.getValue();
    }

    public Entrevistador getEntrevistadorSeleccionado() {
        return cbEntrevistador.getValue();
    }

    public LocalDate getFechaInicio() {
        return fechaActual;
    }

    public String getDireccion() {
        return txtObservaciones.getText();
    }


    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError
                ? "-fx-text-fill: #c0392b; -fx-font-weight: bold;"
                : "-fx-text-fill: #27ae60; -fx-font-weight: bold;");
    }


    public Registro getRegistroGuardado() {
        return registroGuardado;
    }
}
