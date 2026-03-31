package main.proyecto_pastoral.Controller;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.proyecto_pastoral.App;
import main.proyecto_pastoral.DAO.*;
import main.proyecto_pastoral.Model.*;
import main.proyecto_pastoral.Util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// =====================================================================
// CONTROLADOR DE PANTALLA PRINCIPAL DE REGISTROS
//
// Maneja:
//   1. ComboBox de sector y entrevistador para crear un registro
//   2. Botón "Crear Registro" → guarda en BD y agrega fila al display
//   3. Display tipo lista donde cada fila tiene:
//        - Número de registro
//        - Nombre del jefe de familia (se llena después)
//        - Fecha de inicio y duración
//        - Botón "Completar Ficha"
//   4. Al cerrar el formulario completo, la fila se actualiza
// =====================================================================
public class InicioRegistroController implements Initializable {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @FXML public Button btnRegistroAdmin;
    @FXML public Button btnRegistroParroquia;
    @FXML public Button btnAdendumExpediente;

    // ── ELEMENTOS DE LA PANTALLA ──────────────────────────────────────
    @FXML
    private Button btnRegistroForms;
    @FXML
    private Label lblMensaje;

    // Filtros de búsqueda
    @FXML
    private TextField txtFiltroIdRegistro;
    @FXML
    private ComboBox<Parroquia> cbFiltroParroquia;
    @FXML
    private ComboBox<Entrevistador> cbFiltroEntrevistador;
    @FXML
    private Button btnBuscar;

    // El VBox donde se agregan las filas de registros dinámicamente
    @FXML
    private VBox vboxRegistros;

    // Label que muestra cuántos registros hay
    @FXML
    private Label lblContador;

    // ── DAOs ──────────────────────────────────────────────────────────
    private RegistroDAO registroDAO;
    private PersonaDAO personaDAO;
    private ParroquiaDAO parroquiaDAO;
    private EntrevistadorDAO entrevistadorDAO;

    // Lista observable de registros en pantalla
    private final ObservableList<Registro> registrosEnPantalla =
            FXCollections.observableArrayList();

    // =====================================================================
    // INITIALIZE
    // =====================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SessionFactory sf = HibernateUtil.getSessionFactory();
        registroDAO = new RegistroDAO(sf);
        personaDAO = new PersonaDAO(sf);
        parroquiaDAO = new ParroquiaDAO(sf);
        entrevistadorDAO = new EntrevistadorDAO(sf);

        cargarFiltros();

