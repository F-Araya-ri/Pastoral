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

    @FXML private TextField txtNombre;
    @FXML private Button  btnGuardar;
    @FXML private Button  btnCancelar;
    @FXML private Label   lblMensaje;
    @FXML private ListView<Entrevistador> listaEntrevistadores;

    private EntrevistadorDAO entrevistadorDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        SessionFactory sf = HibernateUtil.getSessionFactory();
        entrevistadorDAO = new EntrevistadorDAO(sf);

        listaEntrevistadores.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Entrevistador e, boolean empty) {
                super.updateItem(e, empty);

                setText(empty || e == null ? null :
                        "#" + e.getIdEntrevistador() + " — " + e.getNombre());
            }
        });

        actualizarLista();
    }


    @FXML
    private void guardarEntrevistador() {

        // Obtenemos el texto y eliminamos espacios al inicio y al final
        String nombre = txtNombre.getText().trim();

        // ── VALIDACIONES ──────────────────────────────────────────────────

        if (nombre.isEmpty()) {
            mostrarMensaje("El nombre no puede estar vacío.", true);
            txtNombre.requestFocus();
            return;
        }

        if (nombre.length() < 3) {
            mostrarMensaje("El nombre debe tener al menos 3 caracteres.", true);
            txtNombre.requestFocus();
            return;
        }

        // Verificamos que no exista ya un entrevistador con ese nombre
        // buscarPorNombre() busca coincidencias exactas (ignorando mayúsculas)
        boolean yaExiste = entrevistadorDAO.buscarPorNombre(nombre).isPresent();
        if (yaExiste) {
            mostrarMensaje("⚠ Ya existe un entrevistador con ese nombre.", true);
            txtNombre.requestFocus();
            return;
        }

        // ── CREAR Y GUARDAR ───────────────────────────────────────────────

        // Creamos el objeto con el nombre ingresado
        // El ID lo asigna automáticamente la base de datos
        Entrevistador nuevo = new Entrevistador();
        nuevo.setNombre(nombre);

        try {
            entrevistadorDAO.guardar(nuevo);

            mostrarMensaje("✔ Entrevistador \"" + nombre + "\" guardado con ID #"
                    + nuevo.getIdEntrevistador(), false);

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
        lblMensaje.setText("");
        txtNombre.requestFocus(); // Ponemos el cursor en el campo para escribir de nuevo
    }

    // =====================================================================
    // MOSTRAR MENSAJE
    //   esError = true  → rojo
    //   esError = false → verde
    // =====================================================================
    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError
                ? "-fx-text-fill: #c0392b; -fx-font-weight: bold;"
                : "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
        );
    }
}
