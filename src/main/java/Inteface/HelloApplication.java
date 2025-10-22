package Inteface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    protected Stage primaryStage; // Main stage for the application

    // Entry point for the JavaFX application, initializes the primary stage
    @Override
    public void start(Stage primaryStage) {
        // Store the primary stage for later use
        this.primaryStage = primaryStage;
        // Show the initial scene
        showInitialScene();
    }

    // Loads and displays the initial FXML scene
    protected void showInitialScene() {
        try {
            // Load the FXML file for the initial view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inteface/hello-view.fxml"));
            // Create the root node from the FXML file
            Parent root = loader.load();
            // Create a new scene with the specified dimensions (750x573)
            Scene scene = new Scene(root, 750, 573);
            // Set the title of the application window
            primaryStage.setTitle("Túnel del Terror");
            // Set the scene on the primary stage
            primaryStage.setScene(scene);
            // Display the primary stage
            primaryStage.show();
        } catch (IOException e) {
            // Print stack trace if an error occurs during FXML loading
            e.printStackTrace();
        }
    }

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}