        // Cargamos los registros abiertos ya existentes en la BD
        cargarRegistrosExistentes();
    }

    // =====================================================================
    // CARGAR REGISTROS EXISTENTES AL ABRIR LA PANTALLA
    // Muestra los registros abiertos que ya estaban en la BD
    // =====================================================================
    private void cargarRegistrosExistentes() {
        try {
            List<Registro> abiertos = registroDAO.buscarAbiertos();
            for (Registro r : abiertos) {
                agregarFilaRegistro(r);
            }
            actualizarContador();
        } catch (Exception e) {
            mostrarMensaje("Error al cargar registros: " + e.getMessage(), true);
        }
    }

    // =====================================================================
    // AGREGAR FILA DE REGISTRO AL DISPLAY
    //
    // Cada fila tiene:
    //   [#XXXX] [Nombre familia / "Pendiente"] [Fecha inicio] [Duración] [Botón]
    //
    // La fila se construye programáticamente para poder actualizarla
    // después cuando el formulario completo se cierre.
    // =====================================================================
    private void agregarFilaRegistro(Registro registro) {

        // ── Contenedor principal de la fila ──────────────────────────────
        HBox fila = new HBox(12);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(14, 18, 14, 18));
        fila.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(93,104,61,0.08), 8, 0, 0, 2);"
        );

        // ── Número de registro (badge azul) ──────────────────────────────
        VBox badgeNumero = new VBox(1);
        badgeNumero.setAlignment(Pos.CENTER);
        badgeNumero.setPadding(new Insets(6, 12, 6, 12));
        badgeNumero.setMinWidth(70);
        badgeNumero.setStyle(
                "-fx-background-color: #5D682D;" +  // Verde oliva pastoral
                        "-fx-background-radius: 7;"
        );
        Label lblBadgeTitulo = new Label("FICHA");
        lblBadgeTitulo.setStyle("-fx-font-size: 8px; -fx-text-fill: #C8D4A0; -fx-font-weight: bold;");
        Label lblBadgeNum = new Label("#" + registro.getNumeroRegistro());
        lblBadgeNum.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        badgeNumero.getChildren().addAll(lblBadgeTitulo, lblBadgeNum);

        // ── Información central (nombre familia + sector) ─────────────────
        VBox infoCenter = new VBox(4);
        HBox.setHgrow(infoCenter, Priority.ALWAYS);

        // Nombre del jefe de familia — se actualiza después
        // Buscamos si ya tiene personas registradas
        String nombreFamilia = obtenerNombreFamilia(registro.getNumeroRegistro());
        Label lblNombreFamilia = new Label(nombreFamilia);
        lblNombreFamilia.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2C3527;"
        );

        Label lblSectorInfo = new Label(
                registro.getSector().getParroquia().getNombreParroquia()
                        + "  ›  " + registro.getSector().getNombreSector()
                        + "   •   " + registro.getEntrevistador().getNombre()
        );
        lblSectorInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #8A9070;");

        infoCenter.getChildren().addAll(lblNombreFamilia, lblSectorInfo);

        // ── Fechas y duración ─────────────────────────────────────────────
        VBox infoDerecha = new VBox(4);
        infoDerecha.setAlignment(Pos.CENTER_RIGHT);
        infoDerecha.setMinWidth(140);

        Label lblFechaInicio = new Label("Inicio: " + registro.getFechaInicio().format(FMT));
        lblFechaInicio.setStyle("-fx-font-size: 11px; -fx-text-fill: #5D682D; -fx-font-weight: bold;");

        // Duración: si ya tiene fecha de conclusión la mostramos, si no "Activo"
        String duracionTexto = calcularDuracion(registro);
        Label lblDuracion = new Label(duracionTexto);
        lblDuracion.setStyle("-fx-font-size: 10px; -fx-text-fill: #8A9070;");

        // Indicador de estado (punto verde = activo, gris = cerrado)
        HBox estadoBadge = new HBox(4);
        estadoBadge.setAlignment(Pos.CENTER_RIGHT);
        boolean activo = registro.getFechaConclusion() == null;
        Label puntoEstado = new Label(activo ? "● Activo" : "○ Cerrado");
        puntoEstado.setStyle(
                "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: "
                        + (activo ? "#5D682D" : "#9E9E9E") + ";"
        );
        estadoBadge.getChildren().add(puntoEstado);

        infoDerecha.getChildren().addAll(lblFechaInicio, lblDuracion, estadoBadge);

        // ── Botón "FICHA" ────────────────────────────────────────
        Button btnCompletar = new Button("FICHA →");
        btnCompletar.setMinWidth(90);
        btnCompletar.setStyle(
                "-fx-background-color: #F0F3E8;" +
                        "-fx-text-fill: #5D682D;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #5D682D;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-alignment: center;"
        );

        // Cuando se hace clic, abre el formulario completo
        // y le pasa la fila entera para poder actualizarla al cerrar
        btnCompletar.setOnAction(e -> abrirFichaCompleta(
                registro, lblNombreFamilia, lblDuracion, puntoEstado, btnCompletar
        ));

        // Si el registro ya está cerrado, deshabilitamos el botón
        if (registro.getFechaConclusion() != null) {
            btnCompletar.setDisable(true);
            btnCompletar.setText("Cerrado");
        }

        fila.getChildren().addAll(badgeNumero, infoCenter, infoDerecha, btnCompletar);

        // ── Animación de entrada ───────────────────────────────────────────
        fila.setOpacity(0);
        vboxRegistros.getChildren().addFirst(fila); // Agrega al inicio (más reciente primero)

        FadeTransition fade = new FadeTransition(Duration.millis(400), fila);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        registrosEnPantalla.add(registro);
    }

    // =====================================================================
    // ABRIR FORMULARIO COMPLETO
    //
    // Le pasa el registro Y las referencias a los Labels de la fila
    // para que al cerrarse, pueda actualizar la información en pantalla.
    // =====================================================================
    private void abrirFichaCompleta(
            Registro registro,
            Label lblNombreFamilia,
            Label lblDuracion,
            Label lblEstado,
            Button btnCompletar) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("Vistas/FichaCompletaForms.fxml")
            );
            Scene scene = new Scene(loader.load(), 920, 700);

            FichaCompletaController controller = loader.getController();
            controller.inicializarConRegistro(registro);

            Stage stage = new Stage();
            stage.setTitle("Ficha de Registro #" + registro.getNumeroRegistro()
                    + " — Pastoral Social Diócesis de Cartago");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(true);
            stage.setMinWidth(920);
            stage.setMinHeight(600);

            // Cuando se cierra la ventana, actualizamos la fila
            stage.setOnHidden(e -> actualizarFilaTrasGuardar(
                    registro, lblNombreFamilia, lblDuracion, lblEstado, btnCompletar
            ));

            stage.show();

        } catch (IOException e) {
            mostrarMensaje("✘ Error al abrir la ficha: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    // =====================================================================
    // ACTUALIZAR FILA TRAS CERRAR EL FORMULARIO
    //
    // Reconsulta el registro en la BD para ver si se llenaron
    // personas y si se cerró. Actualiza los Labels de esa fila.
    // =====================================================================
    private void actualizarFilaTrasGuardar(
            Registro registro,
            Label lblNombreFamilia,
            Label lblDuracion,
            Label lblEstado,
            Button btnCompletar) {

        try {
            // Reconsultamos el registro actualizado de la BD
            registroDAO.buscarPorId(registro.getNumeroRegistro()).ifPresent(actualizado -> {

                // Actualizar nombre de familia (jefe de hogar)
                String nombre = obtenerNombreFamilia(actualizado.getNumeroRegistro());
                lblNombreFamilia.setText(nombre);

                // Actualizar duración
                lblDuracion.setText(calcularDuracion(actualizado));

                // Actualizar estado
                boolean activo = actualizado.getFechaConclusion() == null;
                lblEstado.setText(activo ? "● Activo" : "○ Cerrado");
                lblEstado.setStyle(
                        "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: "
                                + (activo ? "#5D682D" : "#9E9E9E") + ";"
                );

                // Si ya está cerrado, deshabilitamos el botón
                if (!activo) {
                    btnCompletar.setDisable(true);
                    btnCompletar.setText("Cerrado");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================================
    // OBTENER NOMBRE DE LA FAMILIA
    // Busca el jefe de hogar del registro y retorna su nombre.
    // Si no tiene personas aún, retorna "Familia por identificar".
    // =====================================================================
    private String obtenerNombreFamilia(int numeroRegistro) {
        try {
            return personaDAO.buscarJefeFamilia(numeroRegistro)
                    .map(p -> "Familia " + p.getPrimerApellido()
                            + (p.getSegundoApellido() != null ? " " + p.getSegundoApellido() : "")
                            + "   —   " + p.getNombre())
                    .orElse("Familia por identificar");
        } catch (Exception e) {
            return "Familia por identificar";
        }
    }

    // =====================================================================
    // CALCULAR DURACIÓN DEL REGISTRO
    // Si tiene fecha de conclusión, calcula cuánto duró.
    // Si no, dice cuántos días lleva activo.
    // =====================================================================
    private String calcularDuracion(Registro registro) {
        if (registro.getFechaConclusion() != null) {
            long dias = registro.getFechaInicio()
                    .until(registro.getFechaConclusion(), java.time.temporal.ChronoUnit.DAYS);
            return "Duración: " + dias + " días  •  Cerrado: "
                    + registro.getFechaConclusion().format(FMT);
        } else {
            long diasActivo = registro.getFechaInicio()
                    .until(LocalDate.now(), java.time.temporal.ChronoUnit.DAYS);
            return diasActivo == 0
                    ? "Abierto hoy"
                    : "Activo hace " + diasActivo + " día(s)";
        }
    }

    private void actualizarContador() {
        int total = vboxRegistros.getChildren().size();
        lblContador.setText(total + (total == 1 ? " registro" : " registros"));
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError
                ? "-fx-text-fill: #8B3A3A; -fx-font-weight: bold;"
                : "-fx-text-fill: #5D682D; -fx-font-weight: bold;"
        );
    }

    // =====================================================================
    // CARGAR FILTROS DE BÚSQUEDA
    // =====================================================================
    private void cargarFiltros() {
        try {
            // Cargar parroquias
            List<Parroquia> parroquias = parroquiaDAO.listarTodos();
            cbFiltroParroquia.getItems().addAll(parroquias);
            cbFiltroParroquia.setConverter(new javafx.util.StringConverter<Parroquia>() {
                @Override
                public String toString(Parroquia p) {
                    return p != null ? p.getNombreParroquia() : "";
                }

                @Override
                public Parroquia fromString(String s) {
                    return null;
                }
            });

            // Cargar entrevistadores
            List<Entrevistador> entrevistadores = entrevistadorDAO.listarTodos();
            cbFiltroEntrevistador.getItems().addAll(entrevistadores);
            cbFiltroEntrevistador.setConverter(new javafx.util.StringConverter<Entrevistador>() {
                @Override
                public String toString(Entrevistador e) {
                    return e != null ? e.getNombre() : "";
                }

                @Override
                public Entrevistador fromString(String s) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================================
    // BUSCAR REGISTRO POR FILTROS
    // =====================================================================
    @FXML
    public void buscarRegistro() {
        String idStr = txtFiltroIdRegistro.getText().trim();
        Parroquia parroquiaSeleccionada = cbFiltroParroquia.getValue();
        Entrevistador entrevistadorSeleccionado = cbFiltroEntrevistador.getValue();

        vboxRegistros.getChildren().clear();
        List<Registro> registrosFiltrados = new ArrayList<>();

        try {
            // Búsqueda por ID
            if (!idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    registroDAO.buscarPorId(id).ifPresent(registrosFiltrados::add);
                } catch (NumberFormatException e) {
                    mostrarMensaje("ID inválido", true);
                    return;
                }
            }
            // Búsqueda por Parroquia y Entrevistador
            else if (parroquiaSeleccionada != null && entrevistadorSeleccionado != null) {
                List<Registro> todos = registroDAO.buscarPorEntrevistador(entrevistadorSeleccionado.getIdEntrevistador());
                for (Registro r : todos) {
                    if (r.getSector() != null && r.getSector().getParroquia() != null &&
                            r.getSector().getParroquia().getIdParroquia() == parroquiaSeleccionada.getIdParroquia()) {
                        registrosFiltrados.add(r);
                    }
                }
            }
            // Búsqueda por solo Parroquia
            else if (parroquiaSeleccionada != null) {
                List<Registro> todos = registroDAO.listarTodos();
                for (Registro r : todos) {
                    if (r.getSector() != null && r.getSector().getParroquia() != null &&
                            r.getSector().getParroquia().getIdParroquia() == parroquiaSeleccionada.getIdParroquia()) {
                        registrosFiltrados.add(r);
                    }
                }
            }
            // Búsqueda por solo Entrevistador
            else if (entrevistadorSeleccionado != null) {
                registrosFiltrados.addAll(registroDAO.buscarPorEntrevistador(entrevistadorSeleccionado.getIdEntrevistador()));
            }
            // Si no hay filtros, mostrar todos
            else {
                registrosFiltrados.addAll(registroDAO.listarTodos());
            }

            // Mostrar resultados
            for (Registro registro : registrosFiltrados) {
                agregarFilaRegistro(registro);
            }

            if (registrosFiltrados.isEmpty()) {
                mostrarMensaje("No se encontraron registros", false);
            } else {
                mostrarMensaje("Se encontraron " + registrosFiltrados.size() + " registro(s)", false);
            }

        } catch (Exception e) {
            mostrarMensaje("Error en búsqueda: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void CargarVistas(String nombreVista, String infVista) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("Vistas/"+nombreVista));
        Scene registroScene = new Scene(loader.load(), 1000, 700);

        Stage stage = new Stage();
        stage.setTitle(infVista);
        stage.setScene(registroScene);
        stage.show();

    }

    @FXML
    public void mostrarRegistroForms() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("Vistas/Wizard.fxml"));
        Scene registroScene = new Scene(loader.load(), 1000, 700);

        WizardController controller = loader.getController();
        controller.setOnRegistroCreado(registro -> {
            agregarFilaRegistro(registro);
            actualizarContador();
        });

        Stage stage = new Stage();
        stage.setTitle("Formulario Registro");
        stage.setScene(registroScene);
        stage.show();
    }
    @FXML
    public void mostrarRegistroAdmin() throws IOException {
        CargarVistas("Entrevistadorforms.fxml","Nuevo Admistrador");
    }
    @FXML
    public void mostrarRegistroParroquia() throws IOException {
        CargarVistas("ParroquiaSector.fxml","Nueva Parroquia");
    }
    @FXML
    public void mostrarAdendumExpediente() throws IOException {
        CargarVistas("AdendumExpediente.fxml","Adendum al Expediente");
    }

}
