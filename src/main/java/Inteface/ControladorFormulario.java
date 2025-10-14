package Inteface;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Random;

public class ControladorFormulario {

    @FXML private Label titulo;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        // Aplicar clase base de neón al título
        titulo.getStyleClass().add("neon-red");

        // Iniciar parpadeo lento solo para el título
        iniciarParpadeo(titulo, 800, 1500);  // entre 0.8 y 1.5 segundos
    }

    /**
     * Inicia un parpadeo aleatorio para un nodo.
     * @param nodo Nodo de JavaFX
     * @param minMillis Tiempo mínimo en milisegundos entre cambios
     * @param maxMillis Tiempo máximo en milisegundos entre cambios
     */
    private void iniciarParpadeo(javafx.scene.Node nodo, int minMillis, int maxMillis) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(minMillis + random.nextInt(maxMillis)), e -> alternarClase(nodo))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Alterna entre las clases CSS neon-red y neon-red-dim.
     */
    private void alternarClase(javafx.scene.Node nodo) {
        if (nodo.getStyleClass().contains("neon-red")) {
            nodo.getStyleClass().remove("neon-red");
            nodo.getStyleClass().add("neon-red-dim");
        } else {
            nodo.getStyleClass().remove("neon-red-dim");
            nodo.getStyleClass().add("neon-red");
        }
    }
}
