package main.proyecto_pastoral.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.proyecto_pastoral.Controller.ControllersRegistro.*;
import main.proyecto_pastoral.Model.Registro;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class WizardController implements Initializable {

    @FXML private StackPane contenedor;
    @FXML private Button    btnAnterior;
    @FXML private Button    btnSiguiente;
    @FXML private Label     lblMensaje;

    @FXML private VBox  indicador1, indicador2, indicador3, indicador4, indicador5;
    @FXML private Label circulo1,   circulo2,   circulo3,   circulo4,   circulo5;

    private int pasoActual = 1;
    private final int TOTAL_PASOS = 5;

    private Registro registroActual;
    private Consumer<Registro> onRegistroCreado;

    private Paso1Controller paso1Controller;
    private Paso2Controller paso2Controller;
    private Paso3Controller paso3Controller;
    private Paso4Controller paso4Controller;
    private Paso5CierreController paso5Controller;

    // Ajustá esta ruta a la tuya
    private static final String BASE = "/main/Proyecto_Pastoral/Vistas/Vistas_Formulario/";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPaso(1);
        btnAnterior.setDisable(true);
    }

    @FXML
    private void siguiente() {
        if (!validarPasoActual()) return;

        if (pasoActual == 1) {
            registroActual = paso1Controller.guardarYObtenerRegistro();
            if (registroActual == null) {
                mostrarError("Error al guardar el registro.");
                return;
            }
            if (onRegistroCreado != null) {
                onRegistroCreado.accept(registroActual);
                onRegistroCreado = null;
            }
        }

        if (pasoActual < TOTAL_PASOS) {
            pasoActual++;
            cargarPaso(pasoActual);

            // Pasar el registro DESPUÉS de cargar el paso, no antes
            if (pasoActual == 2) paso2Controller.setRegistro(registroActual);
            if (pasoActual == 3) paso3Controller.setRegistro(registroActual);
            if (pasoActual == 4) paso4Controller.setRegistro(registroActual);

            actualizarBotones();
            actualizarIndicadores();
            limpiarMensaje();
        }
    }

    @FXML
    private void anterior() {
        if (pasoActual > 1) {
            pasoActual--;
            cargarPaso(pasoActual);
            actualizarBotones();
            actualizarIndicadores();
            limpiarMensaje();
        }
    }

    @FXML
    private void irAPaso1() {
        if (pasoActual > 1) {
            pasoActual = 1;
            cargarPaso(1);
            actualizarBotones();
            actualizarIndicadores();
        }
    }

    private void cargarPaso(int paso) {
        try {
            String fxml = switch (paso) {
                case 1 -> BASE + "Paso1Registro.fxml";
                case 2 -> BASE + "Paso2Vivienda.fxml";
                case 3 -> BASE + "Paso3Personas.fxml";
                case 4 -> BASE + "Paso4Asistencia.fxml";
                case 5 -> BASE + "Paso5Cierre.fxml";
                default -> throw new IllegalArgumentException("Paso inválido: " + paso);
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node nodo = loader.load();

            switch (paso) {
                case 1 -> paso1Controller = loader.getController();
                case 2 -> paso2Controller = loader.getController();
                case 3 -> paso3Controller = loader.getController();
                case 4 -> paso4Controller = loader.getController();
                case 5 -> {
                    paso5Controller = loader.getController();
                    paso5Controller.setControllers(paso1Controller, paso2Controller,
                            paso3Controller, paso4Controller);
                    paso5Controller.cargarResumen();
                }
            }

            contenedor.getChildren().setAll(nodo);

        } catch (IOException e) {
            mostrarError("Error al cargar el paso " + paso + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarPasoActual() {
        return switch (pasoActual) {
            case 1 -> paso1Controller != null && paso1Controller.validar(this::mostrarError);
            case 2 -> paso2Controller != null && paso2Controller.validar(this::mostrarError);
            case 3 -> paso3Controller != null && paso3Controller.validar(this::mostrarError);
            case 4 -> true;
            default -> true;
        };
    }

    private void actualizarBotones() {
        btnAnterior.setDisable(pasoActual == 1);
        if (pasoActual == TOTAL_PASOS) {
            btnSiguiente.setVisible(false);
        } else {
            btnSiguiente.setVisible(true);
            btnSiguiente.setText("Siguiente");
        }
    }

    private void actualizarIndicadores() {
        Label[] circulos = {circulo1, circulo2, circulo3, circulo4, circulo5};
        for (int i = 0; i < circulos.length; i++) {
            boolean activo     = (i + 1) == pasoActual;
            boolean completado = (i + 1) < pasoActual;
            String color = completado ? "#27ae60" : activo ? "#2980b9" : "#5d6d7e";
            circulos[i].setStyle(circulos[i].getStyle()
                    .replaceAll("-fx-background-color: [^;]+;",
                            "-fx-background-color: " + color + ";"));
        }
    }

    public void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    public void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
    }

    private void limpiarMensaje() { lblMensaje.setText(""); }

    public Registro getRegistroActual() { return registroActual; }

    public void setOnRegistroCreado(Consumer<Registro> onRegistroCreado) {
        this.onRegistroCreado = onRegistroCreado;
    }
}
