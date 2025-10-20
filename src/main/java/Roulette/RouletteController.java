package Roulette;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

import java.util.Random;

public class RouletteController {

    @FXML private Group rueda;          // El grupo que rota (rueda completa)
    @FXML private Label resultadoLabel; // Muestra Truco/Trato/Oskar

    private final Random rnd = new Random();

    @FXML
    private void girar() {
        // Giro entre 5 y 8 vueltas + un offset aleatorio para no parar siempre igual
        double vueltas = 5 + rnd.nextInt(4);   // 5..8
        double extra = rnd.nextDouble() * 360; // 0..360

        RotateTransition rt = new RotateTransition(Duration.seconds(2.6), rueda);
        rt.setByAngle(360 * vueltas + extra);
        rt.setOnFinished(e -> evaluarResultado());
        rt.play();
    }

    private void evaluarResultado() {
        // El ángulo efectivo del puntero (0º arriba). Tomamos la rotación normalizada.
        double angle = (rueda.getRotate() % 360 + 360) % 360;

        // Definimos sectores: 0-180 rojo, 180-330 negro, 330-360 verde (simple y claro)
        String texto;
        if (angle >= 0 && angle < 180) {
            texto = "Truco";        // rojo
        } else if (angle >= 180 && angle < 330) {
            texto = "Trato";        // negro
        } else {
            texto = "Oskar";        // verde
        }
        resultadoLabel.setText(texto);
    }
}
