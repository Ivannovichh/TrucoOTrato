package Roulette;

import Roulette.WheelView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RouletteApp extends Application {
    @Override
    public void start(Stage stage) {
        WheelView wheelView = new WheelView();
        wheelView.open("Juan", "Pérez García", "1º DAM");
        StackPane root = new StackPane(wheelView);
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        stage.setTitle("Ruleta Halloween de Terror");
        stage.setScene(scene);
        stage.setFullScreen(true); // Pantalla completa automática
        stage.setFullScreenExitHint(""); // Sin mensaje de salida
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Deshabilitar ESC
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}