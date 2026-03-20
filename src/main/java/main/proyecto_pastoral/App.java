package main.proyecto_pastoral;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Vistas/ParroquiaSector.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 500);
        stage.setTitle("Pastoral");
        stage.setScene(scene);
        stage.show();
    }
}
