package main.proyecto_pastoral.Controller;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.proyecto_pastoral.DAO.EntrevistadorDAO;
import main.proyecto_pastoral.Model.Entrevistador;
import main.proyecto_pastoral.Util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class EntrevistadorController implements Initializable {

    @FXML
    private Button btnRegistroForms;
    @FXML
    private Label lblTotal;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtNombre;

    @FXML
    private Label lblMensaje;
    @FXML
    private ListView<Entrevistador> listaEntrevistadores;

    private EntrevistadorDAO entrevistadorDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SessionFactory sf = HibernateUtil.getSessionFactory();
        entrevistadorDAO = new EntrevistadorDAO(sf);


        listaEntrevistadores.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Entrevistador e, boolean empty) {
                super.updateItem(e, empty);

                setText(empty || e == null ? null :
                        "#" + e.getIdEntrevistador() + " — " + e.getNombre());
            }
        });

        actualizarLista();

        txtTelefono.textProperty().addListener((_, _, newText) -> {
            String soloNumeros = newText.replaceAll("\\D", "");
            if (soloNumeros.length() > 8) soloNumeros = soloNumeros.substring(0, 8);

            if (soloNumeros.length() > 4) {
                txtTelefono.setText(soloNumeros.substring(0, 4) + "-" + soloNumeros.substring(4));
            } else {
                txtTelefono.setText(soloNumeros);
            }
        });
    }


    @FXML
    private void guardarEntrevistador() {


        String Nombre = txtNombre.getText().trim();
        String Telefono = txtTelefono.getText().replaceAll("\\D", "");
        String Email = txtEmail.getText().trim();
        String Estado = "Activo";

        if (Nombre.isEmpty()) {
            mostrarMensaje("El nombre no puede estar vacío.", true);
            txtNombre.requestFocus();
            return;
        }

        if (Nombre.length() < 3) {
            mostrarMensaje("El nombre debe tener al menos 3 caracteres.", true);
            txtNombre.requestFocus();
            return;
        }

        boolean yaExiste = entrevistadorDAO.buscarPorNombre(Nombre).isPresent();
        if (yaExiste) {
            mostrarMensaje("⚠ Ya existe un entrevistador con ese nombre.", true);
            txtNombre.requestFocus();
            return;
        }

        // ── CREAR Y GUARDAR ───────────────────────────────────────────────

        // Creamos el objeto con el nombre ingresado
        // El ID lo asigna automáticamente la base de datos
        Entrevistador entrevistador = new Entrevistador();
        entrevistador.setNombre(Nombre);
        entrevistador.setTelefono(Telefono);
        entrevistador.setEmail(Email);
        entrevistador.setEstadoAdmin(Estado);

        try {
            entrevistadorDAO.guardar(entrevistador);

            mostrarMensaje("✔ Entrevistador \"" + Nombre + "\" guardado con ID #"
                    + entrevistador.getIdEntrevistador(), false);

            limpiarFormulario();

            // Recargamos la lista para que aparezca el nuevo entrevistador
            actualizarLista();

        } catch (Exception e) {
            mostrarMensaje("✘ Error al guardar: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    // =====================================================================
    // ACTUALIZAR LISTA
    // Recarga todos los entrevistadores desde la base de datos
    // y los muestra en el ListView.
    // =====================================================================
    private void actualizarLista() {
        try {
            List<Entrevistador> lista = entrevistadorDAO.listarTodos();
            // setItems reemplaza todo el contenido del ListView con la nueva lista
            listaEntrevistadores.setItems(FXCollections.observableArrayList(lista));
            lblTotal.setText(String.valueOf(lista.size()));
        } catch (Exception e) {
            mostrarMensaje("✘ Error al cargar la lista: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    // =====================================================================
    // LIMPIAR FORMULARIO
    // Deja el campo de texto vacío y borra cualquier mensaje.
    // =====================================================================
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtTelefono.clear();
        txtEmail.clear();
        lblMensaje.setText("");
        txtNombre.requestFocus(); // Ponemos el cursor en el campo para escribir de nuevo
    }

    // =====================================================================
    // MOSTRAR MENSAJE
    //   esError = true → rojo
    //   esError = false → verde
    // =====================================================================
    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError
                ? "-fx-text-fill: #c0392b; -fx-font-weight: bold;"
                : "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
        );
    }

    @FXML
    private void abrirInicioRegistro() {
        javafx.stage.Stage stage = (javafx.stage.Stage) btnRegistroForms.getScene().getWindow();
        stage.close();
    }
}
