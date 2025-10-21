package Inteface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    protected Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            showInitialScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showInitialScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inteface/hello-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 750, 573);
            primaryStage.setTitle("Túnel del Terror");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}