package main.proyecto_pastoral.Controller.ControllersRegistro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.proyecto_pastoral.DAO.ViviendaDAO;
import main.proyecto_pastoral.Model.Registro;
import main.proyecto_pastoral.Model.Vivienda;
import main.proyecto_pastoral.Util.HibernateUtil;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Paso2Controller implements Initializable {

    @FXML private RadioButton rbCasa, rbApartamento, rbCuarto, rbTugurio, rbAlbergue, rbNinguna;
    @FXML private RadioButton rbPropia, rbPagaPlazos, rbAlquilada, rbPrestada, rbPrecario;
    @FXML private RadioButton rbBuena, rbRegular, rbMala, rbNoAplica;

    @FXML private ToggleGroup tipoGroup, tenenciaGroup, condicionGroup;

    private ViviendaDAO viviendaDAO;
    private Registro registro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viviendaDAO = new ViviendaDAO(HibernateUtil.getSessionFactory());
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public boolean validar(Consumer<String> mostrarError) {
        if (tipoGroup.getSelectedToggle() == null) {
            mostrarError.accept("Debe seleccionar el tipo de vivienda.");
            return false;
        }
        if (tenenciaGroup.getSelectedToggle() == null) {
            mostrarError.accept("Debe seleccionar la tenencia.");
            return false;
        }
        if (condicionGroup.getSelectedToggle() == null) {
            mostrarError.accept("Debe seleccionar la condición.");
            return false;
        }
        guardar();
        return true;
    }

    private void guardar() {
        Vivienda vivienda = new Vivienda();
        vivienda.setRegistro(registro);
        vivienda.setTipo(getTextoToggle(tipoGroup));
        vivienda.setTenencia(getTextoToggle(tenenciaGroup));
        vivienda.setCondicion(getTextoToggle(condicionGroup));
        viviendaDAO.guardar(vivienda);
    }

    private String getTextoToggle(ToggleGroup group) {
        Toggle seleccionado = group.getSelectedToggle();
        if (seleccionado == null) return "";
        return ((RadioButton) seleccionado).getText();
    }

    public String getTipo()      { return getTextoToggle(tipoGroup); }
    public String getTenencia()  { return getTextoToggle(tenenciaGroup); }
    public String getCondicion() { return getTextoToggle(condicionGroup); }
}
