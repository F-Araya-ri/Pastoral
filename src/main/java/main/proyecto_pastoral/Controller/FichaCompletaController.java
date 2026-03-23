package main.proyecto_pastoral.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.proyecto_pastoral.DAO.*;
import main.proyecto_pastoral.Model.*;
import main.proyecto_pastoral.Util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FichaCompletaController implements Initializable {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── SECCIÓN 1: DATOS DEL REGISTRO ────────────────────────────────
    @FXML private Label lblNumeroFicha;
    @FXML private Label lblFechaInicio;
    @FXML private Label lblFechaConclusion;
    @FXML private TextArea txtObservaciones;

    // ── SECCIÓN 2: DATOS PARROQUIALES ────────────────────────────────
    @FXML private Label lblParroquia;
    @FXML private Label lblSector;
    @FXML private TextArea txtDireccion;
    @FXML private Label lblTelefono;

    // ── SECCIÓN 3: PERSONAS A, B, C ──────────────────────────────────
    @FXML private Label lblNombreA;
    @FXML private Label lblDocA;
    @FXML private Label lblSexoA;
    @FXML private Label lblJefaturaA;

    @FXML private Label lblNombreB;
    @FXML private Label lblDocB;
    @FXML private Label lblSexoB;
    @FXML private Label lblJefaturaB;

    @FXML private Label lblNombreC;
    @FXML private Label lblDocC;
    @FXML private Label lblSexoC;
    @FXML private Label lblJefaturaC;

    // ── SECCIÓN 4: VIVIENDA ───────────────────────────────────────────
    @FXML private Label lblTipoVivienda;
    @FXML private Label lblTenencia;
    @FXML private Label lblCondicion;

    // ── DAOs ─────────────────────────────────────────────────────────
    private PersonaDAO personaDAO;
    private ViviendaDAO viviendaDAO;
    private RegistroDAO registroDAO;
    private ParroquiaDAO parroquiaDAO;
    private EntrevistadorDAO entrevistadorDAO;

    private Registro registroActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarDAOs();
    }

    public void inicializarConRegistro(Registro registro) {
        if (registro != null) {
            registroActual = registro;
            poblarFormulario(registro);
        }
    }

    private void inicializarDAOs() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        personaDAO       = new PersonaDAO(sessionFactory);
        viviendaDAO      = new ViviendaDAO(sessionFactory);
        registroDAO      = new RegistroDAO(sessionFactory);
        parroquiaDAO     = new ParroquiaDAO(sessionFactory);
        entrevistadorDAO = new EntrevistadorDAO(sessionFactory);
    }


    private void poblarFormulario(Registro registro) {
        try {
            // Sección 1: Datos del Registro
            lblNumeroFicha.setText(String.valueOf(registro.getNumeroRegistro()));  // ✅ FIX: getNumeroRegistro()
            lblFechaInicio.setText(registro.getFechaInicio() != null
                    ? registro.getFechaInicio().format(FMT) : "---");
            lblFechaConclusion.setText(registro.getFechaConclusion() != null
                    ? registro.getFechaConclusion().format(FMT) : "---");
            txtObservaciones.setText(registro.getObservaciones() != null
                    ? registro.getObservaciones() : "");

            // Sección 2: Datos Parroquiales
            // ✅ FIX: getSector() y getParroquia() con sus getters correctos
            if (registro.getSector() != null) {
                lblSector.setText(registro.getSector().getNombreSector());
                if (registro.getSector().getParroquia() != null) {
                    lblParroquia.setText(registro.getSector().getParroquia().getNombreParroquia());
                }
            }
            txtDireccion.setText(registro.getDireccion() != null ? registro.getDireccion() : "");
            lblTelefono.setText(registro.getTelefono() != null ? registro.getTelefono() : "---");

            // Sección 3: Personas
            // ✅ FIX: getNumeroRegistro() en lugar de getIdRegistro()
            List<Persona> personas = personaDAO.buscarPorRegistro(registro.getNumeroRegistro());
            if (personas.size() > 0) poblarPersona(personas.get(0), 'A');
            if (personas.size() > 1) poblarPersona(personas.get(1), 'B');
            if (personas.size() > 2) poblarPersona(personas.get(2), 'C');

            // Sección 4: Vivienda
            // ✅ FIX: buscarPorRegistro retorna Optional<Vivienda>, se maneja con ifPresent
            Optional<Vivienda> viviendaOpt = viviendaDAO.buscarPorRegistro(registro.getNumeroRegistro());
            if (viviendaOpt.isPresent()) {
                Vivienda vivienda = viviendaOpt.get();
                lblTipoVivienda.setText(vivienda.getTipo()      != null ? vivienda.getTipo()      : "---");
                lblTenencia.setText(vivienda.getTenencia()      != null ? vivienda.getTenencia()  : "---");
                lblCondicion.setText(vivienda.getCondicion()    != null ? vivienda.getCondicion() : "---");
            } else {
                lblTipoVivienda.setText("---");
                lblTenencia.setText("---");
                lblCondicion.setText("---");
            }

        } catch (Exception e) {
            mostrarError("Error poblando formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void poblarPersona(Persona persona, char letra) {
        String nombre = (persona.getPrimerApellido()  != null ? persona.getPrimerApellido()  : "") + " " +
                (persona.getSegundoApellido() != null ? persona.getSegundoApellido() : "") + ", " +
                (persona.getNombre()          != null ? persona.getNombre()          : "");

        String sexo     = persona.getSexo() != null ? persona.getSexo() : "---";
        String jefatura = persona.isJefatura() ? "Sí" : "No";
        // ✅ FIX: getter correcto es getnumeroIdentificacion() (n minúscula en el modelo)
        String doc      = persona.getnumeroIdentificacion() != null ? persona.getnumeroIdentificacion() : "---";

        switch (letra) {
            case 'A':
                lblNombreA.setText(nombre.trim());
                lblDocA.setText(doc);
                lblSexoA.setText(sexo);
                lblJefaturaA.setText(jefatura);
                break;
            case 'B':
                lblNombreB.setText(nombre.trim());
                lblDocB.setText(doc);
                lblSexoB.setText(sexo);
                lblJefaturaB.setText(jefatura);
                break;
            case 'C':
                lblNombreC.setText(nombre.trim());
                lblDocC.setText(doc);
                lblSexoC.setText(sexo);
                lblJefaturaC.setText(jefatura);
                break;
        }
    }

    private void limpiarFormulario() {
        lblNumeroFicha.setText("---");
        lblFechaInicio.setText("--/--/----");
        lblFechaConclusion.setText("--/--/----");
        txtObservaciones.setText("");
        lblParroquia.setText("---");
        lblSector.setText("---");
        txtDireccion.setText("");
        lblTelefono.setText("---");

        lblNombreA.setText("---");   lblDocA.setText("---");
        lblSexoA.setText("---");     lblJefaturaA.setText("---");

        lblNombreB.setText("---");   lblDocB.setText("---");
        lblSexoB.setText("---");     lblJefaturaB.setText("---");

        lblNombreC.setText("---");   lblDocC.setText("---");
        lblSexoC.setText("---");     lblJefaturaC.setText("---");

        lblTipoVivienda.setText("---");
        lblTenencia.setText("---");
        lblCondicion.setText("---");

        registroActual = null;
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
