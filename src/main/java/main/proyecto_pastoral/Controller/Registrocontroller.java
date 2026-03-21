package main.proyecto_pastoral.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import main.proyecto_pastoral.DAO.*;
import main.proyecto_pastoral.Model.*;
import main.proyecto_pastoral.Util.HibernateUtil;
import org.hibernate.SessionFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class Registrocontroller implements Initializable {

    @FXML
    private AnchorPane rootPane;
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
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnLimpiar;


    private ParroquiaDAO parroquiaDAO;
    private SectorDAO sectorDAO;
    private EntrevistadorDAO entrevistadorDAO;
    private RegistroDAO registroDAO;
    private LocalDate fechaActual;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // 1. Validar inyección FXML
        validarComponentesFXML();

        // 2. Conexión a la base de datos
        SessionFactory sf = HibernateUtil.getSessionFactory();
        parroquiaDAO     = new ParroquiaDAO(sf);
        sectorDAO        = new SectorDAO(sf);
        entrevistadorDAO = new EntrevistadorDAO(sf);
        registroDAO      = new RegistroDAO(sf);

        // 3. Fecha del sistema
        fechaActual = LocalDate.now();
        String fechaTexto = fechaActual.getDayOfMonth() + "/" +
                fechaActual.getMonthValue() + "/" +
                fechaActual.getYear();
        lblFechaInicio.setText(fechaTexto);

        configurarCeldas();
        cargarParroquias();
        cargarEntrevistadores();
        cbSector.setDisable(true);
        cbParroquia.getSelectionModel().selectedItemProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    if (valorNuevo != null) {
                        cargarSectoresPorParroquia(valorNuevo.getIdParroquia());
                        cbSector.setDisable(false);
                    } else {
                        cbSector.getItems().clear();
                        cbSector.setDisable(true);
                    }
                }
        );

    }

    private void validarComponentesFXML() {
        if (cbParroquia == null) throw new IllegalStateException("cbParroquia no inyectado");
        if (cbSector == null) throw new IllegalStateException("cbSector no inyectado");
        if (cbEntrevistador == null) throw new IllegalStateException("cbEntrevistador no inyectado");
        if (lblFechaInicio == null) throw new IllegalStateException("lblFechaInicio no inyectado");
        if (txtObservaciones == null) throw new IllegalStateException("txtObservaciones no inyectado");
        if (lblMensaje == null) throw new IllegalStateException("lblMensaje no inyectado");
        if (btnGuardar == null) throw new IllegalStateException("btnGuardar no inyectado");
        if (btnLimpiar == null) throw new IllegalStateException("btnLimpiar no inyectado");
    }
    private void configurarCeldas() {

        // ── PARROQUIA ──────────────────────────────────────────────────────
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

        // ── SECTOR ──────────────────────────────────────────────────────
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



    private void cargarParroquias() {
        try {
            List<Parroquia> listaParroquias = parroquiaDAO.listarTodos();

            if (listaParroquias.isEmpty()) {
                mostrarMensaje(" No hay parroquias registradas en el sistema.", true);
                cbParroquia.setDisable(true);
                return;
            }

            cbParroquia.setItems(FXCollections.observableArrayList(listaParroquias));

        } catch (Exception e) {
            mostrarMensaje(" Error al cargar parroquias: " + e.getMessage(), true);

            e.printStackTrace();
        }
    }

    private void cargarSectoresPorParroquia(int idParroquia) {
        try {
            // Limpiamos el sector anterior antes de cargar los nuevos
            cbSector.getSelectionModel().clearSelection();
            cbSector.getItems().clear();

            List<Sector> lista = sectorDAO.buscarPorParroquia(idParroquia);

            if (lista.isEmpty()) {
                mostrarMensaje("⚠ Esta parroquia no tiene sectores registrados.", true);
                cbSector.setDisable(true);
                return;
            }

            cbSector.setItems(FXCollections.observableArrayList(lista));
            lblMensaje.setText(""); // Limpiamos cualquier aviso previo

        } catch (Exception e) {
            mostrarMensaje("✘ Error al cargar sectores: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }


    private void cargarEntrevistadores() {
        try {
            // listarTodos() viene de EntrevistadorDAO → hace "FROM Entrevistador" en la BD
            List<Entrevistador> lista = entrevistadorDAO.listarTodos();
            if (lista.isEmpty()) {
                mostrarMensaje("No hay entrevistadores registrados en el sistema.", true);
                cbEntrevistador.setDisable(true);
                return;
            }

            // FXCollections.observableArrayList convierte List<Entrevistador>
            // en el tipo especial que JavaFX necesita para mostrar en pantalla
            cbEntrevistador.setItems(FXCollections.observableArrayList(lista));

        } catch (Exception e) {
            mostrarMensaje(" Error al cargar entrevistadores: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarRegistro() {

        // ── VALIDACIONES (reglas de negocio) ──────────────────────────────
        if (cbParroquia.getValue() == null) {
            mostrarMensaje(" Debe seleccionar una Parroquia.", true);
            cbParroquia.requestFocus();
            return; // Detenemos el método, no seguimos
        }

        if (cbSector.getValue() == null) {
            mostrarMensaje(" Debe seleccionar un Sector.", true);
            cbSector.requestFocus();
            return;
        }

        if (cbEntrevistador.getValue() == null) {
            mostrarMensaje("Debe seleccionar un Entrevistador.", true);
            cbEntrevistador.requestFocus();
            return;
        }

        Sector        sectorElegido        = cbSector.getValue();
        Entrevistador entrevistadorElegido = cbEntrevistador.getValue();
        String        observaciones        = txtObservaciones.getText().trim();

        Registro nuevoRegistro = new Registro();
        nuevoRegistro.setFechaInicio(LocalDate.now());
        nuevoRegistro.setSector(sectorElegido);
        nuevoRegistro.setEntrevistador(entrevistadorElegido);

        if (!observaciones.isEmpty()) {
            nuevoRegistro.setObservaciones(observaciones);
        }

        // ── GUARDAR EN LA BASE DE DATOS ───────────────────────────────────
        try {
            int numeroRegistroGuardado = registroDAO.guardarYRetornarId(nuevoRegistro);

            // Si llegamos aquí sin excepción, el guardado fue exitoso.
            // Mostramos el número que la BD le asignó al registro.
            mostrarMensaje(
                    "✔ Registro #" + numeroRegistroGuardado +
                            " guardado exitosamente — " +
                            String.format("%02d/%02d/%d",
                                    fechaActual.getDayOfMonth(),
                                    fechaActual.getMonthValue(),
                                    fechaActual.getYear()),
                    false
            );

            limpiarFormulario(); // Dejamos la pantalla lista para el siguiente registro

        } catch (Exception e) {
            mostrarMensaje("✘ Error al guardar el registro: " + e.getMessage(), true);
            e.printStackTrace(); // Imprime el detalle del error en la consola de IntelliJ
        }
    }


    @FXML
    private void limpiarFormulario() {
        cbParroquia.getSelectionModel().clearSelection();

        cbSector.getSelectionModel().clearSelection();
        cbSector.getItems().clear();
        cbSector.setDisable(true);
        cbEntrevistador.getSelectionModel().clearSelection();

        txtObservaciones.clear();
        lblMensaje.setText("");
    }

    public Optional<Registro> buscarRegistroPorId(int id) {
        return registroDAO.buscarPorId(id);
    }
    public List<Registro> obtenerRegistrosAbiertos() {
        return registroDAO.buscarAbiertos();
    }

    public void cerrarRegistro(int numeroRegistro) {
        registroDAO.buscarPorId(numeroRegistro).ifPresentOrElse(
                registro -> {
                    if (registro.getFechaConclusion() != null) {
                        mostrarMensaje("⚠ El registro #" + numeroRegistro + " ya está cerrado.", true);
                        return;
                    }

                    registro.setFechaConclusion(LocalDate.now());
                    registroDAO.actualizar(registro);
                    mostrarMensaje("✔ Registro #" + numeroRegistro + " cerrado exitosamente.", false);
                },
                () -> mostrarMensaje("✘ No se encontró el registro #" + numeroRegistro + ".", true)
        );
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError
                ? "-fx-text-fill: #c0392b; -fx-font-weight: bold;"
                : "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
        );
    }
}
