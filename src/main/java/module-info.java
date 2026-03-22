module main.proyecto_pastoral {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens main.proyecto_pastoral.Model to org.hibernate.orm.core, javafx.base;
    opens main.proyecto_pastoral.Controller to javafx.fxml;
    opens main.proyecto_pastoral to javafx.fxml;
    exports main.proyecto_pastoral;
    opens main.proyecto_pastoral.Controller.ControllersRegistro to javafx.fxml;
}