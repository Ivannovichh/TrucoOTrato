package Inteface;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Random;

public class ControladorFormulario {

    @FXML private Label titulo;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        // Asegurar el estado inicial brillante para el efecto de neón
        titulo.getStyleClass().remove("titulo-neon-dim");
        if (!titulo.getStyleClass().contains("titulo-neon-bright")) {
            titulo.getStyleClass().add("titulo-neon-bright");
        }

        // Iniciar parpadeo irregular con intervalos más espaciados
        iniciarParpadeo(titulo, 1100, 2100);
    }

    /**
     * Inicia un parpadeo aleatorio para un nodo.
     * @param nodo Nodo de JavaFX
     * @param minMillis Tiempo mínimo en milisegundos entre cambios
     * @param maxMillis Tiempo máximo en milisegundos entre cambios
     */
    private void iniciarParpadeo(javafx.scene.Node nodo, int minMillis, int maxMillis) {
        int intervalo = minMillis + random.nextInt(Math.max(1, maxMillis - minMillis));
        PauseTransition pausa = new PauseTransition(Duration.millis(intervalo));
        pausa.setOnFinished(event -> {
            alternarClase(nodo);
            iniciarParpadeo(nodo, minMillis, maxMillis);
        });
        pausa.play();
    }

    /**
     * Alterna entre los estados brillante y tenue del título.
     */
    private void alternarClase(javafx.scene.Node nodo) {
        if (nodo.getStyleClass().contains("titulo-neon-bright")) {
            nodo.getStyleClass().remove("titulo-neon-bright");
            if (!nodo.getStyleClass().contains("titulo-neon-dim")) {
                nodo.getStyleClass().add("titulo-neon-dim");
            }
        } else {
            nodo.getStyleClass().remove("titulo-neon-dim");
            if (!nodo.getStyleClass().contains("titulo-neon-bright")) {
                nodo.getStyleClass().add("titulo-neon-bright");
            }
        }
    }
}
