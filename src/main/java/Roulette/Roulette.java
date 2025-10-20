package Roulette;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Roulette extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar el archivo FXML de la interfaz de ruleta
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Roulette/roulette-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);  // Ajusta el tamaño si es necesario
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ruleta");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
