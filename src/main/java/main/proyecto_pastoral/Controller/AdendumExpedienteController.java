package main.proyecto_pastoral.Controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.proyecto_pastoral.DAO.AdendumAyudaDAO;
import main.proyecto_pastoral.DAO.RegistroDAO;
import main.proyecto_pastoral.Model.AdendumAyuda;
import main.proyecto_pastoral.Model.Registro;
import main.proyecto_pastoral.Util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdendumExpedienteController implements Initializable {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML private Label lblEstado;
    @FXML private TextField txtNumeroRegistro;
    @FXML private TextField txtDescripcionAdicional;
    @FXML private VBox contenedorFormulario;
    @FXML private TextField txtElectricidadMonto;
    @FXML private TextField txtElectricidadFecha;
    @FXML private TextField txtElectricidadDetalle;
    @FXML private TextField txtAguaMonto;
    @FXML private TextField txtAguaFecha;
    @FXML private TextField txtAguaDetalle;
    @FXML private TextField txtTelefonoMonto;
    @FXML private TextField txtTelefonoFecha;
    @FXML private TextField txtTelefonoDetalle;
    @FXML private TextField txtInternetMonto;
    @FXML private TextField txtInternetFecha;
    @FXML private TextField txtInternetDetalle;
    @FXML private TextField txtCableMonto;
    @FXML private TextField txtCableFecha;
    @FXML private TextField txtCableDetalle;
    @FXML private TextField txtAlquilerPrestamo;
    @FXML private TextField txtAlimentacion;
    @FXML private TextField txtMedicamentoMensual;
    @FXML private TextField txtOtroGastoUno;
    @FXML private TextField txtOtroGastoDos;
    @FXML private TextArea txtObservaciones;
    @FXML private RadioButton rbRec1Si;
    @FXML private RadioButton rbRec1No;
    @FXML private RadioButton rbRec2Si;
    @FXML private RadioButton rbRec2No;
    @FXML private RadioButton rbRec3Si;
    @FXML private RadioButton rbRec3No;
    @FXML private TextField txtFechaRec1;
    @FXML private TextField txtFechaRec2;
    @FXML private TextField txtFechaRec3;

    private RegistroDAO registroDAO;
    private AdendumAyudaDAO adendumAyudaDAO;
    private final PauseTransition pausaGuardado = new PauseTransition(Duration.millis(900));

    private Registro registroActual;
    private AdendumAyuda adendumActual;
    private boolean cargandoDatos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        registroDAO = new RegistroDAO(sf);
        adendumAyudaDAO = new AdendumAyudaDAO(sf);
        contenedorFormulario.setDisable(true);
        pausaGuardado.setOnFinished(_ -> guardarAutomaticamente());


        datePikerFechas(txtElectricidadFecha);
        datePikerFechas(txtAguaFecha);
        datePikerFechas(txtTelefonoFecha);
        datePikerFechas(txtInternetFecha);
        datePikerFechas(txtCableFecha);
        datePikerFechas(txtFechaRec1);
        datePikerFechas(txtFechaRec2);
        datePikerFechas(txtFechaRec3);

        registrarAutoGuardado();
    }


    private void registrarAutoGuardado() {
        registrarCambioTexto(
                txtDescripcionAdicional, txtElectricidadMonto, txtElectricidadFecha, txtElectricidadDetalle,
                txtAguaMonto, txtAguaFecha, txtAguaDetalle, txtTelefonoMonto, txtTelefonoFecha, txtTelefonoDetalle,
                txtInternetMonto, txtInternetFecha, txtInternetDetalle, txtCableMonto, txtCableFecha, txtCableDetalle,
                txtAlquilerPrestamo, txtAlimentacion, txtMedicamentoMensual, txtOtroGastoUno, txtOtroGastoDos,
                txtFechaRec1, txtFechaRec2, txtFechaRec3
        );

        txtObservaciones.textProperty().addListener((_, _, _) -> programarGuardado());
        rbRec1Si.selectedProperty().addListener((_, _, _) -> programarGuardado());
        rbRec1No.selectedProperty().addListener((_, _, _) -> programarGuardado());
        rbRec2Si.selectedProperty().addListener((_, _, _) -> programarGuardado());
        rbRec2No.selectedProperty().addListener((_, _, _) -> programarGuardado());
        rbRec3Si.selectedProperty().addListener((_, _, _) -> programarGuardado());
        rbRec3No.selectedProperty().addListener((_, _, _) -> programarGuardado());
    }

    private void registrarCambioTexto(TextInputControl... controles) {
        for (TextInputControl control : controles) {
            control.textProperty().addListener((_, _, _) -> programarGuardado());
        }
    }

    private void programarGuardado() {
        if (cargandoDatos || registroActual == null) {
            return;
        }
        pausaGuardado.playFromStart();
        mostrarEstado("Cambios detectados. Guardando...", false);
    }

    @FXML
    private void cargarRegistro() {
        String numeroTexto = txtNumeroRegistro.getText().trim();
        if (numeroTexto.isEmpty()) {
            mostrarEstado("Ingrese un numero de registro para cargar el adendum.", true);
            return;
        }

        int numeroRegistro;
        try {
            numeroRegistro = Integer.parseInt(numeroTexto);
        } catch (NumberFormatException e) {
            mostrarEstado("El numero de registro debe ser numerico.", true);
            return;
        }

        registroDAO.buscarPorId(numeroRegistro).ifPresentOrElse(registro -> {
            registroActual = registro;
            adendumActual = adendumAyudaDAO.buscarPorRegistro(numeroRegistro).orElseGet(() -> {
                AdendumAyuda nuevo = new AdendumAyuda();
                nuevo.setRegistro(registro);
                return nuevo;
            });
            poblarFormulario(adendumActual);
            contenedorFormulario.setDisable(false);
            mostrarEstado("Registro #" + numeroRegistro + " cargado. El formulario se guarda automaticamente.", false);
        }, () -> {
            registroActual = null;
            adendumActual = null;
            contenedorFormulario.setDisable(true);
            limpiarFormularioInterno();
            mostrarEstado("No existe un registro con ese numero.", true);
        });
    }

    private void poblarFormulario(AdendumAyuda adendum) {
        cargandoDatos = true;
        txtDescripcionAdicional.setText(valor(adendum.getDescripcionAdicional()));
        txtElectricidadMonto.setText(valor(adendum.getElectricidadMonto()));
        txtElectricidadFecha.setText(valor(adendum.getElectricidadFecha()));
        txtElectricidadDetalle.setText(valor(adendum.getElectricidadObservacion()));
        txtAguaMonto.setText(valor(adendum.getAguaMonto()));
        txtAguaFecha.setText(valor(adendum.getAguaFecha()));
        txtAguaDetalle.setText(valor(adendum.getAguaObservacion()));
        txtTelefonoMonto.setText(valor(adendum.getTelefonoMonto()));
        txtTelefonoFecha.setText(valor(adendum.getTelefonoFecha()));
        txtTelefonoDetalle.setText(valor(adendum.getTelefonoObservacion()));
        txtInternetMonto.setText(valor(adendum.getInternetMonto()));
        txtInternetFecha.setText(valor(adendum.getInternetFecha()));
        txtInternetDetalle.setText(valor(adendum.getInternetObservacion()));
        txtCableMonto.setText(valor(adendum.getCableMonto()));
        txtCableFecha.setText(valor(adendum.getCableFecha()));
        txtCableDetalle.setText(valor(adendum.getCableObservacion()));
        txtAlquilerPrestamo.setText(valor(adendum.getAlquilerPrestamo()));
        txtAlimentacion.setText(valor(adendum.getAlimentacion()));
        txtMedicamentoMensual.setText(valor(adendum.getMedicamentoMensual()));
        txtOtroGastoUno.setText(valor(adendum.getOtroGastoUno()));
        txtOtroGastoDos.setText(valor(adendum.getOtroGastoDos()));
        txtObservaciones.setText(valor(adendum.getObservaciones()));
        txtFechaRec1.setText(valor(adendum.getFechaRecomendacionUno()));
        txtFechaRec2.setText(valor(adendum.getFechaRecomendacionDos()));
        txtFechaRec3.setText(valor(adendum.getFechaRecomendacionTres()));
        seleccionarRecomendacion(adendum.getRecomendacionUno(), rbRec1Si, rbRec1No);
        seleccionarRecomendacion(adendum.getRecomendacionDos(), rbRec2Si, rbRec2No);
        seleccionarRecomendacion(adendum.getRecomendacionTres(), rbRec3Si, rbRec3No);
        cargandoDatos = false;
    }

    private void seleccionarRecomendacion(String valor, RadioButton rbSi, RadioButton rbNo) {
        rbSi.setSelected("SI".equalsIgnoreCase(valor));
        rbNo.setSelected("NO".equalsIgnoreCase(valor));
    }

    private void guardarAutomaticamente() {
        if (registroActual == null || cargandoDatos) {
            return;
        }
        if (!tieneDatosIngresados()) {
            mostrarEstado("No hay datos para guardar todavia.", false);
            return;
        }
        if (adendumActual == null) {
            adendumActual = new AdendumAyuda();
            adendumActual.setRegistro(registroActual);
        }
        mapearFormularioAEntidad();
        try {
            if (adendumActual.getIdAdendum() == null) {
                adendumAyudaDAO.guardar(adendumActual);
            } else {
                adendumAyudaDAO.actualizar(adendumActual);
            }
            mostrarEstado("Guardado automatico: " + FMT.format(LocalDateTime.now()), false);
        } catch (Exception e) {
            mostrarEstado("No se pudo guardar el adendum: " + e.getMessage(), true);
        }
    }

    private void mapearFormularioAEntidad() {
        adendumActual.setRegistro(registroActual);
        adendumActual.setDescripcionAdicional(normalizar(txtDescripcionAdicional.getText()));
        adendumActual.setElectricidadMonto(normalizar(txtElectricidadMonto.getText()));
        adendumActual.setElectricidadFecha(normalizar(txtElectricidadFecha.getText()));
        adendumActual.setElectricidadObservacion(normalizar(txtElectricidadDetalle.getText()));
        adendumActual.setAguaMonto(normalizar(txtAguaMonto.getText()));
        adendumActual.setAguaFecha(normalizar(txtAguaFecha.getText()));
        adendumActual.setAguaObservacion(normalizar(txtAguaDetalle.getText()));
        adendumActual.setTelefonoMonto(normalizar(txtTelefonoMonto.getText()));
        adendumActual.setTelefonoFecha(normalizar(txtTelefonoFecha.getText()));
        adendumActual.setTelefonoObservacion(normalizar(txtTelefonoDetalle.getText()));
        adendumActual.setInternetMonto(normalizar(txtInternetMonto.getText()));
        adendumActual.setInternetFecha(normalizar(txtInternetFecha.getText()));
        adendumActual.setInternetObservacion(normalizar(txtInternetDetalle.getText()));
        adendumActual.setCableMonto(normalizar(txtCableMonto.getText()));
        adendumActual.setCableFecha(normalizar(txtCableFecha.getText()));
        adendumActual.setCableObservacion(normalizar(txtCableDetalle.getText()));
        adendumActual.setAlquilerPrestamo(normalizar(txtAlquilerPrestamo.getText()));
        adendumActual.setAlimentacion(normalizar(txtAlimentacion.getText()));
        adendumActual.setMedicamentoMensual(normalizar(txtMedicamentoMensual.getText()));
        adendumActual.setOtroGastoUno(normalizar(txtOtroGastoUno.getText()));
        adendumActual.setOtroGastoDos(normalizar(txtOtroGastoDos.getText()));
        adendumActual.setObservaciones(normalizar(txtObservaciones.getText()));
        adendumActual.setRecomendacionUno(valorRecomendacion(rbRec1Si, rbRec1No));
        adendumActual.setFechaRecomendacionUno(normalizar(txtFechaRec1.getText()));
        adendumActual.setRecomendacionDos(valorRecomendacion(rbRec2Si, rbRec2No));
        adendumActual.setFechaRecomendacionDos(normalizar(txtFechaRec2.getText()));
        adendumActual.setRecomendacionTres(valorRecomendacion(rbRec3Si, rbRec3No));
        adendumActual.setFechaRecomendacionTres(normalizar(txtFechaRec3.getText()));
        adendumActual.setActualizadoEn(LocalDateTime.now());
    }

    private String valorRecomendacion(RadioButton rbSi, RadioButton rbNo) {
        if (rbSi.isSelected()) return "SI";
        if (rbNo.isSelected()) return "NO";
        return null;
    }

    private boolean tieneDatosIngresados() {
        return tieneTexto(txtDescripcionAdicional)
                || tieneTexto(txtElectricidadMonto) || tieneTexto(txtElectricidadFecha) || tieneTexto(txtElectricidadDetalle)
                || tieneTexto(txtAguaMonto) || tieneTexto(txtAguaFecha) || tieneTexto(txtAguaDetalle)
                || tieneTexto(txtTelefonoMonto) || tieneTexto(txtTelefonoFecha) || tieneTexto(txtTelefonoDetalle)
                || tieneTexto(txtInternetMonto) || tieneTexto(txtInternetFecha) || tieneTexto(txtInternetDetalle)
                || tieneTexto(txtCableMonto) || tieneTexto(txtCableFecha) || tieneTexto(txtCableDetalle)
                || tieneTexto(txtAlquilerPrestamo) || tieneTexto(txtAlimentacion) || tieneTexto(txtMedicamentoMensual)
                || tieneTexto(txtOtroGastoUno) || tieneTexto(txtOtroGastoDos) || tieneTexto(txtObservaciones)
                || tieneTexto(txtFechaRec1) || tieneTexto(txtFechaRec2) || tieneTexto(txtFechaRec3)
                || rbRec1Si.isSelected() || rbRec1No.isSelected()
                || rbRec2Si.isSelected() || rbRec2No.isSelected()
                || rbRec3Si.isSelected() || rbRec3No.isSelected();
    }

    private boolean tieneTexto(TextInputControl control) {
        return control.getText() != null && !control.getText().trim().isEmpty();
    }

    private String normalizar(String valor) {
        if (valor == null) return null;
        String texto = valor.trim();
        return texto.isEmpty() ? null : texto;
    }

    private String valor(String texto) {
        return texto == null ? "" : texto;
    }

    @FXML
    private void limpiarFormulario() {
        limpiarFormularioInterno();
        pausaGuardado.stop();
        if (registroActual != null) {
            mostrarEstado("Formulario limpio. Se guardara cuando vuelva a ingresar informacion.", false);
        } else {
            lblEstado.setText("");
        }
    }

    private void limpiarFormularioInterno() {
        cargandoDatos = true;
        for (javafx.scene.Node child : contenedorFormulario.getChildrenUnmodifiable()) {
            if (child instanceof Parent parent) {
                limpiarNodoRecursivo(parent);
            }
        }
        cargandoDatos = false;
    }

    private void limpiarNodoRecursivo(Parent parent) {
        for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof TextField textField) {
                textField.clear();
            } else if (child instanceof TextArea textArea) {
                textArea.clear();
            } else if (child instanceof RadioButton radioButton) {
                radioButton.setSelected(false);
            } else if (child instanceof Parent nestedParent) {
                limpiarNodoRecursivo(nestedParent);
            }
        }
    }

    private void mostrarEstado(String mensaje, boolean error) {
        lblEstado.setText(mensaje);
        lblEstado.setStyle(error
                ? "-fx-text-fill: #8B3A3A; -fx-font-size: 12px; -fx-font-weight: bold;"
                : "-fx-text-fill: #5D682D; -fx-font-size: 12px; -fx-font-weight: bold;");
    }

    @FXML
    private void cerrarVentana() {
        pausaGuardado.stop();
        Stage stage = (Stage) lblEstado.getScene().getWindow();
        stage.close();
    }

    private void datePikerFechas(TextField textField){
        textField.focusedProperty().addListener((_, _, tieneFoco) -> {
            if (tieneFoco && !contenedorFormulario.isDisabled()) {
                abrirDatePicker(textField);
            }
        });
        textField.setEditable(false);
        textField.setStyle("-fx-cursor: hand;");
    }
    private void abrirDatePicker(TextField campoDestino) {
        DatePicker datePicker = new DatePicker();
        if (!campoDestino.getText().isEmpty()) {
            try {
                datePicker.setValue(LocalDate.parse(campoDestino.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } catch (Exception ignored) {}
        }

        javafx.stage.Popup popup = new javafx.stage.Popup();
        popup.getContent().add(datePicker);
        popup.setAutoHide(true);


        datePicker.setOnAction(_ -> {
            LocalDate fecha = datePicker.getValue();
            if (fecha != null) {
                campoDestino.setText(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                popup.hide();
            }
        });

        javafx.geometry.Bounds bounds = campoDestino.localToScreen(campoDestino.getBoundsInLocal());
        popup.show(campoDestino.getScene().getWindow(), bounds.getMinX(), bounds.getMaxY());
        datePicker.show();
    }
}
