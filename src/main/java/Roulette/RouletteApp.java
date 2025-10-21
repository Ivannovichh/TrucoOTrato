package Roulette;

import Roulette.WheelView;
import javafx.application.Application;
import javafx.scene.Scene;
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
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}