package Roulette;

import Inteface.HelloApplication;
import javafx.stage.Stage;

public class RouletteApp extends HelloApplication {
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showInitialScene();
    }
}