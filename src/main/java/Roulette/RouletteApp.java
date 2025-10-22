package Roulette;

import Inteface.HelloApplication;
import javafx.stage.Stage;

public class RouletteApp extends HelloApplication {
    // Overrides the start method to initialize the primary stage for the roulette application
    @Override
    public void start(Stage stage) {
        // Store the provided stage as the primary stage
        this.primaryStage = stage;
        // Call the inherited method to show the initial scene
        showInitialScene();
    }
}