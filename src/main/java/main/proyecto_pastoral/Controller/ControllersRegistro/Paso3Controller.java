package main.proyecto_pastoral.Controller.ControllersRegistro;

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

    @FXML private TextField txtNombre, txtDocumento, txtEdad, txtPais;
    @FXML private TextField txtMigracion, txtEducacion, txtSalud, txtSeguro;
    @FXML private TextField txtOcupacion, txtIngreso;
    @FXML private ComboBox<String> cmbTipoDoc, cmbSexo, cmbRelacion;
    @FXML private CheckBox chkJefatura, chkTrabaja;
    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String>  colNombre, colDocumento, colSexo, colRelacion;
    @FXML private TableColumn<Persona, Integer> colEdad;
    @FXML private TableColumn<Persona, Boolean> colJefatura;

    private PersonaDAO personaDAO;
    private IngresoFamiliarDAO ingresoDAO;
    private Registro registro;
    private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        personaDAO = new PersonaDAO(HibernateUtil.getSessionFactory());
        ingresoDAO = new IngresoFamiliarDAO(HibernateUtil.getSessionFactory());

        cmbTipoDoc.setItems(FXCollections.observableArrayList("Cédula", "DIMEX", "Pasaporte", "Sin documento"));
        cmbSexo.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));
        cmbRelacion.setItems(FXCollections.observableArrayList("Jefe/a", "Cónyuge", "Hijo/a", "Padre/Madre", "Otro familiar", "No familiar"));

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroIdentificacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colJefatura.setCellValueFactory(new PropertyValueFactory<>("jefatura"));
        colRelacion.setCellValueFactory(new PropertyValueFactory<>("relacion"));
        tablaPersonas.setItems(listaPersonas);
    }

    public void setRegistro(Registro registro) { this.registro = registro; }

    @FXML
    private void agregarPersona() {
        if (txtNombre.getText().trim().isEmpty()) return;
        Persona persona = new Persona();
        persona.setRegistro(registro);
        persona.setNombre(txtNombre.getText().trim());
        persona.setTipoDocumento(cmbTipoDoc.getValue());
        persona.setnumeroIdentificacion(txtDocumento.getText().trim());
        persona.setSexo(cmbSexo.getValue());
        persona.setJefatura(chkJefatura.isSelected());
        persona.setRelacion(cmbRelacion.getValue());
        persona.setEdad(txtEdad.getText().isEmpty() ? 0 : Integer.parseInt(txtEdad.getText().trim()));
        persona.setPais(txtPais.getText().trim());
        persona.setMigracion(txtMigracion.getText().trim());
        persona.setEducacion(txtEducacion.getText().trim());
        persona.setSalud(txtSalud.getText().trim());
        persona.setSeguro(txtSeguro.getText().trim());
        personaDAO.guardar(persona);

        // Guardar ingreso familiar
        IngresoFamiliar ingreso = new IngresoFamiliar();
        ingreso.setPersona(persona);
        ingreso.setOcupacion(txtOcupacion.getText().trim());
        ingreso.setTrabaja(chkTrabaja.isSelected());
        if (!txtIngreso.getText().isEmpty()) {
            ingreso.setIngresoMensual(new BigDecimal(txtIngreso.getText().trim()));
        }
        ingresoDAO.guardar(ingreso);

        listaPersonas.add(persona);
        limpiarFormularioPersona();
    }

    private void limpiarFormularioPersona() {
        txtNombre.clear(); txtDocumento.clear(); txtEdad.clear();
        txtPais.clear(); txtMigracion.clear(); txtEducacion.clear();
        txtSalud.clear(); txtSeguro.clear(); txtOcupacion.clear();
        txtIngreso.clear(); chkJefatura.setSelected(false);
        chkTrabaja.setSelected(false); cmbTipoDoc.setValue(null);
        cmbSexo.setValue(null); cmbRelacion.setValue(null);
    }

    public boolean validar(Consumer<String> mostrarError) {
        if (listaPersonas.isEmpty()) {
            mostrarError.accept("Debe agregar al menos una persona al hogar.");
            return false;
        }
        return true;
    }

    public int getCantidadPersonas() { return listaPersonas.size(); }
}
