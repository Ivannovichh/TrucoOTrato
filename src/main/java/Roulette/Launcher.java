package Roulette;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    private static String nombreJugador;
    private static String apellidosJugador;
    private static String cursoJugador;
    private static boolean appIniciada = false; // para no relanzar la app

    public static void open(String nombre, String apellidos, String curso) {
        nombreJugador = nombre;
        apellidosJugador = apellidos;
        cursoJugador = curso;

        if (!appIniciada) {
            appIniciada = true;
            // Solo la primera vez
            new Thread(() -> Application.launch(Launcher.class)).start();
        } /*else {
            // Si ya está corriendo, abrir desde el hilo de JavaFX
            Platform.runLater(() -> {
                try {
                    new Launcher().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }*/
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Inteface/hello-viewRouleta.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/Inteface/styleRouleta.css").toExternalForm());

        stage.setTitle("Ruleta de Halloween");
        stage.setScene(scene);
        stage.show();

        // Pasar los datos al controlador
        HelloControllerRouleta controller = fxmlLoader.getController();
        controller.recibirDatos(nombreJugador, apellidosJugador, cursoJugador);
    }
}
