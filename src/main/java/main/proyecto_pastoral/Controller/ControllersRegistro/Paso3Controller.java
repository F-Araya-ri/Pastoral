package main.proyecto_pastoral.Controller.ControllersRegistro;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.proyecto_pastoral.DAO.IngresoFamiliarDAO;
import main.proyecto_pastoral.DAO.PersonaDAO;
import main.proyecto_pastoral.Model.IngresoFamiliar;
import main.proyecto_pastoral.Model.Persona;
import main.proyecto_pastoral.Model.Registro;
import main.proyecto_pastoral.Util.HibernateUtil;


import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Paso3Controller implements Initializable {

    @FXML private TextField txtNombre,txtprimerApellido,txtsegundoApellido, txtDocumento, txtEdad, txtPais,txtTelfono;
    @FXML private TextField txtOcupacion, txtIngreso;
    @FXML private ComboBox<String> cmbTipoDoc,cmbSexo,cmbRelacion,cmbMigracion,cmbEducacion,cmbSalud,cmbSeguro;
    @FXML private CheckBox chkJefatura, chkTrabaja;
    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String>  colNombre, colDocumento, colSexo, colRelacion;
    @FXML private TableColumn<Persona, Integer> colEdad;
    @FXML private TableColumn<Persona, Boolean> colJefatura;

    private PersonaDAO personaDAO;
    private IngresoFamiliarDAO ingresoDAO;
    private Registro registro;
    private final ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
    private javafx.beans.value.ChangeListener<String> cedulaListener = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        personaDAO = new PersonaDAO(HibernateUtil.getSessionFactory());
        ingresoDAO = new IngresoFamiliarDAO(HibernateUtil.getSessionFactory());

        cmbTipoDoc.setItems(FXCollections.observableArrayList("Cédula", "DIMEX", "Pasaporte", "Sin documento"));
        cmbSexo.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));
        cmbRelacion.setItems(FXCollections.observableArrayList("Jefe/a", "Cónyuge", "Hijo/a", "Padre/Madre", "Otro familiar", "No familiar"));
        cmbMigracion.setItems(FXCollections.observableArrayList("Ciudadano/a","Residentes Permanente","No Inmigrante","Protegido","Indocumentados"));
        cmbEducacion.setItems(FXCollections.observableArrayList("Educación Preescolar","Educación General Básica","Educación Diversificada","Educación Superior"));
        cmbSeguro.setItems(FXCollections.observableArrayList("Asegurado","No asegurado","En trámite/Proceso"));
        cmbSalud.setItems(FXCollections.observableArrayList("Excelente","Bueno","Regular","Malo/Delicado"));
        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreCompleto())
        );
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroIdentificacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colJefatura.setCellValueFactory(new PropertyValueFactory<>("jefatura"));
        colRelacion.setCellValueFactory(new PropertyValueFactory<>("relacion"));
        tablaPersonas.setItems(listaPersonas);

        cmbTipoDoc.valueProperty().addListener((_, _, nuevo) -> aplicarFormatoDocumento(nuevo));
        txtTelfono.textProperty().addListener((_, _, newText) -> {
            String soloNumeros = newText.replaceAll("\\D", "");
            if (soloNumeros.length() > 8) soloNumeros = soloNumeros.substring(0, 8);

            if (soloNumeros.length() > 4) {
                txtTelfono.setText(soloNumeros.substring(0, 4) + "-" + soloNumeros.substring(4));
            } else {
                txtTelfono.setText(soloNumeros);
            }
        });


    }

    public void setRegistro(Registro registro) { this.registro = registro; }

    @FXML
    private void agregarPersona() {
        if (txtNombre.getText().trim().isEmpty()) return;
        Persona persona = new Persona();
        persona.setRegistro(registro);
        persona.setNombre(txtNombre.getText().trim());
        persona.setPrimerApellido(txtprimerApellido.getText().trim());
        persona.setSegundoApellido(txtsegundoApellido.getText().trim());
        persona.setTipoDocumento(cmbTipoDoc.getValue());
        persona.setNumeroIdentificacion(txtDocumento.getText().trim());
        persona.setSexo(cmbSexo.getValue());
        if (chkJefatura.isSelected()){
            persona.setJefatura("SI");
        }else{
            persona.setJefatura("NO");
        }
        persona.setRelacion(cmbRelacion.getValue());
        persona.setEdad(txtEdad.getText().isEmpty() ? 0 : Integer.parseInt(txtEdad.getText().trim()));
        persona.setTelefono(txtTelfono.getText().replaceAll("\\D", ""));
        persona.setPais(txtPais.getText().trim());
        persona.setMigracion(cmbMigracion.getValue());
        persona.setEducacion(cmbEducacion.getValue());
        persona.setSalud(cmbSalud.getValue());
        persona.setSeguro(cmbSeguro.getValue());
        personaDAO.guardar(persona);


        IngresoFamiliar ingreso = new IngresoFamiliar();
        ingreso.setPersona(persona);
        ingreso.setOcupacion(txtOcupacion.getText().trim());

        if (chkTrabaja.isSelected()){
            ingreso.setTrabaja("SI");
        }else{
            ingreso.setTrabaja("NO");
        }
        if (!txtIngreso.getText().isEmpty()) {
            ingreso.setIngresoMensual(new BigDecimal(txtIngreso.getText().trim()));
        }
        ingresoDAO.guardar(ingreso);

        listaPersonas.add(persona);
        limpiarFormularioPersona();
    }

    private void limpiarFormularioPersona() {
        txtNombre.clear(); txtprimerApellido.clear();txtDocumento.clear(); txtEdad.clear();
        txtPais.clear();  txtOcupacion.clear();txtIngreso.clear(); txtsegundoApellido.clear();
       txtTelfono.clear(); chkJefatura.setSelected(false);chkTrabaja.setSelected(false);
        cmbTipoDoc.setValue(null);cmbSexo.setValue(null); cmbRelacion.setValue(null);
        cmbMigracion.setValue(null); cmbSalud.setValue(null); cmbEducacion.setValue(null);
        cmbSeguro.setValue(null);
    }

    public boolean validar(Consumer<String> mostrarError) {
        if (listaPersonas.isEmpty()) {
            mostrarError.accept("Debe agregar al menos una persona al hogar.");
            return false;
        }
        return true;
    }

    public int getCantidadPersonas() { return listaPersonas.size(); }


    private void aplicarFormatoDocumento(String tipo) {
        txtDocumento.setTextFormatter(null);
        txtDocumento.setDisable(false);
        txtDocumento.setPromptText("");
        txtDocumento.clear();

        if (cedulaListener != null) {
            txtDocumento.textProperty().removeListener(cedulaListener);
            cedulaListener = null;
        }

        if (tipo == null) return;

        switch (tipo) {
            case "Cédula" -> {
                txtDocumento.setPromptText("0-0000-0000");
                final boolean[] formateando = {false};

                cedulaListener = (_, _, newText) -> {
                    if (formateando[0]) return;

                    String soloNumeros = newText.replaceAll("\\D", "");
                    if (soloNumeros.length() > 9) soloNumeros = soloNumeros.substring(0, 9);

                    String resultado = formatearCedula(soloNumeros);

                    if (!txtDocumento.getText().equals(resultado)) {
                        formateando[0] = true;
                        javafx.application.Platform.runLater(() -> {
                            txtDocumento.setText(resultado);
                            txtDocumento.positionCaret(resultado.length());
                            formateando[0] = false;
                        });
                    }
                };

                txtDocumento.textProperty().addListener(cedulaListener);
            }

            case "DIMEX" -> {
                txtDocumento.setPromptText("000000000000 (máx. 12 dígitos)");
                txtDocumento.setTextFormatter(new TextFormatter<>(change -> {
                    if (change.getControlNewText().matches("\\d{0,12}")) return change;
                    return null;
                }));
            }

            case "Pasaporte" -> {
                txtDocumento.setPromptText("AB123456 (máx. 9 caracteres)");
                txtDocumento.setTextFormatter(new TextFormatter<>(change -> {
                    String nuevo = change.getControlNewText().toUpperCase();
                    if (nuevo.matches("[A-Z0-9]{0,9}")) {
                        change.setText(change.getText().toUpperCase());
                        return change;
                    }
                    return null;
                }));
            }

            case "Sin documento" -> {
                txtDocumento.setDisable(true);
                txtDocumento.setText("N/A");
            }
        }
    }

    private static String formatearCedula(String soloNumeros) {
        StringBuilder sb = new StringBuilder();
        if (!soloNumeros.isEmpty()) {
            sb.append(soloNumeros.charAt(0));
        }
        if (soloNumeros.length() >= 2) {
            sb.append("-").append(soloNumeros, 1, Math.min(5, soloNumeros.length()));
        }
        if (soloNumeros.length() >= 6) {
            sb.append("-").append(soloNumeros.substring(5));
        }
        return sb.toString();
    }

}
